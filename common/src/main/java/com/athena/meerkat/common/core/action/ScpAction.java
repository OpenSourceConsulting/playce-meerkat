/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
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
 * Sang-cheon Park	2013. 10. 11.		First Draft.
 */
package com.athena.meerkat.common.core.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.common.core.action.support.TargetHost;
import com.athena.meerkat.common.core.util.ScpUtil;

/**
 * <pre>
 * 지정된 호스트로 파일 또는 디렉토리를 전송하기 위한 액션 클래스
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ScpAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScpAction.class);
	
	private static final long serialVersionUID = 1L;
	
	private TargetHost targetHost;
    private String source;
    private String target;

	public ScpAction(int sequence) {
		super(sequence);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the targetHost
	 */
	public TargetHost getTargetHost() {
		return targetHost;
	}

	/**
	 * @param targetHost the targetHost to set
	 */
	public void setTargetHost(TargetHost targetHost) {
		this.targetHost = targetHost;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/* (non-Javadoc)
	 * @see com.athena.meerkat.common.core.action.Action#perform()
	 */
	@Override
	public String perform() {
    	LOGGER.debug("\n- Target Host Info : [{}]\n- Source : [{}]\n- Target : [{}]", new Object[]{targetHost.toString(), source, target});
    	
		String result = "F";
    	
        try {
			ScpUtil.upload(targetHost, source, target);
			result = "S";
		} catch (Exception e) {
			LOGGER.error("Unhandled exception has occurred.", e);
		}
        
        return result;
    }//end of perform()

}
//end of ScpAction.java