package com.athena.meerkat.controller.web.monitoring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.web.common.model.ExtjsGridParam;

/**
 * <pre>
 * 
 * </pre>
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
	
	public void insertMonData(MonData monData){
		repository.save(monData);
	}
	/*
	public List<MonData> getMonDataList(ExtjsGridParam gridParam){
		return repository.getMonDataList(gridParam);
	}
	
	public int getMonDataListTotalCount(ExtjsGridParam gridParam){
		
		return repository.getMonDataListTotalCount(gridParam);
	}
	
	public MonData getMonData(MonData monData){
		return repository.getMonData(monData);
	}
	
	public void updateMonData(MonData monData){
		repository.updateMonData(monData);
	}
	
	public void deleteMonData(MonData monData){
		repository.deleteMonData(monData);
	}
*/
}
//end of MonDataService.java