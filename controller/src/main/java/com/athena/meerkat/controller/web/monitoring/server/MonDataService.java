package com.athena.meerkat.controller.web.monitoring.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

	@Autowired
	private MonFsRepository fsRepo;

	public MonDataService() {
		// TODO Auto-generated constructor stub
	}

	public void insertMonData(MonData monData) {
		repository.save(monData);
	}

	public void insertMonDatas(List<MonData> monDatas) {
		repository.save(monDatas);
	}

	public List<MonDataViewModel> getMonDataList(String[] types,
			Integer serverId, Date time, Date now) {
		List<MonData> list = repository.findByMonFactorIdAndServerId(types,
				serverId, time, now);

		List<MonDataViewModel> result = new ArrayList<>();
		int step = types.length;

		for (int i = 0; i <= list.size() - step;) {
			MonDataViewModel model = new MonDataViewModel();
			model.setMonDt(list.get(i).getMonDt());
			Map<String, Double> value = new HashMap<>();
			int count = 0;
			while (count < step) {
				value.put(list.get(i + count).getMonFactorId(),
						list.get(i + count).getMonValue());
				count++;
			}
			model.setValue(value);
			result.add(model);
			i += step;
		}
		return result;
	}

	public void saveMonFsList(List<MonFs> monFsList) {
		fsRepo.save(monFsList);
	}

	public List<MonFs> getDiskMonDataList(Integer serverId) {
		return fsRepo.getFsMonData(serverId);
	}
}
// end of MonDataService.java