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
 * Sang-cheon Park	2013. 7. 18.		First Draft.
 */
package com.athena.meerkat.common.netty.message;

import java.util.ArrayList;
import java.util.List;

import com.athena.meerkat.common.core.command.Command;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ProvisioningCommandMessage extends AbstractMessage {

	private static final long serialVersionUID = 1L;

	private List<Command> commands;

	/**
	 * @return the commands
	 */
	public List<Command> getCommands() {
		if (commands == null) {
			commands = new ArrayList<Command>();
		}

		return commands;
	}

	/**
	 * <pre>
	 * Adds command object to command list
	 * </pre>
	 * 
	 * @param command
	 */
	public void addCommand(Command command) {
		getCommands().add(command);
	}

	/**
	 * <pre>
	 * Executes command
	 * </pre>
	 */
	public void executeCommands(ProvisioningResponseMessage response) {
		/*
		 * Command 수행에 필요한 단위 Action을 모두 포함시킬 수도 있지만, install, uninstall,
		 * configuration 등의 작업을 별도의 Command로 구성할 필요가 있을 경우 사용될 수 있는 실행환경을 제공한다.
		 */
		for (Command command : commands) {
			command.execute(response);
		}
	}

	public ProvisioningCommandMessage() {
		super(MessageType.COMMAND);
	}
}
// end of ProvisioningCommand.java