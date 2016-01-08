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
 * Sang-cheon Park	2013. 9. 2.		First Draft.
 */
package com.athena.meerkat.common.core.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.common.constant.MeerkatConstant;
import com.athena.meerkat.common.core.action.support.AgentConfigUtil;

/**
 * <pre>
 * Action for Shell Command
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ShellAction extends Action {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShellAction.class);

	private static final long serialVersionUID = 1L;

	private String workingDiretory;
	private String command;
	private List<String> arguments;

	public ShellAction(int sequence) {
		super(sequence);
	}

	/**
	 * @return the workingDiretory
	 */
	public String getWorkingDiretory() {
		return workingDiretory;
	}

	/**
	 * @param workingDiretory
	 *            the workingDiretory to set
	 */
	public void setWorkingDiretory(String workingDiretory) {
		this.workingDiretory = workingDiretory;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the arguments
	 */
	public List<String> getArguments() {
		if (arguments == null) {
			arguments = new ArrayList<String>();
		}

		return arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @param argument
	 *            the argument to add
	 */
	public void addArguments(String argument) {
		getArguments().add(argument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.athena.meerkat.common.core.action.Action#perform()
	 */
	@Override
	public String perform() {
		StringStreamConsumer consumer = null;
		String commandStr = null;

		try {
			Commandline commandLine = new Commandline();

			if (!StringUtils.isEmpty(workingDiretory)) {
				commandLine.setWorkingDirectory(workingDiretory);
			}

			commandLine.setExecutable(command);

			if (getArguments().size() > 0) {
				for (String argument : arguments) {
					if (argument.indexOf("RepositoryUrl") > -1) {
						commandLine
								.createArg()
								.setLine(
										argument.replaceAll(
												"\\$\\{RepositoryUrl\\}",
												AgentConfigUtil
														.getConfig(MeerkatConstant.REPOSITORY_URL)));
					} else {
						commandLine.createArg().setLine(argument);
					}
				}
			}

			/** verify command string */
			commandStr = commandLine.toString();
			LOGGER.debug("{} ~]$ {}\n", workingDiretory, commandStr);

			/**
			 * also enable StringWriter, PrintWriter, WriterStreamConsumer and
			 * etc.
			 */
			consumer = new CommandLineUtils.StringStreamConsumer();

			int returnCode = CommandLineUtils.executeCommandLine(commandLine,
					consumer, consumer, 20 * 60);

			if (returnCode == 0) {
				// success
				LOGGER.debug(consumer.getOutput());
			} else {
				// fail
				LOGGER.error(consumer.getOutput());
			}
		} catch (CommandLineException e) {
			LOGGER.error("CommandLineException has occurred. : ", e);
		}

		if (consumer.getOutput() != null && consumer.getOutput().length() > 0) {
			return commandStr
					+ "\n"
					+ consumer.getOutput().substring(0,
							consumer.getOutput().length() - 1);
		} else {
			return commandStr + "\n" + consumer.getOutput();
		}
	}

}
// end of ShellAction.java