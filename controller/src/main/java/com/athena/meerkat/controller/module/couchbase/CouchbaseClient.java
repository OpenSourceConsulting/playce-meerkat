/* 
 * Copyright (C) 2012-2015 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2015. 1. 6.		First Draft.
 */
package com.athena.meerkat.controller.module.couchbase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.athena.meerkat.controller.module.MeerkatClient;
import com.athena.meerkat.controller.module.vo.DesignDocumentVo;
import com.athena.meerkat.controller.module.vo.MemoryVo;
import com.athena.meerkat.controller.module.vo.ViewVo;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;

/**
 * <pre>
 * Spring RestTemplate을 이용하여 Couchbase 상태 정보를 조회하기 위한 Component 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class CouchbaseClient extends MeerkatClient {
	
    private static final Logger logger = LoggerFactory.getLogger(CouchbaseClient.class);
	
	private static final String AUTH_HEADER_KEY = "Authorization";

	private com.couchbase.client.CouchbaseClient client;
	private String url;
	private String name;
	private String passwd;
	private String credential;

	private List<MemoryVo> memoryList;
	private List<String> cpuList;
	private long timestamp = 0L;
	
	public CouchbaseClient(String url, String name, String passwd) {
		this.url = url;
		this.name = name;
		this.passwd = passwd;
		
		init();
	}

	/**
	 * 유효하지 않은 인증서를 가진 호스트로 HTTPS 호출 시 HandShake Exception 및 인증서 관련 Exception이 발생하기 때문에
	 * SSL 인증서 관련 검증 시 예외를 발생하지 않도록 추가됨.
	 */
	public void init() {
		getCredential();
		System.setProperty("jsse.enableSNIExtension", "false");
		
		List<URI> uris = new LinkedList<URI>();
		uris.add(URI.create(url));
		
		try {
			client = new com.couchbase.client.CouchbaseClient(uris, name, passwd);
		} catch (IOException e) {
			logger.error("Can't create an instance of com.couchbase.client.CouchbaseClient. ", e);
		}
	}//end of afterPropertiesSet()
	
	/**
	 * <pre>
	 * HTTP Header에 인증 정보를 포함시킨다.
	 * </pre>
	 * @return
	 */
	private HttpEntity<Object> setHTTPEntity(String body) {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
	    acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
	    
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(acceptableMediaTypes);
		
		if (getCredential() != null) {
			requestHeaders.set(AUTH_HEADER_KEY, getCredential());
		}
		
		if (body != null) {
			logger.debug("Content Body => {}", body);
			return new HttpEntity<Object>(body, requestHeaders);
		} else {
			return new HttpEntity<Object>(requestHeaders);
		}
	}//end of addAuth()
	
	/**
	 * <pre>
	 * Couchbase에 전달하기 위한 인증 정보를 생성한다. 
	 * </pre>
	 * @return
	 */
	public String getCredential() {
		if (credential == null && name != null && passwd != null) {
			try {
				String plain = name + ":" + passwd;
				credential = "Basic " + Base64.encodeBase64String(plain.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error("UnsupportedEncodingException has occurred.", e);
			}
		}
		
		return credential;
	}//end of getCredential()
	
	/**
	 * <pre>
	 * API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param api
	 * @param body
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> submit(String api, String body, HttpMethod method) throws RestClientException, Exception {
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<?> response = rt.exchange(api, method, setHTTPEntity(body), Map.class);
			
			//logger.debug("[Request URL] : {}", api);
			//logger.debug("[Response] : {}", response);
			
			return (Map<String, Object>) response.getBody();
		} catch (RestClientException e) {
			logger.error("RestClientException has occurred.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred.", e);
			throw e;
		}
	}//end of submit()

	@SuppressWarnings("unchecked")
	public List<MemoryVo> getMemoryUsageList() {
		List<MemoryVo> memoryList = new ArrayList<MemoryVo>();
		List<String> cpuList = new ArrayList<String>();
		MemoryVo memory = null;
		
		if (System.currentTimeMillis() - timestamp > 2000) {
			try {
				Map<String, Object> response = submit(url + "/default/buckets/" + name, null, HttpMethod.GET);
				
				List<Map<String, Object>> nodeList = (List<Map<String, Object>>) response.get("nodes");
				
				Map<String, Object> systemStats = null;
				for (int i = 0; i < nodeList.size(); i++) {
					systemStats = (Map<String, Object>) nodeList.get(i).get("systemStats");
					
					Long totalMem = Long.parseLong(systemStats.get("mem_total").toString());
					Long freeMem = Long.parseLong(systemStats.get("mem_free").toString());
					String cpu = systemStats.get("cpu_utilization_rate").toString();
					
					memory = new MemoryVo();
					memory.setCommitted(totalMem);
					memory.setMax(totalMem);
					memory.setUsed(totalMem - freeMem);
					
					memoryList.add(memory);
					cpuList.add(cpu);
				}
				
				this.memoryList = memoryList;
				this.cpuList = cpuList;
				this.timestamp = System.currentTimeMillis();
			} catch (RestClientException e) {
				logger.error("RestClientException has occurred.", e);
			} catch (Exception e) {
				logger.error("Unhandled Exception has occurred.", e);
			}
		} 
		
		return this.memoryList;
	}

	@SuppressWarnings("unchecked")
	public MemoryVo getMemoryUsage() {
		MemoryVo memory = null;
		
		Long totalMem = 0L;
		Long freeMem = 0L;
		
		try {
			Map<String, Object> response = submit(url + "/default/buckets/" + name, null, HttpMethod.GET);
			
			List<Map<String, Object>> nodeList = (List<Map<String, Object>>) response.get("nodes");
			Map<String, Object> systemStats = (Map<String, Object>) nodeList.get(0).get("systemStats");
			
			totalMem = Long.parseLong(systemStats.get("mem_total").toString());
			freeMem = Long.parseLong(systemStats.get("mem_free").toString());
		} catch (RestClientException e) {
			logger.error("RestClientException has occurred.", e);
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred.", e);
		}
		
		memory = new MemoryVo();
		memory.setCommitted(totalMem);
		memory.setMax(totalMem);
		memory.setUsed(totalMem - freeMem);
		
		return memory;
	}

	@SuppressWarnings("unchecked")
	public List<String> getCpuUsageList() {
		List<MemoryVo> memoryList = new ArrayList<MemoryVo>();
		List<String> cpuList = new ArrayList<String>();
		MemoryVo memory = null;
		
		if (System.currentTimeMillis() - timestamp > 2000) {
			try {
				Map<String, Object> response = submit(url + "/default/buckets/" + name, null, HttpMethod.GET);
				
				List<Map<String, Object>> nodeList = (List<Map<String, Object>>) response.get("nodes");
				
				Map<String, Object> systemStats = null;
				for (int i = 0; i < nodeList.size(); i++) {
					systemStats = (Map<String, Object>) nodeList.get(i).get("systemStats");
					
					Long totalMem = Long.parseLong(systemStats.get("mem_total").toString());
					Long freeMem = Long.parseLong(systemStats.get("mem_free").toString());
					String cpu = systemStats.get("cpu_utilization_rate").toString();
					
					memory = new MemoryVo();
					memory.setCommitted(totalMem);
					memory.setMax(totalMem);
					memory.setUsed(totalMem - freeMem);
					
					memoryList.add(memory);
					cpuList.add(cpu);
				}
				
				this.memoryList = memoryList;
				this.cpuList = cpuList;
				this.timestamp = System.currentTimeMillis();
			} catch (RestClientException e) {
				logger.error("RestClientException has occurred.", e);
			} catch (Exception e) {
				logger.error("Unhandled Exception has occurred.", e);
			}
		} 
		
		return this.cpuList;
	}

	@SuppressWarnings("unchecked")
	public String getCpuUsage() {
		String cpu = null;
		try {
			Map<String, Object> response = submit(url + "/default/buckets/" + name, null, HttpMethod.GET);
			
			List<Map<String, Object>> nodeList = (List<Map<String, Object>>) response.get("nodes");
			Map<String, Object> systemStats = (Map<String, Object>) nodeList.get(0).get("systemStats");
			
			cpu = systemStats.get("cpu_utilization_rate").toString();
		} catch (RestClientException e) {
			logger.error("RestClientException has occurred.", e);
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred.", e);
		}
		
		return cpu;
	}

	@SuppressWarnings("unchecked")
	public List<DesignDocumentVo> getDesigndocs() {
		List<DesignDocumentVo> docList = new ArrayList<DesignDocumentVo>();
		DesignDocumentVo ddoc = null;
		
		try {
			Map<String, Object> response = submit(url + "/default/buckets/" + name + "/ddocs", null, HttpMethod.GET);
			
			List<Map<String, Map<String, Map<String, Object>>>> rows = (List<Map<String, Map<String, Map<String, Object>>>>) response.get("rows");
			
			Map<String, Map<String, Map<String, Object>>> row = null;
			Map<String, Map<String, String>> viewMap = null;
			List<String> viewNames = null;
			ViewVo view = null;
			String docName = null;
			for (int i = 0; i < rows.size(); i++) {
				row = rows.get(i);
				
				docName = ((String)row.get("doc").get("meta").get("id"));
				if (docName.indexOf("dev_") < 0) {
					ddoc = new DesignDocumentVo();
					ddoc.setDesignDocumentName(docName.replaceAll("_design/", ""));
					
					viewMap = (Map<String, Map<String, String>>)row.get("doc").get("json").get("views");
					
					viewNames = new ArrayList<String>(viewMap.keySet());
					for (String viewName : viewNames) {
						view = new ViewVo();
						view.setViewName(viewName);
						view.setMap(viewMap.get(viewName).get("map"));
						view.setReduce(viewMap.get(viewName).get("reduce"));
						ddoc.getViewList().add(view);
					}
					
					docList.add(ddoc);
				}
			}
		} catch (RestClientException e) {
			logger.error("RestClientException has occurred.", e);
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred.", e);
		}
		
		return docList;
	}

	public String createView(String docName, String viewName) {
		String result = "false";
		
		try {
			DesignDocument ddoc = getDesignDoc(docName);
			
			List<ViewDesign> viewList = ddoc.getViews();
	
			for (ViewDesign view : viewList) {
				if (view.getName().equals(viewName)) {
					return "already exist";
				}
			}
			
			ViewDesign design = new ViewDesign(viewName, "function (doc, meta) {\n" +
	                "    emit(meta.id, null);\n" +
	                "}");
			
			ddoc.getViews().add(design);
	        client.createDesignDoc(ddoc);
	        
	        result = "true";
		} catch (Exception e) {
			logger.error("Unhabdled exception has occurred while create view.", e);
		}
		
		return result;
	}
	
	public Boolean updateView(DesignDocumentVo designDoc) {
		boolean result = false;
		boolean isExist = false;
		
		try {
			DesignDocument ddoc = getDesignDoc(designDoc.getDesignDocumentName());
			ViewVo view = designDoc.getViewList().get(0);
			
			List<ViewDesign> viewList = ddoc.getViews();
	
			for (ViewDesign design : viewList) {
				if (design.getName().equals(view.getViewName())) {
					isExist = true;
					viewList.remove(design);
					break;
				}
			}
			
			if (isExist) {
				ViewDesign viewDesign = null;
				
				if (view.getReduce() == null || view.getReduce().equals("")) {
					viewDesign = new ViewDesign(view.getViewName(), view.getMap());
				} else {
					viewDesign = new ViewDesign(view.getViewName(), view.getMap(), view.getReduce());
				}
				
				ddoc.getViews().add(viewDesign);
		        client.createDesignDoc(ddoc);
		        
		        result = true;
			}
		} catch (Exception e) {
			logger.error("Unhabdled exception has occurred while create view.", e);
		}
		
		return result;
	}

	public Boolean deleteDesignDoc(String docName) {
		return client.deleteDesignDoc(docName);
	}

	public Boolean deleteView(String docName, String viewName) {
		boolean result = false;
		boolean isExist = false;
		
		try {
			DesignDocument ddoc = client.getDesignDoc(docName);
			
			List<ViewDesign> viewList = ddoc.getViews();

			for (ViewDesign view : viewList) {
				if (view.getName().equals(viewName)) {
					isExist = true;
					viewList.remove(view);
					break;
				}
			}
			
			if (isExist) {
				if (viewList.size() == 0) {
					deleteDesignDoc(docName);
				} else {
					ddoc.setViews(viewList);
					client.createDesignDoc(ddoc);
				}
				result = true;
			}
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			// design document does not exist.
			// ignore
		}
		
		return result;
	}
	
	public void shutdown() {
		client.shutdown(5, TimeUnit.SECONDS);
	}
	
	private DesignDocument getDesignDoc(String docName) {
		try {
			return client.getDesignDoc(docName);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(docName);
		}
	}

	public static void main(String[] args) {
		CouchbaseClient client = new CouchbaseClient("http://127.0.0.1:8091/pools", "gamesim-sample", "");
		
		try {
			client.init();
			System.out.println(client.getMemoryUsage());
			System.out.println(client.getCpuUsage());
			//System.err.println(client.getDesigndocs());
			//System.err.println(client.deleteDesignDoc("test"));
			//System.err.println(client.deleteView("test", "test"));
			//System.err.println(client.createView("test", "test"));
		} catch (RestClientException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			client.shutdown();
		}
	}
}
//end of CouchbaseClient.java