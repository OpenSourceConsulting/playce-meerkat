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
 * Sang-cheon Park	2013. 8. 1.		First Draft.
 */
package com.athena.meerkat.common.scheduler;

/**
 * <pre>
 * Batch Job 실행시 발생하는 예외를 Wrapping하기 위한 Exception 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class InternalJobExecutionException extends RuntimeException {

    private static final long serialVersionUID = -8566230065054696121L;

    public InternalJobExecutionException(Throwable cause) {
		super(cause);
	}

	public InternalJobExecutionException(String msg) {
		super(msg);
	}

	public InternalJobExecutionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}//end of InternalJobExecutionException.java