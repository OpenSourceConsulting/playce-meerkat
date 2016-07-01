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
 * BongJin Kwon		2016. 6. 30.		First Draft.
 */
package com.athena.meerkat.common.tomcat;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athena.meerkat.common.CommonTestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration( classes = {CommonTestApplication.class})
public class TomcatVersionsPropertiesTest {

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
	
	@Autowired
	ApplicationContext ctx;
	
	@Autowired
	TomcatVersionsProperties tomcatVerProps;
	
	@Test
	public void testContextLoads() throws Exception {
		assertNotNull(this.ctx);
		assertTrue(this.ctx.containsBean("tomcatVersionsProperties"));
	}

	@Test
	public void testGetTomcatJmxThreadMap() {
		System.out.println(tomcatVerProps.getTomcatThreadAttr(8));
		System.out.println(tomcatVerProps.getDBCPMaxActive(8));
	}

}
//end of TomcatVersionsPropertiesTest.java