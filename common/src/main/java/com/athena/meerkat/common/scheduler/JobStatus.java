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
 * 현재 스케줄링 잡의 상태를 나타내는 클래스
 * code 값
 * - RUNNING
 * - COMPLETED
 * - FAILED
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class JobStatus {

    public static final String RUNNING = "RUNNING";
    public static final String COMPLETED = "COMPLETED";
    public static final String FAILED = "FAILED";

    private String currentStatus;

    private JobStatus() {
        this.currentStatus = RUNNING;
    }

    public static JobStatus start() {
        return new JobStatus();
    }

    public String getStatusCode() {
        return this.currentStatus;
    }

    public void complete() {
        this.currentStatus = COMPLETED;
    }

    public void fail() {
        this.currentStatus = FAILED;
    }

    public boolean isCompleted() {
        return COMPLETED.equals(currentStatus);
    }

    public boolean isFailed() {
        return FAILED.equals(currentStatus);
    }
}//end of JobStatus.java