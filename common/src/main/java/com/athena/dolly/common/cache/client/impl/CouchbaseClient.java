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
 * Sang-cheon Park	2014. 12. 23.		First Draft.
 */
package com.athena.dolly.common.cache.client.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.infinispan.client.hotrod.exceptions.TransportException;

import com.athena.dolly.common.cache.DollyConfig;
import com.athena.dolly.common.cache.DollyManager;
import com.athena.dolly.common.cache.SessionKey;
import com.athena.dolly.common.cache.client.DollyClient;
import com.athena.dolly.common.exception.ConfigurationException;
import com.athena.dolly.common.stats.DollyStats;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.google.gson.Gson;

/**
 * <pre>
 * Couchbase용 Dolly Client
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class CouchbaseClient implements DollyClient {

	private DollyConfig config;
	private com.couchbase.client.CouchbaseClient client;
	
	final Gson gson = new Gson();
	
	private int node = 0;
	
	/**
	 * <pre>
	 * 주어진 프로퍼티를 이용하여 CouchbaseClient 객체를 생성한다. 
	 * </pre>
	 */
	public CouchbaseClient() {
		if (DollyConfig.properties == null || config == null) {
			try {
	            config = new DollyConfig().load();
			} catch (ConfigurationException e) {
	            System.err.println("[Dolly] Configuration error : " + e.getMessage());
	            e.printStackTrace();
			}
		}
		
		String[] uris = config.getCouchbaseUris().split(";");
		List<URI> uriList = new LinkedList<URI>();
		for (String uri : uris) {
			uriList.add(URI.create(uri));
		}
		
		try {
			client = new com.couchbase.client.CouchbaseClient(uriList, config.getCouchbaseBucketName(), config.getCouchbaseBucketPasswd());
		} catch (IOException e) {
			System.err.println("Error connecting to Couchbase: " + e.getMessage());
		}
	}//end of Default Contructor()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#get(java.lang.String)
	 */
	public Object get(String cacheKey) {
		Object obj = null;

		if (!DollyManager.isSkipConnection()) {
			try {
				String document = (String) client.get(cacheKey);
				
				if (document != null) {
					obj = gson.fromJson(document, Object.class);
				}
				
				client.touch(cacheKey, config.getTimeout() * 60);
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
		
		return obj;
	}//end of get()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#get(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Object get(String cacheKey, String dataKey) {
		if (!DollyManager.isSkipConnection()) {
			try {
		    	Map<String, Object> attribute = (Map<String, Object>)get(cacheKey);
				
				if (attribute == null) {
					return null;
				} else {
					return attribute.get(dataKey);
				}
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}//end of get()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#put(java.lang.String, java.lang.Object)
	 */
	public synchronized void put(String cacheKey, Object value) throws Exception {
		if (!DollyManager.isSkipConnection()) {
			try {
				client.set(cacheKey, config.getTimeout() * 60, gson.toJson(value)).get();
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
	}//end of put()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#put(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public synchronized void put(String cacheKey, String dataKey, Object value) throws Exception {
		if (!DollyManager.isSkipConnection()) {
			try {
				if (dataKey != null) {
			    	Map<String, Object> attribute = (Map<String, Object>)get(cacheKey);
					
					if (attribute == null) {
						attribute = new ConcurrentHashMap<String, Object>();
					}
					
					attribute.put(dataKey, value);
					client.set(cacheKey, config.getTimeout() * 60, gson.toJson(attribute)).get();
				}
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
	}//end of put()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#remove(java.lang.String)
	 */
	public synchronized void remove(String cacheKey) throws Exception {
		if (!DollyManager.isSkipConnection()) {
			try {
				client.delete(cacheKey).get();
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
	}//end of remove()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#remove(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public synchronized void remove(String cacheKey, String dataKey) throws Exception {
		if (!DollyManager.isSkipConnection()) {
			try {
				if (dataKey != null) {
			    	Map<String, Object> attribute = (Map<String, Object>)get(cacheKey);
					
					if (attribute == null) {
						attribute = new ConcurrentHashMap<String, Object>();
					}
					
					attribute.remove(dataKey);
					client.set(cacheKey, config.getTimeout() * 60, gson.toJson(attribute)).get();
				}
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
	}//end of remove()
    
    /* (non-Javadoc)
     * @see com.athena.dolly.enhancer.client.DollyClient#getValueNames(java.lang.String)
     */
    @SuppressWarnings("unchecked")
	public Enumeration<String> getValueNames(String cacheKey) {
		if (!DollyManager.isSkipConnection()) {
			try {
		    	Map<String, Object> attribute = (Map<String, Object>)get(cacheKey);
				
				if (attribute == null) {
					return null;
				} else {
					return Collections.enumeration(attribute.keySet());
				}
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
		
		return null;
    }//end of getValueNames()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#getKeys()
	 */
	public List<SessionKey> getKeys(String viewName) {
		List<SessionKey> keyList = new ArrayList<SessionKey>();

		if (!DollyManager.isSkipConnection()) {
			try {
				View view = null;
				
				if (viewName == null) {
					//DesignDocument designDoc = getDesignDocument(config.getCouchbaseBucketName());
					DesignDocument designDoc = getDesignDocument("dolly");
			
			        boolean found = false;
			        for (ViewDesign design : designDoc.getViews()) {
			            if (design.getName().equals("getKeys")) {
			                found = true;
			                break;
			            }
			        }
			        
			        if (!found) {
			            ViewDesign design = new ViewDesign("getKeys", "function (doc, meta) {\n" +
			                    "    emit(meta.id, null);\n" +
			                    "}");
			            designDoc.getViews().add(design);
			            client.createDesignDoc(designDoc);
			        }
			        
					//View view = client.getView(config.getCouchbaseBucketName(), "get_all");
		        	view = client.getView("dolly", "getKeys");
		        
				} else {
					String[] names = viewName.split("/");
					view = client.getView(names[0], names[1]);
				}
		        
				Query query = new Query().setStale(Stale.FALSE).setReduce(false).setIncludeDocs(false);
				
				ViewResponse response = client.query(view, query);
		
				SessionKey key = null;
				for (ViewRow row : response) {
					key = new SessionKey();
					key.setKey(row.getKey());
					keyList.add(key);
				}
			} catch (Exception e) {
				if (e instanceof TransportException || e instanceof ConnectException) {
					DollyManager.setSkipConnection();
				} else if (e instanceof com.couchbase.client.vbucket.ConfigurationException) {
					DollyManager.setSkipConnection();
				} else if (e.getMessage().startsWith("Timed out waiting for")) {
					DollyManager.setSkipConnection();
				} else {
					e.printStackTrace();
				}
			}
		}
		
		return keyList;
	}//end of getKeys()

    /* (non-Javadoc)
     * @see com.athena.dolly.enhancer.client.DollyClient#destory()
     */
    public void destory() {
		client.shutdown(10, TimeUnit.SECONDS);
    }//end of destory()

	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#getStats()
	 */
	public DollyStats getStats() {
		Map<SocketAddress, Map<String, String>> statMap = client.getStats();
		
		List<SocketAddress> socketList = new ArrayList<SocketAddress>(statMap.keySet());
		
		int idx = (node++ + socketList.size()) % socketList.size();
		
		Map<String, String> stat = null;
		stat = statMap.get(socketList.get(idx));
		
		/*
		for (SocketAddress addr : socketList) {
			stat = statMap.get(addr);
			
			System.out.println(addr + " : " + stat);
		}
		//*/

		int currentSize = Integer.parseInt(stat.get("curr_items_tot"));
		int deletedSize = Integer.parseInt(stat.get("vb_active_ops_delete"));

		DollyStats dollyStat = new DollyStats();
		dollyStat.setSize(currentSize);
		dollyStat.setCurrentNumberOfEntries(Integer.toString(currentSize));
		dollyStat.setStores(Integer.toString(currentSize + deletedSize));
		dollyStat.setTotalNumberOfEntries(Integer.toString(currentSize + deletedSize));
		dollyStat.setRemoveHits(stat.get("delete_hits"));
		dollyStat.setTimeSinceStart(stat.get("uptime"));
		dollyStat.setMisses(stat.get("get_misses"));
		dollyStat.setRetrievals(stat.get("cmd_get"));
		dollyStat.setHits(stat.get("get_hits"));
		dollyStat.setRemoveMisses(stat.get("delete_misses"));
		
		return dollyStat;
	}//end of getStats()
	
	/* (non-Javadoc)
	 * @see com.athena.dolly.enhancer.client.DollyClient#printAllCache()
	 */
	public void printAllCache() {
		DesignDocument designDoc = getDesignDocument("dolly");
		
        boolean found = false;
        for (ViewDesign design : designDoc.getViews()) {
            if (design.getName().equals("getKeys")) {
                found = true;
                break;
            }
        }
        
        System.out.println("Found => " + found);
        
        if (!found) {
            ViewDesign design = new ViewDesign("getKeys", "function (doc, meta) {\n" +
                    "    emit(meta.id, null);\n" +
                    "}");
            designDoc.getViews().add(design);
            client.createDesignDoc(designDoc);
        }
        
		//View view = client.getView(config.getCouchbaseBucketName(), "get_all");
    	View view = client.getView("dolly", "getKeys");
		Query query = new Query().setStale(Stale.FALSE).setReduce(false).setIncludeDocs(true);
		//int docsPerPage = 25;
		
		ViewResponse response = client.query(view, query);
		System.out.println("TotalRow => " + response.getTotalRows());
		
		Gson gson = new Gson();
		for (ViewRow row : response) {
			Object obj = gson.fromJson((String) row.getDocument(), Object.class);
			System.out.println(row.getKey() + ":" + obj);
		}
	}//end of printAllCache()
	
	/* (non-Javadoc)
	 * @see com.athena.dolly.common.cache.client.DollyClient#healthCheck()
	 */
	public void healthCheck() {
		client.get("healthCheck");
	}//end of healthCheck()
	
	/**
	 * <pre>
	 * 지정된 Design Documents를 조회하고 존재하지 않을 경우 생성하여 반환한다.
	 * </pre>
	 * @param name
	 * @return
	 */
	private DesignDocument getDesignDocument(String name) {
		try {
			return client.getDesignDoc(name);
		} catch (com.couchbase.client.protocol.views.InvalidViewException e) {
			return new DesignDocument(name);
		}
	}//end of getDesignDocument()
}
//end of CouchbaseClient.java