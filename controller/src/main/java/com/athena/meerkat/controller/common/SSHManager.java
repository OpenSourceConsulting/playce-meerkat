/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Bong-Jin Kwon	2015. 1. 19.		First Draft.
 */
package com.athena.meerkat.controller.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * <pre>
 * ssh command manager.
 * </pre>
 * @author Bong-Jin Kwon
 * @version 2.0
 */
public class SSHManager {
	//private static final Logger LOGGER = Logger.getLogger(SSHManager.class.getName());
	static final Logger logger = LoggerFactory.getLogger(SSHManager.class);
	
	private JSch jsch;
	private String strUserName;
	private String strConnectionIP;
	private int intConnectionPort;
	private String strPassword;
	private Session session;
	private int intTimeOut;

	private void doCommonConstructorActions(String userName, String password, String connectionIP, String knownHostsFileName) {
		jsch = new JSch();

		try {
			if(knownHostsFileName != null && knownHostsFileName.length() > 0){
				jsch.setKnownHosts(knownHostsFileName);
			}
		} catch (JSchException jschX) {
			logError(jschX.getMessage());
		}

		strUserName = userName;
		strPassword = password;
		strConnectionIP = connectionIP;
	}
	
	public SSHManager(String userName, String password, String connectionIP) {
		this(userName, password, connectionIP, "");
	}

	public SSHManager(String userName, String password, String connectionIP, String knownHostsFileName) {
		doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
		intConnectionPort = 22;
		intTimeOut = 60000;
	}

	public SSHManager(String userName, String password, String connectionIP, String knownHostsFileName, int connectionPort) {
		doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
		intConnectionPort = connectionPort;
		intTimeOut = 60000;
	}

	public SSHManager(String userName, String password, String connectionIP, String knownHostsFileName, int connectionPort, int timeOutMilliseconds) {
		doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
		intConnectionPort = connectionPort;
		intTimeOut = timeOutMilliseconds;
	}

	public String connect() {
		String errorMessage = null;

		try {
			session = jsch.getSession(strUserName, strConnectionIP, intConnectionPort);
			session.setPassword(strPassword);
			
			// UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(intTimeOut);
		} catch (JSchException jschX) {
			errorMessage = jschX.getMessage();
		}

		return errorMessage;
	}

	private String logError(String errorMessage) {
		if (errorMessage != null) {
			logger.error("{}:{} - {}", new Object[] { strConnectionIP, intConnectionPort, errorMessage });
		}

		return errorMessage;
	}

	private String logWarning(String warnMessage) {
		if (warnMessage != null) {
			logger.warn("{}:{} - {}", new Object[] { strConnectionIP, intConnectionPort, warnMessage });
		}

		return warnMessage;
	}

	public String sendCommand(String command) {
		StringBuilder outputBuffer = new StringBuilder();

		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.connect();
			InputStream commandOutput = channel.getInputStream();
			int readByte = commandOutput.read();

			while (readByte != 0xffffffff) {
				outputBuffer.append((char) readByte);
				readByte = commandOutput.read();
			}

			channel.disconnect();
		} catch (IOException ioX) {
			logWarning(ioX.getMessage());
			return null;
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
			return null;
		}

		return outputBuffer.toString();
	}
	
	/**
	 * 
	 * @param remoteFile
	 */
	public byte[] scpDown(String remoteFile){
		
	    byte[] downByte = null;
		// exec 'scp -f remoteFile' remotely
		
		try{
			String command = "scp -f " + remoteFile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ')
						break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				// System.out.println("filesize="+filesize+", file="+file);

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				
				// read a content of lfile
				ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
				//FileOutputStream fos = new FileOutputStream(prefix == null ? lfile : prefix + file);
				int foo;
				while (true) {
					if (buf.length < filesize)
						foo = buf.length;
					else
						foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					bos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L)
						break;
				}
				downByte = bos.toByteArray();
				bos.close();
				bos = null;

				if (checkAck(in) == 0) {
					// send '\0'
					buf[0] = 0;
					out.write(buf, 0, 1);
					out.flush();
				}
				
			}
			
			return downByte;
			
		} catch (IOException ioX) {
			logWarning(ioX.getMessage());
			return null;
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
			return null;
		}
		
		
	}
	
	/**
	 * 
	 * @param remoteFile
	 */
	public void scpDown(String remoteFile, String lfile){
		
		String prefix=null;
	    if(new File(lfile).isDirectory()){
	        prefix=lfile+File.separator;
	    }
	      
		// exec 'scp -f remoteFile' remotely
		
		try{
			String command = "scp -f " + remoteFile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ')
						break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				// System.out.println("filesize="+filesize+", file="+file);

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				
				// read a content of lfile
				FileOutputStream fos = new FileOutputStream(prefix == null ? lfile : prefix + file);
				int foo;
				while (true) {
					if (buf.length < filesize)
						foo = buf.length;
					else
						foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L)
						break;
				}
				fos.close();
				fos = null;

				if (checkAck(in) == 0) {
					// send '\0'
					buf[0] = 0;
					out.write(buf, 0, 1);
					out.flush();
				}
				
			}
			
			
		} catch (IOException ioX) {
			logWarning(ioX.getMessage());
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
		}
		
		
	}

	public void close() {
		session.disconnect();
	}
	
	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			
			if (b == 1) { // error
				//System.out.print(sb.toString());
				logger.error(sb.toString());
			}
			if (b == 2) { // fatal error
				//System.out.print(sb.toString());
				logger.error(sb.toString());
			}
			
		}
		return b;
	}
	/*
	public static void main(String[] args) {
		SSHManager sshMng = new SSHManager("alm", "opensource", "192.168.0.242");
		
		String errorMsg = sshMng.connect();
		
		if(errorMsg != null){
			System.out.println(errorMsg);
		}else{
			String logs = sshMng.sendCommand("ls -al");
			
			System.out.println(logs);
			
			sshMng.close();
		}
		
	}
*/
}