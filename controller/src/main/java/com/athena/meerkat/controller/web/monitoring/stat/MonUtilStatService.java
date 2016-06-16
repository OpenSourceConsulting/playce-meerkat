package com.athena.meerkat.controller.web.monitoring.stat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class MonUtilStatService {

	@Autowired
	private MonUtilStatRepository repository;
	
	public MonUtilStatService() {
		
	}
	
	public void save(List<MonUtilStat> monUtilStats){
		repository.save(monUtilStats);
	}
	
	public void save(MonUtilStat monUtilStat){
		repository.save(monUtilStat);
	}
	
	public MonUtilStat getMonUtilStat(int monUtilStatId){
		return repository.findOne(monUtilStatId);
	}

	public List<MonUtilStat> getAll() {
		return repository.findAll();
	}
}
//end of MonUtilStatService.java