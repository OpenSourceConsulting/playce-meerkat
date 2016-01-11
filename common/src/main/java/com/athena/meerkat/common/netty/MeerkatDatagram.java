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
package com.athena.meerkat.common.netty;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.athena.meerkat.common.netty.message.AbstractMessage;
import com.athena.meerkat.common.netty.message.MessageType;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MeerkatDatagram<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T message;

	public MeerkatDatagram(T message) {
		if (message instanceof AbstractMessage) {
			this.message = message;
		} else {
			throw new IllegalArgumentException(message.getClass().getName()
					+ " does not supported.");
		}
	}

	/**
	 * @return the messageType
	 * @throws Exception
	 */
	public MessageType getMessageType() {
		return message != null ? ((AbstractMessage) message).getMessageType()
				: null;
	}

	/**
	 * @return the message
	 */
	public T getMessage() {
		return message;
	}

	/**
	 * @return the message
	 */
	public T getMessage(Class<T> clazz) {
		return clazz.cast(message);
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(T message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
// end of Message.java