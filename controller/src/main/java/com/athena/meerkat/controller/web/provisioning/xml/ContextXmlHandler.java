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
 * BongJin Kwon		2016. 4. 21.		First Draft.
 */
package com.athena.meerkat.controller.web.provisioning.xml;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.athena.meerkat.controller.web.entities.DataSource;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class ContextXmlHandler {
	
	private static final String TAG_NAME_RES = "Resource";
	private static final String DS_TYPE = "javax.sql.DataSource";

	private String filepath;
	
	public ContextXmlHandler(String filepath) {
		this.filepath = filepath;
	}
	
	public void updateDatasource(List<DataSource> dsList, File jobDir) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
	
			// Get the root element
			Node context = doc.getFirstChild();
			
			NodeList list = doc.getElementsByTagName(TAG_NAME_RES);
			for (int i = 0; i < list.getLength(); i++) {
				
                Node node = list.item(i);
                
                if (DS_TYPE.equals(XMLUtil.getAttribute(node, "type"))) {
                	context.removeChild(node);
				}
			}
			
			for (DataSource ds : dsList) {
				Element res = doc.createElement(TAG_NAME_RES);
				res.setAttribute("name", 			ds.getName());
				res.setAttribute("auth", 			"Container");
				res.setAttribute("type", 			DS_TYPE);
				res.setAttribute("maxActive", 		String.valueOf(ds.getMaxConnection()));
				res.setAttribute("maxIdle", 		String.valueOf(ds.getMaxConnectionPool()));
				res.setAttribute("minIdle", 		String.valueOf(ds.getMinConnectionPool()));
				res.setAttribute("maxWait", 		String.valueOf(ds.getTimeout()));
				res.setAttribute("username", 		ds.getUserName());
				res.setAttribute("password", 		ds.getPassword());
				res.setAttribute("driverClassName", "com.mysql.jdbc.Driver");
				res.setAttribute("url", 			ds.getJdbcUrl());
				res.setAttribute("testOnBorrow", 	"true");
				res.setAttribute("validationQuery", "SELECT 1");
				
				context.appendChild(res);
			}
			
			XMLUtil.writeToFile(doc, new File(jobDir.getAbsoluteFile() + File.separator + "context.xml"));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
//end of ContextXmlHandler.java