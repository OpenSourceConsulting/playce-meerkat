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
 * BongJin Kwon		2016. 4. 12.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.log;

import java.io.File;
import java.util.concurrent.Executor;

import org.apache.commons.io.input.Tailer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogTailerListenerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHandleString() {
		LogTailerListener listener = new LogTailerListener(new WebSocketSessionMock());
		long delay = 2000;
		File file = new File("G:\\project\\AthenaMeerkat\\meerkat.log");
		Tailer tailer = new Tailer(file, listener, delay);

	    // stupid executor impl. for demo purposes
		
	    Executor executor = new Executor() {
	          public void execute(Runnable command) {
	              command.run();
	          }
	    };

	    executor.execute(tailer);
	    
	    /*
		Thread thread = new Thread(tailer);
		thread.setDaemon(true);
		thread.start();
		
		try{
			Thread.sleep(10000);
		}catch(Exception e){}
		*/
	}

}
//end of LogTailerListenerTest.java