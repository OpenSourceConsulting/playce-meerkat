/* 
 * Copyright 2013 The Athena-Peacock Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 7. 22.		First Draft.
 */
package com.athena.meerkat.agent.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * Meerkat Agent 시스템의 Physical Hardware 주소를 조회하기 위한 유틸 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MacAddressUtil {
	
	/**
	 * <pre>
	 * localhost에 대한 mac address를 조회한다.
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	public static String getMacAddress() throws Exception {
		return getMacAddress(InetAddress.getLocalHost());
	}//end of getMacAddress()

	/**
	 * <pre>
	 * 주어진 호스트에 대한 mac address를 조회한다.
	 * </pre>
	 * @param host
	 * @return
	 * @throws Exception
	 */
//	public static String getMacAddress(String host) throws Exception {
//		return getMacAddress(InetAddress.getByName(host));
//	}//end of getMacAddress()

	/**
	 * <pre>
	 * 주어진 호스트에 대한 mac address를 조회한다.
	 * </pre>
	 * @param host
	 * @return
	 * @throws Exception
	 */
	public static String getMacAddress(InetAddress host) throws Exception {
		String macAddress = null;
		NetworkInterface iface = NetworkInterface.getByInetAddress(host);
		
		if (iface != null) {
			byte[] hardware = iface.getHardwareAddress();
			
			if (hardware != null && hardware.length == 6 && hardware[1] != (byte) 0xff) {
				StringBuilder sb = new StringBuilder();
				
				for (int i = 0; i < hardware.length; i++) {
					sb.append(String.format("%02x%s", hardware[i], (i < hardware.length - 1) ? ":" : ""));
				}
				
				macAddress = sb.toString().toLowerCase().replaceAll(":", "").replaceAll("-", "");
			}
		}
		
		return macAddress;
	}//end of getMacAddress()

	/**
	 * <pre>
	 * 주어진 NetworkInterface 이름에 대한 mac address를 조회한다.
	 * </pre>
	 * @param host
	 * @return
	 * @throws Exception
	 */
	public static String getMacAddress(String name) throws Exception {
		String macAddress = null;
		List<String> macAddressList = null;
		Set<String> macAddressSet = null;
		Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
		
		if (ifs != null) {
			macAddressSet = new LinkedHashSet<String>();
			
			NetworkInterface nifs = null;
            while (ifs.hasMoreElements()) {
            	nifs = ifs.nextElement();
            	Enumeration<InetAddress> hosts = nifs.getInetAddresses();
                
            	if (hosts != null) {
            		macAddress = null;
                	
            		while (hosts.hasMoreElements()) {
            			macAddress = getMacAddress(hosts.nextElement());
            			
            			if (macAddress != null) {
            				if (nifs.getName().equals(name)) {
            					return macAddress;
            				} else {
            					macAddressSet.add(macAddress);
            				}
            			}
            		}
            	}
            }
        }
		
		if(macAddressSet == null) {
			return null;
		}
		
		// 중복 제거 후 내림차순으로 정렬한다.
		macAddressList = new ArrayList<String>(macAddressSet);
		Collections.sort(macAddressList);
		Collections.reverse(macAddressList);
		
		return macAddressList.get(0);
	}//end of getMacAddress()
	
	/**
	 * <pre>
	 * 전체 mac address 목록을 조회한다.
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	public static List<String> getMacAddressList() throws Exception {
		List<String> macAddressList = null;
		Set<String> macAddressSet = null;
		Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
		
		if (ifs != null) {
			macAddressSet = new LinkedHashSet<String>();
			
            while (ifs.hasMoreElements()) {
            	Enumeration<InetAddress> hosts = ifs.nextElement().getInetAddresses();
                
            	if (hosts != null) {
            		String macAddress = null;
            		
            		while (hosts.hasMoreElements()) {
            			macAddress = getMacAddress(hosts.nextElement());
            			
            			if (macAddress != null) {
            				macAddressSet.add(macAddress);
            			}
            		}
            	}
            }
        }
		
		if(macAddressSet == null) {
			return null;
		}
		
		// 중복 제거 후 내림차순으로 정렬한다.
		macAddressList = new ArrayList<String>(macAddressSet);
		Collections.sort(macAddressList);
		Collections.reverse(macAddressList);
		
		return macAddressList;
	}//end of getMacAddressList()

	/**
	 * <pre>
	 * 전체 mac address 목록을 조회한다.
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getMacAddressMap() throws Exception {
		Map<String, String> macAddressMap = new HashMap<String, String>();
		Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
		
		if (ifs != null) {
            while (ifs.hasMoreElements()) {
            	Enumeration<InetAddress> hosts = ifs.nextElement().getInetAddresses();
                
            	if (hosts != null) {
            		InetAddress host = null;
            		String macAddress = null;
            		
            		while (hosts.hasMoreElements()) {
            			host = hosts.nextElement();
            			macAddress = getMacAddress(host);
            			
            			if (macAddress != null) {
            				macAddressMap.put(host.getHostAddress(), macAddress);
            			}
            		}
            	}
            }
        }
		
		if(macAddressMap.size() == 0) {
			return null;
		}
		
		return macAddressMap;
	}//end of getMacAddressMap()

}
//end of MacAddressUtil.java