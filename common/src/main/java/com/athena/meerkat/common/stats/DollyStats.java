/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
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
 * Sang-cheon Park	2013. 12. 17.		First Draft.
 */
package com.athena.meerkat.common.stats;

import java.util.List;

/**
 * <pre>
 * Infinispan의 속성, 통계정보 및 저장된 키 목록을 저장하기 위한 Data Object
 * - Cache Data는 포함되지 않으며 필요할 경우 <b>DollyManager.getClient().get(key)</b>를 호출한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DollyStats {

	/** RemoteCache Properties */
	private String protocolVersion;
	private String version;
	private String name;
	private Boolean isEmpty;
	private Integer size;
	
	/** RemoteCache Statistics */
	/** Number of seconds since Hot Rod started */
	private String timeSinceStart;
	/** Number of entries currently in the Hot Rod server */
	private String currentNumberOfEntries;
	/** Number of entries stored in Hot Rod server since the server started running */
	private String totalNumberOfEntries;
	/** Number of put operations */
	private String stores;
	/** Number of get operations */
	private String retrievals;
	/** Number of get hits */
	private String hits;
	/** Number of get misses */
	private String misses;
	/** Number of removal hits */
	private String removeHits;
	/** Number of removal misses */
	private String removeMisses;
	
	/** All keys in the RemoteCache */
	private List<String> cacheKeys;

	/**
	 * @return the protocolVersion
	 */
	public String getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * @param protocolVersion the protocolVersion to set
	 */
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isEmpty
	 */
	public Boolean getIsEmpty() {
		return isEmpty;
	}

	/**
	 * @param isEmpty the isEmpty to set
	 */
	public void setIsEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return the timeSinceStart
	 */
	public String getTimeSinceStart() {
		return timeSinceStart;
	}

	/**
	 * @param timeSinceStart the timeSinceStart to set
	 */
	public void setTimeSinceStart(String timeSinceStart) {
		this.timeSinceStart = timeSinceStart;
	}

	/**
	 * @return the currentNumberOfEntries
	 */
	public String getCurrentNumberOfEntries() {
		return currentNumberOfEntries;
	}

	/**
	 * @param currentNumberOfEntries the currentNumberOfEntries to set
	 */
	public void setCurrentNumberOfEntries(String currentNumberOfEntries) {
		this.currentNumberOfEntries = currentNumberOfEntries;
	}

	/**
	 * @return the totalNumberOfEntries
	 */
	public String getTotalNumberOfEntries() {
		return totalNumberOfEntries;
	}

	/**
	 * @param totalNumberOfEntries the totalNumberOfEntries to set
	 */
	public void setTotalNumberOfEntries(String totalNumberOfEntries) {
		this.totalNumberOfEntries = totalNumberOfEntries;
	}

	/**
	 * @return the stores
	 */
	public String getStores() {
		return stores;
	}

	/**
	 * @param stores the stores to set
	 */
	public void setStores(String stores) {
		this.stores = stores;
	}

	/**
	 * @return the retrievals
	 */
	public String getRetrievals() {
		return retrievals;
	}

	/**
	 * @param retrievals the retrievals to set
	 */
	public void setRetrievals(String retrievals) {
		this.retrievals = retrievals;
	}

	/**
	 * @return the hits
	 */
	public String getHits() {
		return hits;
	}

	/**
	 * @param hits the hits to set
	 */
	public void setHits(String hits) {
		this.hits = hits;
	}

	/**
	 * @return the misses
	 */
	public String getMisses() {
		return misses;
	}

	/**
	 * @param misses the misses to set
	 */
	public void setMisses(String misses) {
		this.misses = misses;
	}

	/**
	 * @return the removeHits
	 */
	public String getRemoveHits() {
		return removeHits;
	}

	/**
	 * @param removeHits the removeHits to set
	 */
	public void setRemoveHits(String removeHits) {
		this.removeHits = removeHits;
	}

	/**
	 * @return the removeMisses
	 */
	public String getRemoveMisses() {
		return removeMisses;
	}

	/**
	 * @param removeMisses the removeMisses to set
	 */
	public void setRemoveMisses(String removeMisses) {
		this.removeMisses = removeMisses;
	}

	/**
	 * @return the cacheKeys
	 */
	public List<String> getCacheKeys() {
		return cacheKeys;
	}

	/**
	 * @param cacheKeys the cacheKeys to set
	 */
	public void setCacheKeys(List<String> cacheKeys) {
		this.cacheKeys = cacheKeys;
	}
	
	@Override
	public String toString() {
		return "DollyStats{" + 
					"protocolVersion=" + protocolVersion + 
					", version=" + version + 
					", name=" + name + 
					", isEmpty=" + isEmpty + 
					", size=" + size + 
					", timeSinceStart=" + timeSinceStart + 
					", currentNumberOfEntries=" + currentNumberOfEntries + 
					", totalNumberOfEntries=" + totalNumberOfEntries + 
					", stores=" + stores + 
					", retrievals=" + retrievals + 
					", hits=" + hits + 
					", misses=" + misses + 
					", removeHits=" + removeHits + 
					", removeMisses=" + removeMisses + 
					", cacheKeys=" + cacheKeys + 
				"}";
	}
}
//end of DollyStats.java