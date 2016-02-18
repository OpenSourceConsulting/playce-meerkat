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
 * BongJin Kwon		2016. 2. 15.		First Draft.
 */
package com.athena.meerkat.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class ConfigTest {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		RoleHierarchyImpl r = new RoleHierarchyImpl();
		r.setHierarchy("ROLE_TOMCAT_ADMIN > ROLE_TOMCAT_USER | ROLE_MONITOR_ADMIN > ROLE_MONITOR_DB | ROLE_RES_ADMIN > ROLE_RES_USER | ROLE_USER_ADMIN > ROLE_USER_USER " + 
		  		 "ROLE_ADMIN > ROLE_TOMCAT_ADMIN | ROLE_ADMIN > ROLE_MONITOR_ADMIN | ROLE_ADMIN > ROLE_RES_ADMIN | ROLE_ADMIN > ROLE_USER_ADMIN");
		  
		System.out.println(r.toString());
	}

}
//end of ServiceResultTest.java