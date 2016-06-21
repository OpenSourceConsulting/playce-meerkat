package com.athena.meerkat.controller.web.monitoring.stat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.entities.MonAlertConfig;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.repositories.ServerRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainAlertSettingRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class MonUtilStatService {

	@Autowired
	private MonUtilStatRepository repository;
	@Autowired
	private DomainAlertSettingRepository settingRepository;

	@Autowired
	private ServerRepository serverRepo;

	@Autowired
	private TomcatInstanceRepository tomcatRepo;

	public MonUtilStatService() {

	}

	public void save(List<MonUtilStat> monUtilStats) {
		repository.save(monUtilStats);
	}

	public void save(MonUtilStat monUtilStat) {
		repository.save(monUtilStat);
	}

	public MonUtilStat getMonUtilStat(int monUtilStatId) {
		return repository.findOne(monUtilStatId);
	}

	public List<MonUtilStat> getAll() {
		return repository.findAll();
	}

	public List<MonUtilStat> getAlerts(Integer count) {
		List<MonUtilStat> list = repository.findAllByOrderByMonValueDescUpdateDtDesc(new PageRequest(0, 10));
		//	HashMap<Integer, List<MonAlertConfig>> settingMap = new HashMap<Integer, List<MonAlertConfig>>();
		List<MonAlertConfig> alertSettings = new ArrayList<>();
		for (MonUtilStat alert : list) {
			Integer serverId = alert.getServerId();

			//set name and server id
			if (serverId == 0) {
				TomcatInstance tc = tomcatRepo.findOne(alert.getTomcatInstanceId());
				if (tc != null) {
					serverId = tc.getServerId();
					alert.setName(tc.getDomainName() + " > " + tc.getName());
					alertSettings = settingRepository.findByServer_Id(tc.getDomainId());
				}
			} else {
				Server s = serverRepo.getOne(serverId);
				if (s != null) {
					alert.setName(s.getName());
					alertSettings = settingRepository.findByServer_Id(s.getId());
				}
			}
			//set type
			if (alert.getMonFactorId().equals(MeerkatConstants.MON_FACTOR_CPU_USED)) {
				alert.setType("CPU");
			} else if (alert.getMonFactorId().equals(MeerkatConstants.MON_FACTOR_MEM_USED_PER)) {
				alert.setType("Memory");
			} else if (alert.getMonFactorId().contains("/")) {//disk
				alert.setType("Disk(" + alert.getMonFactorId() + ")");
			}

			//			if (!settingMap.containsKey(serverId)) {
			//				alertSettings = alertRepository.findByServerId(serverId);
			//				settingMap.put(serverId, alertSettings);
			//			} else {
			//				alertSettings = settingMap.get(serverId);
			//			}

			for (MonAlertConfig setting : alertSettings) {
				if (alert.getMonFactorId().equals(setting.getMonFactorId())) {
					if (setting.getThresholdOpCdId() == MeerkatConstants.ALERT_ITEM_OPPERATOR_GREATER_THAN_ID) {
						alert.setAlertStatus(alert.getMonValue() > setting.getThresholdValue());
					} else if (setting.getThresholdOpCdId() == MeerkatConstants.ALERT_ITEM_OPPERATOR_LESS_THAN_ID) {
						alert.setAlertStatus(alert.getMonValue() < setting.getThresholdValue());
					}
				}
			}
		}
		return list;
	}
}
//end of MonUtilStatService.java