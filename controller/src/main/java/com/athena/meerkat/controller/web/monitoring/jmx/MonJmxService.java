package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
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

				TomcatInstance instance = tiService.findOne(monJmx.getInstanceId());

				if (instance != null && instance.getState() == MeerkatConstants.TOMCAT_STATUS_STARTING
						&& monJmx.getMonValue().intValue() == MeerkatConstants.TOMCAT_STATUS_SHUTDOWN) {

					LOGGER.debug("{} instance is starting. don't save state.", monJmx.getInstanceId());

				} else if (instance != null && instance.getState() == MeerkatConstants.TOMCAT_STATUS_STOPPING
						&& monJmx.getMonValue().intValue() == MeerkatConstants.TOMCAT_STATUS_RUNNING) {

					LOGGER.debug("{} instance is stopping. don't save state.", monJmx.getInstanceId());

				} else if (instance != null) {
					tiService.saveState(instance, monJmx.getMonValue().intValue());

				} else {
					LOGGER.warn("{} instance is null. don't save state.", monJmx.getInstanceId());
				}
			}

		}
	}

	public List<MonJmx> getJmxMonDataList(String[] types, int instanceId, Date time, Date now) {
		return repository.findByInstanceIdAndMonFactorIds(types, instanceId, time, now);
	}

	public List<MonJmx> getJmxMonDataList(String[] types, Date time, Date now) {
		return repository.findByMonFactorIds(types, time, now);
	}

	public List<MonJmx> getJmxJDBCConnectionList(Date time, Date now) {
		return repository.findByJdbcConnection(time, now);
	}

	public Long getJDBCConnectionCount(String name, Date time, Date now) {
		String type = MeerkatConstants.MON_JMX_FACTOR_JDBC_CONNECTIONS + "." + name;
		return repository.getJdbcConnectionSum(type, time, now);
	}
}
//end of MonJmxService.java