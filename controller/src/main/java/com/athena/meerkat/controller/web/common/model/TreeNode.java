/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Bong-Jin Kwon	2014. 9. 12.		First Draft.
 */
package com.athena.meerkat.controller.web.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Bong-Jin Kwon
 *
 */
public class TreeNode extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	private static final String KEY_CHILDREN = "children";
	/**
	 * 
	 */
	public TreeNode() {
		put(KEY_CHILDREN, new ArrayList<TreeNode>());
	}

	/**
	 * @param initialCapacity
	 */
	public TreeNode(int initialCapacity) {
		super(initialCapacity);
		put(KEY_CHILDREN, new ArrayList<TreeNode>());
	}

	/**
	 * @param m
	 */
	public TreeNode(TreeNode m) {
		super(m);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public TreeNode(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		put(KEY_CHILDREN, new ArrayList<TreeNode>());
	}
	
	@SuppressWarnings("unchecked")
	public void addChild(TreeNode node){
		List<TreeNode> children = (List<TreeNode>)get(KEY_CHILDREN);
		
		children.add(node);
	}

}
