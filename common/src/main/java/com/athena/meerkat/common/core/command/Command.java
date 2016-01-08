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
package com.athena.meerkat.common.core.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.meerkat.common.core.action.Action;
import com.athena.meerkat.common.core.action.ShellAction;
import com.athena.meerkat.common.netty.message.ProvisioningResponseMessage;

/**
 * <pre>
 * Set of actions
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(Command.class);

	private final String name;
	private List<Action> actions;

	public Command(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the actions
	 */
	public List<Action> getActions() {
		if (actions == null) {
			actions = new ArrayList<Action>();
		}

		return actions;
	}

	public void addAction(Action action) {
		getActions().add(action);
	}

	public void execute(ProvisioningResponseMessage response) {
		String result = null;
		for (Action action : actions) {
			LOGGER.debug("[{}] will be start.", action.getClass()
					.getCanonicalName());

			result = action.perform();

			if (action instanceof ShellAction) {
				response.addCommand(result.substring(0, result.indexOf("\n")));
				response.addResult(result.substring(result.indexOf("\n") + 1));
			} else {
				if (result != null && !result.equals("")) {
					response.addCommand("\n");
					response.addResult(result);
				}
			}

			LOGGER.debug("[{}] has done.", action.getClass().getCanonicalName());
		}
	}
}
// end of Command.java