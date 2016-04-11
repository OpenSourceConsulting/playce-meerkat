package com.athena.meerkat.controller.web.monitoring;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.common.model.ExtjsGridParam;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class MonDataService {

	@Autowired
	private MonDataRepository repository;

	public MonDataService() {
		// TODO Auto-generated constructor stub
	}

	public void insertMonData(MonData monData) {
		repository.save(monData);
	}

	public void insertMonDatas(List<MonData> monDatas) {
		repository.save(monDatas);
	}

	public List<MonData> getMonDataList(String type, Integer serverId,
			Date tenMinsAgo, Date now) {
		return repository.findByMonFactorIdAndServerIdInTenMins(type, serverId,
				tenMinsAgo, now);
	}
}
// end of MonDataService.java