package com.athena.meerkat.controller.web.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;

@Controller
@RequestMapping("/menu")
public class MenuController {

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private TomcatInstanceService tomcatService;
	@Autowired
	private ServerService serverService;

	@RequestMapping(value = "/getNodes", method = RequestMethod.GET)
	@ResponseBody
	public List<HashMap<String, Object>> getNodes(String node) {
		boolean isLeaf = false;
		String prefixMeuId = "";
		List<HashMap<String, Object>> nodes = new ArrayList<>();

		if (node.equals("tomcatMng") || node.equals("mon_tomcats")) {
			List<TomcatDomain> domains = domainService.getAll();
			isLeaf = false;
			prefixMeuId = node + "_domain_";
			for (TomcatDomain d : domains) {

				nodes.add(createMenuMap(d.getName(), prefixMeuId + d.getId(), isLeaf, null));
			}
		} else if ((node.indexOf("mon_tomcats") >= 0 || node.indexOf("tomcatMng_domain") >= 0) && node.indexOf("_domain_") > 0) {
			Integer objId = Integer.parseInt(node.substring(node.indexOf("_domain_") + "_domain_".length()));
			List<TomcatInstance> tomcats = new ArrayList<>();
			isLeaf = true;
			prefixMeuId = node + "_tomcat_";
			tomcats = tomcatService.getTomcatListByDomainId(objId);
			for (TomcatInstance t : tomcats) {

				DomainTomcatConfiguration domainConfig = tomcatService.getTomcatConfig(t.getId());

				String menuText = t.getName() + "(" + domainConfig.getHttpPort() + ")";
				nodes.add(createMenuMap(menuText, prefixMeuId + t.getId(), isLeaf, t.getState()));
			}
		} else if (node.equals("mon_servers")) {
			List<Server> servers = new ArrayList<>();
			isLeaf = true;
			prefixMeuId = node + "_server_";
			servers = serverService.getList();
			for (Server s : servers) {

				nodes.add(createMenuMap(s.getName(), prefixMeuId + s.getId(), isLeaf, null));
			}
		} else {

		}

		return nodes;
	}

	/**
	 * 
	 * @param text
	 * @param menuId
	 * @param isLeaf
	 * @param state
	 *            tomcat instance state
	 * @return
	 */
	private HashMap<String, Object> createMenuMap(String text, String menuId, boolean isLeaf, Integer state) {
		HashMap<String, Object> n = new HashMap<String, Object>();
		n.put("text", text);
		n.put("id", menuId);
		n.put("leaf", isLeaf);
		if (state != null) {
			n.put("state", state);
		}
		return n;
	}
}
