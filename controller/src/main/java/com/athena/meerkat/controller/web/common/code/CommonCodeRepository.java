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
 * BongJin Kwon		2016. 3. 29.		First Draft.
 */
package com.athena.meerkat.controller.web.common.code;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.CommonCode;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface CommonCodeRepository extends
		JpaRepository<CommonCode, Integer> {

	List<CommonCode> findByGropId(String gropId);

	// get by name
	CommonCode findByCodeNm(String codeNm);
}
// end of CommonCodeRepository.java