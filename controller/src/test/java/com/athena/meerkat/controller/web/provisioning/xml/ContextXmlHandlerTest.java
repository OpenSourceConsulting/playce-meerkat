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
 * BongJin Kwon		2016. 4. 22.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.xml;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import com.athena.meerkat.controller.web.entities.DataSource;

public class ContextXmlHandlerTest {

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
	public void testUpdateDatasource() {
		
		DefaultResourceLoader resLoader = new DefaultResourceLoader();
		
		try{
			String file = ContextXmlHandlerTest.class.getResource("context.xml").getFile();
			System.out.println(file);
			ContextXmlHandler xmlHandler = new ContextXmlHandler(file);
			
			List<DataSource> dsList = new ArrayList<DataSource>();
			DataSource ds = new DataSource();
			ds.setName("ddddd");
			dsList.add(ds);
			
			xmlHandler.updateDatasource(dsList);
		
		}catch(Exception e) {
			fail(e.toString());
			
		}
	}

}
//end of ContextXmlHandlerTest.java