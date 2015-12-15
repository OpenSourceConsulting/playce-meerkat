/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 12. 5.		First Draft.
 */
package com.athena.dolly.core;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

/**
 * <pre>
 * javassist를 이용하여 지정된 클래스에 대해 BCI(Byte Code Instrumentation)를 수행하는 변환 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyClassTransformer implements ClassFileTransformer {
	
	private List<String> classList;
	private List<String> ssoDomainList;
	private boolean verbose;
	private boolean enableSSO;
    private String ssoParamKey;

	private Map<ClassLoader, ClassPool> pools = new HashMap<ClassLoader, ClassPool>();
	
	public DollyClassTransformer(boolean verbose, List<String> classList, boolean enableSSO, List<String> ssoDomainList, String ssoParamKey) {
		this.verbose = verbose;
		this.classList = classList;
		this.enableSSO = enableSSO;
		this.ssoDomainList = ssoDomainList;
		this.ssoParamKey = ssoParamKey;
	}//end of constructor()

	/* (non-Javadoc)
	 * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
	 */
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        CtClass cl = null;
		
        try {
            if (className != null && acceptClass(className)) {
            	
        		ClassPool pool = getClassPool(loader);
        		
                if (verbose) {
                    System.out.println("[Dolly] Enhancing class : " + className);
                }

                // Transform
                cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                
                if (cl.subtypeOf(pool.get("javax.servlet.http.HttpSession"))) {
                	return instumentHttpSession(className, cl);
                } 
                
                // SSO 활성화 시 주어진 SessionID 값을 JSESSIONID로 사용하도록 변환 
				if (cl.subtypeOf(pool.get("javax.servlet.http.HttpServletRequest"))) {
					return instumentRequest(className, cl, enableSSO);
				}
                
                // Tomcat, JBoss 외의 WAS에서는 org.apache.catalina.session.ManagerBase로 타입 검사를 수행할 경우 
                // ClassNotFound Exception이 발생하므로 문자열을 비교한다.
                if (className.startsWith("org/apache/catalina/session/ManagerBase")) {
                	return instumentManager(className, cl);
                }
                
                // Debug 용으로써 Target Class로 지정된 클래스(위 HttpSession과 ManagerBase 등은 제외)의 모든 메소드의 실행 시 로깅을 수행한다.
            	CtMethod[] methods = cl.getDeclaredMethods();
				for (int i = 0; i < methods.length; i++) {
					if (methods[i].isEmpty() == false) {
						String signature = getSignature(methods[i]);
						String returnValue = returnValue(methods[i]);
						
						methods[i].insertAfter("System.out.println(\"" + className.replace("/", ".") + " : " + signature + returnValue + ");");
					}
				}
                
				return cl.toBytecode();
            }
        } catch (Exception e) {
            System.err.println("[Dolly] Warning: could not enhance class " + className + " : " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        
        return classfileBuffer;
	}//end of transform()

	/**
	 * <pre>
	 * HttpSession 구현 클래스에 대해 (set/get/remove)Attribute, getAttributeNames, invalidate 메소드 호출 시
	 * Infinispan Data Grid에 해당 내용을 함께 저장/조회/삭제 하도록 byte code를 조작한다.
	 * </pre>
	 * @param className
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	private byte[] instumentHttpSession(String className, CtClass cl) throws Exception {
		byte[] redefinedClassfileBuffer = null;

		if (cl.isInterface() == false) {
			CtMethod[] methods = cl.getDeclaredMethods();
			CtClass[] params = null;
			boolean isEnhanced = false;
			
			CtField f = CtField.make("private java.lang.String _id = null;", cl);
			cl.addField(f);
			
			// getAttribute() 변환 시 _getAttributeNames() 메소드가 변환되어 있어야 하기 때문에 methods를 역으로 탐색한다.
			for (int i = methods.length - 1; i >= 0; i--) {
				if (methods[i].isEmpty()) {
					continue;
				}

				params = methods[i].getParameterTypes();
				
				// Weblogic의 경우 sessionId는 ByhvS3mGpb5cZgn3BfmrSDN3gvQL5YVydpMdGDpCFP3xkYdM3LBS!1397924265!1387783814698 형태이며,
				// 제일 처음 ! 앞 부분만 키로 사용한다.
				
				// Tomcat, JBoss의 경우 clustering 된 sessionId는 A4BBE7439E743B2E0CCD3B695E0F0DC9.node1 형태이며,
				// 제일 처음 . 앞 부분만 키로 사용한다.
				
				String body = null;
				isEnhanced = false;
				if (methods[i].getName().equals("setAttribute")) {
					if (params.length == 2) {		                
						body =		"{" +
									"	if (_id == null) { " +
									"		_id = getId();" +
									"		_id = _id.split(\"!\")[0];" +
									" 		java.lang.String[] _ids = _id.split(\"\\\\.\");" +
									"		_id = _ids[0];" +
									"		if (_ids.length > 1) {" +
									"			try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, \"jvmRoute\", _ids[1]); } catch (Exception e) { e.printStackTrace(); }" +
									"		}" + 
									"	}";

		                if (verbose) {
							body += "	System.out.println(\"[Dolly] Session(\" + _id + \") setAttribute(\" + $1 + \", \" + $2 + \") has called.\");";
		                }
		                
						body +=	   "	try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, $1, $2); } catch (Exception e) { e.printStackTrace(); }" +
								   "	try { _setAttribute($1, $2); } catch (Exception e) { e.printStackTrace(); }" + 
								   "}";
						isEnhanced = true;
					}
				} else if (methods[i].getName().equals("getAttribute")) {
					//*                
					body =		"{" +
								"	if (_id == null) { " +
								"		_id = getId();" +
								"		_id = _id.split(\"!\")[0];" +
								" 		java.lang.String[] _ids = _id.split(\"\\\\.\");" +
								"		_id = _ids[0];" +
								"		if (_ids.length > 1) {" +
								"			try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, \"jvmRoute\", _ids[1]); } catch (Exception e) { e.printStackTrace(); }" +
								"		}" + 
								"	}";
					
					if (verbose) {
						body += "	System.out.println(\"[Dolly] Session(\" + _id + \") getAttribute(\" + $1 + \") has called.\");";
	                }
	                
					// Expiration(time-out) 갱신을 위해 Session Server에서 먼저 조회한다.
					body +=	   "	java.lang.Object obj = null;" +
							   "	try { obj = com.athena.dolly.common.cache.DollyManager.getClient().get(_id, $1); } catch (Exception e) { e.printStackTrace(); }";

	                if (verbose) {
						body += "	if (obj == null) { System.out.println(\"[Dolly] Attribute does not exist in Session Server. Trying search in Local Session.\"); }";
	                }

					body +=	   "	if (obj == null) {" +
							   "		try { " +
							   "			obj = _getAttribute($1);" +
							   " 			if (obj != null && !com.athena.dolly.common.cache.DollyManager.isSkipConnection()) {" +
							   "				System.out.println(\"[Dolly] Attribute exists in Local Session and copy to Session Server.\");" + 
							   "				java.util.Enumeration e = _getAttributeNames();" + 
							   "				java.lang.String key = null;" +
							   "				while (e.hasMoreElements()) { " +
							   "					key = (java.lang.String) e.nextElement();" +
					   		   "					com.athena.dolly.common.cache.DollyManager.getClient().put(_id, key, _getAttribute(key));" +
							   "				}" +
							   "			}" +
							   "		} catch (Exception e) { e.printStackTrace(); }" +
							   "	}";
							   
					if (verbose) {
						body += "	System.out.println(\"[Dolly] Session(\" + _id + \") getAttribute()'s result => \" + obj);";
					}
					/*/    
					body =		"{" +
								"	if (_id == null) { " +
								"		_id = getId();" +
								"		_id = _id.split(\"!\")[0];" +
								" 		java.lang.String[] _ids = _id.split(\"\\\\.\");" +
								"		_id = _ids[0];" +
								"		if (_ids.length > 1) {" +
								"			try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, \"jvmRoute\", _ids[1]); } catch (Exception e) { e.printStackTrace(); }" +
								"		}" + 
								"	}";
					
	                if (verbose) {
						body += "	System.out.println(\"[Dolly] Session(\" + _id + \") getAttribute(\" + $1 + \") has called.\");";
	                }
	                
					body +=	   "	java.lang.Object obj = null;" +
							   "	try { obj = _getAttribute($1); } catch (Exception e) { e.printStackTrace(); }";

	                if (verbose) {
						body += "	if (obj == null) { System.out.println(\"[Dolly] Attribute does not exist in local session. Trying search in Session Server.\"); }";
	                }
	                
					body +=	   "	if (obj == null) {" +
							   "		try { " +
							   "			obj = com.athena.dolly.common.cache.DollyManager.getClient().get(_id, $1); " +
							   " 			if (obj != null) {" +
							   "				java.util.Enumeration e = com.athena.dolly.common.cache.DollyManager.getClient().getValueNames(_id);" + 
							   "				java.lang.String key = null;" +
							   "				while (e.hasMoreElements()) { " +
							   "					key = e.nextElement();";
						
					if (className.startsWith("org/apache/catalina/session/StandardSessionFacade")) {
						body += "					session.setAttribute(key, com.athena.dolly.common.cache.DollyManager.getClient().get(_id, key));";
					} else {
						body += "					attributes.put(key, com.athena.dolly.common.cache.DollyManager.getClient().get(_id, key));";
					}
					
					body +=	   "				}" +
							   "			}" +
							   "		} catch (Exception e) { e.printStackTrace(); }" +
							   "	}";
							   
					if (verbose) {
						body += "	System.out.println(\"[Dolly] Session(\" + _id + \") getAttribute()'s result => \" + obj);";
					}
					//*/
							   
					body +=    "	return obj;" +
							   "}";
					isEnhanced = true;
				} else if (methods[i].getName().equals("getAttributeNames")) {	                 
					body =		"{" +
								"	if (_id == null) { " +
								"		_id = getId();" +
								"		_id = _id.split(\"!\")[0];" +
								" 		java.lang.String[] _ids = _id.split(\"\\\\.\");" +
								"		_id = _ids[0];" +
								"		if (_ids.length > 1) {" +
								"			try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, \"jvmRoute\", _ids[1]); } catch (Exception e) { e.printStackTrace(); }" +
								"		}" + 
								"	}";

	                if (verbose) {
						body += "	System.out.println(\"[Dolly] Session(\" + _id + \") getAttributeNames() has called.\");";
	                }
	                
					body +=	   "	java.util.Enumeration obj = null;" +
							   "	try { obj = com.athena.dolly.common.cache.DollyManager.getClient().getValueNames(_id); } catch (Exception e) { e.printStackTrace(); }" +
							   "	if (obj == null) {" +
							   "		try { obj = _getAttributeNames(); } catch (Exception e) { e.printStackTrace(); }" +
							   "	}" +
							   "	return obj;" +
							   "}";
					isEnhanced = true;
				} else if (methods[i].getName().equals("removeAttribute")) {
					if (params.length == 1) {	                     
						body =		"{" +
									"	if (_id == null) { " +
									"		_id = getId();" +
									"		_id = _id.split(\"!\")[0];" +
									" 		java.lang.String[] _ids = _id.split(\"\\\\.\");" +
									"		_id = _ids[0];" +
									"		if (_ids.length > 1) {" +
									"			try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, \"jvmRoute\", _ids[1]); } catch (Exception e) { e.printStackTrace(); }" +
									"		}" + 
									"	}";

		                if (verbose) {
							body += "	System.out.println(\"[Dolly] Session(\" + _id + \") removeAttribute(\" + $1 + \") has called.\");";
		                }
		                
						body +=	   "	try { com.athena.dolly.common.cache.DollyManager.getClient().remove(_id, $1); } catch (Exception e) { e.printStackTrace(); }" +
								   "	try { _removeAttribute($1); } catch (Exception e) { e.printStackTrace(); }" +
								   "}";
						isEnhanced = true;
					}
				} else if (methods[i].getName().equals("invalidate")) {
					if (params.length == 0) {         	                
						body =		"{" +
									"	if (_id == null) { " +
									"		_id = getId();" +
									"		_id = _id.split(\"!\")[0];" +
									" 		java.lang.String[] _ids = _id.split(\"\\\\.\");" +
									"		_id = _ids[0];" +
									"		if (_ids.length > 1) {" +
									"			try { com.athena.dolly.common.cache.DollyManager.getClient().put(_id, \"jvmRoute\", _ids[1]); } catch (Exception e) { e.printStackTrace(); }" +
									"		}" + 
									"	}";

		                if (verbose) {
							body += "	System.out.println(\"[Dolly] Session(\" + _id + \") invalidate() has called.\");";
		                }
		                
						body +=	   "	try { com.athena.dolly.common.cache.DollyManager.getClient().remove(_id); } catch (Exception e) { e.printStackTrace(); }" +
								   "	try { _invalidate(); } catch (Exception e) { e.printStackTrace(); }" +
								   "}";
						isEnhanced = true;
					}
				} else if (methods[i].getName().equals("reuseSessionId")) {
					body =     "{" +
							   "	return true;" +
							   "}";
					isEnhanced = true;
				}
				
				if (isEnhanced) {
					CtMethod newMethod = CtNewMethod.copy(methods[i], cl, null);
					methods[i].setName("_" + methods[i].getName());
					newMethod.setBody(body);
					cl.addMethod(newMethod);
					
					if (verbose) {
						System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
					}
				}
			}
			
			redefinedClassfileBuffer = cl.toBytecode();
		}
		
		return redefinedClassfileBuffer;
	}//end of instumentHttpSession()
	
	/**
	 * <pre>
	 * org.apache.catalina.session.ManagerBase 클래스에 대해 findSession 메소드 호출 시 해당하는 세션이 없으면
	 * 인자에 해당하는 세션 아이디로 새로운 세션을 만들도록 byte code를 조작한다.
	 * </pre>
	 * @param className
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	private byte[] instumentManager(String className, CtClass cl) throws Exception {
		byte[] redefinedClassfileBuffer = null;
		
		CtMethod[] methods = cl.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].isEmpty()) {
				continue;
			}
			
			if (methods[i].getName().equals("findSession")) {
				CtMethod method = cl.getDeclaredMethod("createSession");
				CtClass[] params = method.getParameterTypes();

				String body = 	"{" +
						   		"	if ($1 == null) {" +
						   		"		return null;" +
						   		"	}" +
						   		"	if (sessions.get($1) == null) {";
				
				// Jboss EAP 6의 경우 createSession()의 파라메타가 2개
				if (params.length == 2) {
					body +=		"		return createSession($1, null);";
				} else {
					body +=		"		return createSession($1);";
				}
				
				body +=			"	} else {" +
				   				"		return (org.apache.catalina.Session) sessions.get($1);" +
				   				"	}" +
				   				"}";

				CtMethod newMethod = CtNewMethod.copy(methods[i], cl, null);
				methods[i].setName("_" + methods[i].getName());
				newMethod.setBody(body);				
				cl.addMethod(newMethod);
				
				//newMethod.insertBefore("System.out.println(\"=========== findSession(\" + $1 + \") ===========\");");
				
				if (verbose) {
					System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
				}
			}
		}
		
		redefinedClassfileBuffer = cl.toBytecode();
		
		return redefinedClassfileBuffer;
	}//end of instumentManager()
	
	/**
	 * <pre>
	 * org.apache.catalina.connector.Request 구현 클래스에 대해 getRequestedSessionId 메소드 호출 시 
	 * enableSSO가 true 이고, 현재 도메인이 ssoDomainList에 있으면 Data Grid에서 client IP Address 에 해당하는 session ID를 
	 * 조회하여 반환하도록 byte code를 조작한다.
	 * </pre>
	 * @param className
	 * @param cl
	 * @param enableSSO
	 * @return
	 * @throws Exception
	 */
	private byte[] instumentRequest(String className, CtClass cl, boolean enableSSO) throws Exception {
		byte[] redefinedClassfileBuffer = null;
		
		CtMethod[] methods = cl.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].isEmpty()) {
				continue;
			}

			if (enableSSO && className.equals("org/apache/catalina/connector/Request") && methods[i].getName().equals("doGetSession")) {
				String body = 	"boolean ssoDomain = false;" +
						   		"String serverName = getServerName();";
				
				for (int j = 0; j < ssoDomainList.size(); j++) {
					if (j > 0) {
						body += "else ";
					}
					
					body += 	"if (serverName.endsWith(\"" + ssoDomainList.get(j) + "\")) {" +
								"  	ssoDomain = true;" +
								"}";
				}
				
				body +=			"if (ssoDomain) {" +
								"	String newId = getParameter(\"" + ssoParamKey + "\");" +
								"	if (newId != null && newId.length() > 0) {" +
								"		setRequestedSessionId(newId);" +
								"		changeSessionId(newId);" +
								"	}" +
								"}";
				
				methods[i].insertBefore(body);
			} else if (enableSSO && className.equals("weblogic/servlet/internal/ServletRequestImpl") && methods[i].getName().equals("getSession")) {
				CtClass[] params = methods[i].getParameterTypes();
				
				if (params == null || params.length < 1) {
					continue;
				}
				
				String body = 	"{" +
								"	boolean ssoDomain = false;" +
						   		"	String serverName = getServerName();" + 
						   		"	javax.servlet.http.HttpSession httpsession = null;" +
								"	String requestedSessionId = getRequestedSessionId();";
				
				for (int j = 0; j < ssoDomainList.size(); j++) {
					if (j > 0) {
						body += "	else ";
					}
					
					body += 	"	if (serverName.endsWith(\"" + ssoDomainList.get(j) + "\")) {" +
								"  		ssoDomain = true;" +
								"	}";
				}
				
				body +=			"	if (ssoDomain) {" +
								"		String newId = getParameter(\"" + ssoParamKey + "\");" +
								"		if (newId != null && newId.length() > 0) {" +
								"			if (requestedSessionId == null || !requestedSessionId.equals(newId)) {" +
								"				httpsession = getContext().getSessionContext().getNewSession(newId, this, getResponse());" +
								"				sessionHelper.setSession(httpsession);" + 
								"				getResponse().setSessionCookie(httpsession);" + 
								"			}" +
								"		}" +
								"	}" + 
								"	if (httpsession == null) {" +
								"		httpsession = sessionHelper.getSession($1);" + 
								"	}" +
								"	checkAndSetDebugSessionFlag(httpsession);" + 
								"	return httpsession;" +
								"}";
				
				CtMethod newMethod = CtNewMethod.copy(methods[i], cl, null);
				methods[i].setName("_" + methods[i].getName());
				newMethod.setBody(body);				
				cl.addMethod(newMethod);
				
				if (verbose) {
					System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
				}
			}
			
			/*
			if (methods[i].getName().equals("getSession")) {
				CtClass[] params = methods[i].getParameterTypes();
				
				if (params == null || params.length < 1) {
					continue;
				}
				
				String body = 	"javax.servlet.http.HttpSession httpsession = null;" +
								"String requestedSessionId = getRequestedSessionId();" + 
								"boolean ssoDomain = false;" +
						   		"String serverName = getServerName();";
				
				for (int j = 0; j < ssoDomainList.size(); j++) {
					if (j > 0) {
						body += "else ";
					}
					
					body += 	"if (serverName.endsWith(\"" + ssoDomainList.get(j) + "\")) {" +
								"  	ssoDomain = true;" +
								"}";
				}
				
				body +=			"if (ssoDomain) {" +
								"	String newId = getParameter(\"ATHENA_DOLLY_SESSION_ID\");" +
								"	System.out.println(\"ATHENA_DOLLY_SESSION_ID => \" + newId);" +
								"	if (newId != null && newId.length() > 0) {" +
								"		if (requestedSessionId == null || !requestedSessionId.equals(newId)) {" +
								"			httpsession = sessionHelper.getNewSession(newId);" +
								"		}" +
								"	}" +
								//"	System.out.println(serverName + \"'s requestedSessionId_after => \" + requestedSessionId);" +
								"}" + 
								"if (httpsession == null) {" +
								"	httpsession = sessionHelper.getSession($1);" + 
								"}" +
								"checkAndSetDebugSessionFlag(httpsession);" + 
								"return httpsession;";
				
				methods[i].insertBefore(body);
			}*/


			/*
			if (methods[i].getName().equals("getRequestedSessionId")) {
				String body = 	"boolean ssoDomain = false;" +
						   		"String serverName = getServerName();";
				
				for (int j = 0; j < ssoDomainList.size(); j++) {
					if (j > 0) {
						body += "else ";
					}
					
					body += 	"if (serverName.endsWith(\"" + ssoDomainList.get(j) + "\")) {" +
								"  	ssoDomain = true;" +
								"}";
				}
				
				body +=			"if (ssoDomain) {" +
								"	String newId = getParameter(\"ATHENA_DOLLY_SESSION_ID\");" +
								"	System.out.println(\"ATHENA_DOLLY_SESSION_ID => \" + newId);" +
								"	if (newId != null && newId.length() > 0) {" +
								"		System.out.println(serverName + \"'s requestedSessionId_before => \" + requestedSessionId);" +
								//"		if (session != null) { session.expire(); }" +
								"		requestedSessionId = newId;" +
								//"		session = doGetSession(true);" +
								"		changeSessionId(newId);" +
								"	}" +
								//"	System.out.println(serverName + \"'s requestedSessionId_after => \" + requestedSessionId);" +
								"}";
				
				methods[i].insertBefore(body);
				
				if (verbose) {
					System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
				}
			} else if (methods[i].getName().equals("changeSessionId")) {
				String body = 	"String serverName = getServerName();" +
						   		"System.out.println(serverName + \"'s requestedSessionId_after => \" + requestedSessionId);";
				
				methods[i].insertAfter(body);
				
				if (verbose) {
					System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
				}
			} 
			*/
			
			/*
			if (methods[i].getName().equals("getRequestedSessionId")) {
				String body = 	"{" +
								"	boolean ssoDomain = false;" +
						   		"	String serverName = getServerName();";
				
				for (int j = 0; j < ssoDomainList.size(); j++) {
					if (j > 0) {
						body += " else ";
					}
					
					body += 	"   if (serverName.endsWith(\"" + ssoDomainList.get(j) + "\")) {" +
								"   	ssoDomain = true;" +
								"   }";
				}
				
				body +=			"   if (ssoDomain) {" +
								"		String remoteAddr = getHeader(\"X-FORWARDED-FOR\");" +
								"		if (remoteAddr == null) {" +
								"			remoteAddr = getRemoteAddr();" +
								"		}" +
								"		requestedSessionId = (java.lang.String)com.athena.dolly.common.cache.DollyManager.getClient().get(remoteAddr);" +
								"	}" +
								"	return requestedSessionId;" +
								"}";

				CtMethod newMethod = CtNewMethod.copy(methods[i], cl, null);
				methods[i].setName("_" + methods[i].getName());
				newMethod.setBody(body);				
				cl.addMethod(newMethod);
				
				if (verbose) {
					System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
				}
			} else if (methods[i].getName().equals("doGetSession")) {
				String body =	"String remoteAddr = getHeader(\"X-FORWARDED-FOR\");" +
								"if (remoteAddr == null) {" +
								"	remoteAddr = getRemoteAddr();" +
								"}" +
								"if (session != null) {" + 
								"	com.athena.dolly.common.cache.DollyManager.getClient().put(remoteAddr, session.getId());" +
								"}";
				
				methods[i].insertAfter(body);
				
				if (verbose) {
					System.out.println(className.replace('/', '.') + "." + methods[i].getName() + "() is successfully enhanced.");
				}
			}
			*/
		}
		
		redefinedClassfileBuffer = cl.toBytecode();
		
		return redefinedClassfileBuffer;
	}//end of instumentRequest()

	/**
	 * <pre>
	 * 주어진 ClassLoader에 해당하는 ClassPool을 반환한다.
	 * </pre>
	 * @param loader
	 * @return
	 */
	private synchronized ClassPool getClassPool(ClassLoader loader) {
		ClassPool pool = pools.get(loader);
		
		if (pool == null) {
			pool = new ClassPool(true);
			pool.appendClassPath(new LoaderClassPath(loader));
			pools.put(loader, pool);
		}
		
		return pool;
	}//end of getClassPool()

    /**
     * <pre>
     * 탐색된 클래스가 프로퍼티에 명시된 target class 인지 확인한다.
     * </pre>
     * @param className
     * @return
     */
    private boolean acceptClass(String className) {
    	/*
        for (String IGNORED_PACKAGE : IGNORED_PACKAGES) {
            if (className.startsWith(IGNORED_PACKAGE)) {
            	System.err.println(className + " : " + IGNORED_PACKAGE);
                return false;
            }
        }
        
        return true;
        */
        
        for (String targetClass : classList) {
        	if (className.equals(targetClass)) {
        		return true;
        	}
        }
        
        return false;
    }//end of acceptClass()
    
    /**
     * <pre>
     * 메소드의 리턴 값을 확인한다.
     * </pre>
     * @param method
     * @return
     * @throws NotFoundException
     */
    private String returnValue(CtBehavior method) throws NotFoundException {
		String returnValue = "";
		if (methodReturnsValue(method)) {
			returnValue = "\" returns: \" + $_ ";
		}
		return returnValue;
	}//end of returnValue()

	/**
	 * <pre>
	 * 메소드의 리턴 값을 확인한다.
	 * </pre>
	 * @param method
	 * @return
	 * @throws NotFoundException
	 */
	private boolean methodReturnsValue(CtBehavior method) throws NotFoundException {
		CtClass returnType = ((CtMethod) method).getReturnType();
		String returnTypeName = returnType.getName();

		boolean isVoidMethod = (method instanceof CtMethod) && "void".equals(returnTypeName);
		boolean isConstructor = method instanceof CtConstructor;

		boolean methodReturnsValue = (isVoidMethod || isConstructor) == false;
		return methodReturnsValue;
	}//end of methodReturnsValue()

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param method
	 * @return
	 * @throws NotFoundException
	 */
	private String getSignature(CtBehavior method) throws NotFoundException {
		CtClass parameterTypes[] = method.getParameterTypes();

		CodeAttribute codeAttribute = method.getMethodInfo().getCodeAttribute();

		LocalVariableAttribute locals = (LocalVariableAttribute) codeAttribute.getAttribute("LocalVariableTable");
		String methodName = method.getName();

		StringBuffer sb = new StringBuffer(methodName + "(\" ");
		for (int i = 0; i < parameterTypes.length; i++) {
			if (i > 0) {
				sb.append(" + \", \" ");
			}

			CtClass parameterType = parameterTypes[i];
			CtClass arrayOf = parameterType.getComponentType();

			sb.append(" + \"");
			sb.append(parameterNameFor(method, locals, i));
			sb.append("\" + \"=");

			// use Arrays.asList() to render array of objects.
			if (arrayOf != null && !arrayOf.isPrimitive()) {
				sb.append("\"+ java.util.Arrays.asList($" + (i + 1) + ")");
			} else {
				sb.append("\"+ $" + (i + 1));
			}
		}
		sb.append("+\")\"");

		String signature = sb.toString();
		return signature;
	}//end of getSignature()

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param method
	 * @param locals
	 * @param i
	 * @return
	 */
	private String parameterNameFor(CtBehavior method, LocalVariableAttribute locals, int i) {
		if (locals == null) {
			return Integer.toString(i + 1);
		}

		if (Modifier.isStatic(method.getModifiers())) {
			return locals.variableName(i);
		}

		// skip #0 which is reference to "this"
		return locals.variableName(i + 1);
	}//end of parameterNameFor()
}
//end of DollyClassTransformer.java