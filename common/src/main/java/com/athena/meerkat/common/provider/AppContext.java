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
 * Sang-cheon Park	2013. 8. 25.		First Draft.
 */
package com.athena.meerkat.common.provider;

import org.springframework.context.ApplicationContext;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class AppContext {
    
    private static ApplicationContext ctx;  
    
    public static void setApplicationContext(ApplicationContext applicationContext) {  
        ctx = applicationContext;  
    }  
  
    public static ApplicationContext getApplicationContext() {  
        return ctx;  
    }

	public static Object getBean(String value) {
		return ctx.getBean(value);
	}  

	public static <T> T getBean(Class<T> clazz) {
		return clazz.cast(ctx.getBean(clazz));
	}  

	public static <T> T getBean(String value, Class<T> clazz) {
		return clazz.cast(ctx.getBean(value));
	}  
}//end of AppContext.java