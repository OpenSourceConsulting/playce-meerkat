package com.athena.meerkat.controller.web.monitoring.jmx;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.monitoring.StompEventListener;
import com.athena.meerkat.controller.web.monitoring.server.MonDataController;
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
	
	@Autowired
	private StompEventListener stompEventListener;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;


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
	
	/**
	 * <pre>
	 * server agent 에게 신규 tomcat instance 의 모니터링을 위한 관련 데이타(DomainTomcatConfiguration)를 전송한다.
 	 * </pre>
	 * @param server
	 * @param tomcatConfig
	 */
	public void requestTomcatInstanceAdding(Server server, DomainTomcatConfiguration tomcatConfig) {
		
		if (stompEventListener.isRunningAgent(server.getId())) {
			
			SimpleJsonResponse cmdRes = createCommandRes("add_tomcat_instnace", "tomcatInstanceConfig", tomcatConfig);

			String userDest = stompEventListener.getUserDestination(server.getId());
			
			this.messagingTemplate.convertAndSendToUser(userDest, MonDataController.STOMP_USER_DEST, cmdRes);
			
			LOGGER.debug("sent 'add_tomcat_instnace' command. server:{}, userDest: {}, tomcatInstanceId: {}", server.getSshIPAddr(), userDest, tomcatConfig.getTomcatInstanceId());
		} else {
			LOGGER.warn("server ({}) agent is not running.", server.getSshIPAddr());
		}
		
	}
	
	/**
	 * <pre>
	 * server agent 에게 tomcat instance 의 모니터링을 중지시킨다.
	 * </pre>
	 * @param server
	 * @param tomcatInstanceId
	 */
	public void requestTomcatInstanceRemoving(Server server, int tomcatInstanceId) {
		
		if (stompEventListener.isRunningAgent(server.getId())) {
			
			SimpleJsonResponse cmdRes = createCommandRes("remove_tomcat_instnace", "tomcatInstanceId", tomcatInstanceId);

			String userDest = stompEventListener.getUserDestination(server.getId());
			
			this.messagingTemplate.convertAndSendToUser(userDest, MonDataController.STOMP_USER_DEST, cmdRes);
			
			LOGGER.debug("sent 'remove_tomcat_instnace' command. server:{}, userDest: {}, tomcatInstanceId: {}", server.getSshIPAddr(), userDest, tomcatInstanceId);
		} else {
			LOGGER.warn("server ({}) agent is not running.", server.getSshIPAddr());
		}
		
	}
	
	private SimpleJsonResponse createCommandRes(String command, String paramKey, Object paramObj) {
		
		SimpleJsonResponse cmdRes = new SimpleJsonResponse();
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("cmd", command);
		dataMap.put(paramKey, paramObj);
		cmdRes.setData(dataMap);
		
		return cmdRes;
	}
	
	public void deleteAll(int instanceId) {
		repository.deleteByInstanceId(instanceId);
	}
}
//end of MonJmxService.java