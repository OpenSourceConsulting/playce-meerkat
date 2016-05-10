package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class MonJmxService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonJmxService.class);

	@Autowired
	private MonJmxRepository repository;

	@Autowired
	private TomcatInstanceService tiService;

	public MonJmxService() {
	}

	public void insertMonJmx(MonJmx monJmx) {
		repository.save(monJmx);
	}

	public void insertMonJmxs(List<MonJmx> monJmxs) {
		repository.save(monJmxs);
	}

	@Transactional
	public void saveInstanceState(List<MonJmx> monJmxs) {
		for (MonJmx monJmx : monJmxs) {

			if (MeerkatConstants.MON_FACTOR_TI_RUN.equals(monJmx.getMonFactorId())) {
				tiService.saveState(monJmx.getInstanceId(), monJmx.getMonValue().intValue());
			}

		}
	}

	public List<MonJmx> getJmxMonDataList(String[] types, int instanceId, Date time, Date now) {
		return repository.findByInstanceIdAndMonFactorIds(types, instanceId, time, now);
	}

	/*
	public List<MonJmx> getMonJmxList(ExtjsGridParam gridParam){
		return repository.getMonJmxList(gridParam);
	}
	
	public int getMonJmxListTotalCount(ExtjsGridParam gridParam){
		
		return repository.getMonJmxListTotalCount(gridParam);
	}
	
	public MonJmx getMonJmx(MonJmx monJmx){
		return repository.getMonJmx(monJmx);
	}
	
	public void updateMonJmx(MonJmx monJmx){
		repository.updateMonJmx(monJmx);
	}
	
	public void deleteMonJmx(MonJmx monJmx){
		repository.deleteMonJmx(monJmx);
	}
	*/
}
//end of MonJmxService.java