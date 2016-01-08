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

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.common.core.action.support.TargetHost;
import com.athena.meerkat.common.core.util.SshExecUtil;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class SshAction extends Action {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SshAction.class);

	private static final long serialVersionUID = 1L;

	private TargetHost targetHost;
	private List<String> commandList;

	public SshAction(int sequence) {
		super(sequence);
	}

	/**
	 * @return the targetHost
	 */
	public TargetHost getTargetHost() {
		return targetHost;
	}

	/**
	 * @param targetHost
	 *            the targetHost to set
	 */
	public void setTargetHost(TargetHost targetHost) {
		this.targetHost = targetHost;
	}

	/**
	 * @return the commandList
	 */
	public List<String> getCommandList() {
		return commandList;
	}

	/**
	 * @param commandList
	 *            the commandList to set
	 */
	public void setCommandList(List<String> commandList) {
		this.commandList = commandList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.athena.meerkat.common.core.action.Action#perform()
	 */
	@Override
	public String perform() {
		LOGGER.debug("\n- Target Host Info : [{}]", targetHost.toString());

		String result = "F";

		try {
			SshExecUtil.executeCommand(targetHost, commandList);
			result = "S";

			LOGGER.debug("Execute Command(s) Result : \n{}",
					IOUtils.toString(SshExecUtil.output.toURI()));
		} catch (IOException e) {
			LOGGER.error("IOException has occurred.", e);
		}

		return result;
	}// end of perform()

}
// end of SshAction.java