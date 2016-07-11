/*
 * File: app/controller/dashboardController.js
 */

Ext.define('webapp.controller.dashboardController', {
    extend: 'Ext.app.Controller',

    refs: {
        centerContainer: '#centerContainer',
        dashboardBtn: '#dashboardBtn'
    },
	control:{
		"#dashboardPanel": {
            activate: 'onActivateContainer'
        }
	},

	onActivateContainer: function(container, opts){
	
		console.log('activate dashboard.');
		var dashboard = container;
		//statistic data
		this.reloadStatisticData(dashboard);
		//cpu chart
		var minsAgo = 30;
		var url =  GlobalData.urlPrefix + "dashboard/cpu.used_per/" + minsAgo + "/all";
		var chart = dashboard.down("#machineCPUChart");
		webapp.app.getController("ServerMonitoringController").loadServersChart(chart, 0, url, "%", false);
		
		//domain chart
		this.reloadDomainChart(dashboard);
		
		//jdbc connection chart
		var jdbcChart = dashboard.down("#jdbcConnStatsChart");
		jdbcChart.getStore().load();
		
		//alert grid
		this.reloadAlertGrid(dashboard);
		
		//log grid
		this.reloadTaskHistoryGrid(dashboard);
	},
	
	reloadStatisticData: function(dashboard){
		//general statistic info
		var statUrl = GlobalData.urlPrefix + "dashboard/get/stats";
		webapp.app.getController("globalController").ajaxRequest(statUrl, {}, "GET", function(json){
			var data = json.data;
			dashboard.down("#domainNoDisplayField").setText(data.domainCount);
			dashboard.down("#tomcatInstNoDisplayField").setText(data.tomcatInstCount);
			dashboard.down("#serverNoDisplayField").setText(data.serverCount);
			dashboard.down("#dsNoDisplayField").setText(data.dsCount);
		}, null);
	},
	
	reloadDomainChart: function(dashboard){
		//domain pie chart
		var domainChart = dashboard.down("#domainStatsChart");
		domainChart.getStore().load();
	},
	
	reloadAlertGrid: function(dashboard) {
		//grid
		var grid = dashboard.down("#alertGrid");
		grid.getStore().load();
	},
	
	reloadTaskHistoryGrid: function(dashboard) {
		//grid
		var grid = dashboard.down("#taskHistoryGrid");
		grid.getStore().load();
	}
	
	
	

});
