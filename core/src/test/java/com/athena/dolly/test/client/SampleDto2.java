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
 * Sang-cheon Park	2014. 12. 12.		First Draft.
 */
package com.athena.dolly.test.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class SampleDto2 implements Externalizable {

	private String userId = "osci";
	private String firstName = "Open Source";
	private String lastName = "Consulting";
	private String email = "support@osci.kr";
	private int age = 30;
	private String address1 = "Address 1";
	private String address2 = "Address 2";
	
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.userId);
		out.writeObject(this.firstName);
		out.writeObject(this.lastName);
		out.writeObject(this.email);
		out.writeObject(this.age);
		out.writeObject(this.address1);
		out.writeObject(this.address2);
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.userId = (String)in.readObject();
        this.firstName = (String)in.readObject();
        this.lastName = (String)in.readObject();
        this.email = (String)in.readObject();
        this.age = (Integer)in.readObject();
        this.address1 = (String)in.readObject();
        this.address2 = (String)in.readObject();
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String toString() {
		return "userId : " + userId +
				", firstName : " + firstName +
				", lastName : " + lastName +
				", email : " + email +
				", age : " + age +
				", address1 : " + address1 +
				", address2 : " + address2;
	}
}
//end of SampleDto2.java