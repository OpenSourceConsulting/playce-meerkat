/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 12. 30.		First Draft.
 */
package com.athena.dolly.test.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.couchbase.client.CouchbaseClient;
import com.google.gson.Gson;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class CouchbaseTest {
	
	public static final Gson gson = new Gson();

	/**
	 * @param args
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ArrayList<URI> nodes = new ArrayList<URI>();

		// Add one or more nodes of your cluster (exchange the IP with yours)
		nodes.add(URI.create("http://127.0.0.1:8091/pools"));

		// Try to connect to the client
		CouchbaseClient client = null;
		try {
			client = new CouchbaseClient(nodes, "dolly", "dolly");
		} catch (Exception e) {
			System.err.println("Error connecting to Couchbase: " + e.getMessage());
			System.exit(1);
		}

		// Set your first document with a key of "hello" and a value of
		//client.set("hello", "couchbase!").get();
		SampleDto1 sample = null;
		for (int i = 1; i < 11; i++) {
			sample = new SampleDto1();
			sample.setAddress1("Address1_" + i);
			sample.setAddress2("Address2_" + i);
			sample.setAge(25 + (i % 20));
			sample.setEmail("support_" + i + "@osci.kr");
			sample.setFirstName("Firstname-" + i);
			sample.setLastName("Lastname-" + i);
			sample.setUserId("user_" + i);
			client.set(UUID.randomUUID().toString(), 30 * 60, gson.toJson(sample)).get();
			
			
		}

		// Return the result and cast it to string
		//String result = (String) client.get("hello");
		//System.out.println(result);
		
		// Shutdown the client
		client.shutdown();
	}

}
//end of CouchbaseTest.java