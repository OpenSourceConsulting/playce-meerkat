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
 * BongJin Kwon		2016. 3. 8.		First Draft.
 */
package cargo;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXMain {

	public static void main(String[] args) throws Exception {

		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://192.168.0.87:8225/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url);
		//MBeanServerConnection server = jmxc.getMBeanServerConnection();
		
		/*
		Object o = jmxc.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
		CompositeData cd = (CompositeData) o;
		System.out.println(cd.get("used"));// used or max
		*/
		
		//ObjectName name = new ObjectName("Catalina:type=Manager,context=/cargocpc,host=localhost");
		//Object o = jmxc.getMBeanServerConnection().getAttribute(name, "activeSessions");
		
		ObjectName name = new ObjectName("Catalina:type=ThreadPool,name=\"http-bio-8083\"");
		Object o = jmxc.getMBeanServerConnection().getAttribute(name, "currentThreadsBusy");// currentThreadCount or currentThreadsBusy
		System.out.println(o.toString());
		
		
	}

}
// end of JMXMain.java