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
 * Sang-cheon Park	2013. 10. 10.		First Draft.
 */
package com.athena.meerkat.common.core.action.support;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class Property implements Serializable {
    /**
     * Serializable
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 프로퍼티 키
     */
    private String key;
    
    /**
     * 프로퍼티 값
     */
    private String value;
        
    
    public Property() {
        super();
    }

    public Property(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * 프로퍼티 키 반환
     * @return
     */
    public String getKey() {
        return key;
    }
    
    /**
     * 프로퍼티 키 설정
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * 프로퍼티 값 반환
     * @return
     */
    public String getValue() {
        return value;
    }
    
    /**
     * 프로퍼티 값 설정
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * 문자열 반환
     */
    @Override
    public String toString() {
        return "property [key=" + key + ", value=" + value + "]";
    }

}
//end of Property.java