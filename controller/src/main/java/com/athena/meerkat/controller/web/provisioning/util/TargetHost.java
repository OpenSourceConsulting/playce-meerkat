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
 * BongJin Kwon		2016. 7. 5.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.util;

import java.io.Serializable;
import java.util.List;

import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.SshAccount;

/**
 * <pre>
 * 프로비저닝을 실행할 서버의 정보를 담는 POJO 객체
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class TargetHost implements Serializable {
	
	private static final long serialVersionUID = 3033486349988152260L;

	/** Callback 받을 URL */
    private String callback;

    /** 프로비저닝 대상 host */
    private String host;

    /** 프로비저닝 대상의 SSH Port 번호 */
    private Integer port;

    /** 프로비저닝 대상 Host의 Shell 계정 */
    private String username;

    /** 프로비저닝 대상 Host의 Shell 패스워드 */
    private String password;
    
    /** ssh key file */
    private String keyfile;
    
    /** is trust Y/N (default : true) */
    private boolean trust = true;

    public TargetHost(String host, Integer port, String username, String password) {

		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
    
    public TargetHost(Server server){
    	this.host = server.getSshIPAddr();
    	this.port = server.getSshPort();
    	
    	List<SshAccount> accounts = (List<SshAccount>) server.getSshAccounts();
    	
    	this.username = accounts.get(0).getUsername();
    	this.password = accounts.get(0).getPassword();
    }

	/**
	 * @return the callback
	 */
	public String getCallback() {
		return callback;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the keyfile
	 */
	public String getKeyfile() {
		return keyfile;
	}

	/**
	 * @param keyfile the keyfile to set
	 */
	public void setKeyfile(String keyfile) {
		this.keyfile = keyfile;
	}

	/**
	 * @return the trust
	 */
	public boolean isTrust() {
		return trust;
	}

	/**
	 * @param trust the trust to set
	 */
	public void setTrust(boolean trust) {
		this.trust = trust;
	}

	@Override
    public String toString() {
        return "[callback=" + callback 
        		+ ", host=" + host
                + ", port=" + port 
                + ", username=" + username 
                + ", password=" + password 
                + ", keyfile=" + keyfile
                + ", trust=" + trust + "]";
    }
}
//end of TargetHost.java