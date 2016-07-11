/*
 * File: app/controller/ServerMonitoringController.js
 */

Ext.define('webapp.controller.ServerMonitoringController', {
    extend: 'Ext.app.Controller',
	require:[
		'webapp.view.MonitoringRealTimeServerChartWindow'
	],
    control: {
		"#DetailMonitoringMachineContainer":{
			deactivate: 'containerDeactivate',
			activate: "containerActivate"
		},
		"#MonitoringMachineContainer": {
			activate: 'monitoringMachineContainerActivate'
		},
		"#reloadStaticChartBtn":{
			click: 'reloadDetailServerStaticChartBtnClick'
		},
		"#monitoringRealTimeServerChartWindow":{
			destroy: 'realtimeWindowClose'
		},
		"#reloadServersStaticChartBtn": {
			click: 'reloadServersStaticChartBtnClick'
		},
		"#realtimeServerReloadChartBtn":{
			click: 'realtimeServerReloadChartBtnClick'
		}
		
    },
	
	showRealTimeChartWindow: function(type) {
		var window = Ext.create("widget.MonitoringRealTimeServerChartWindow");
		var title = "";
		if(type === GlobalData.SERVER_CPU_CHART){
			title = "Real time CPU Usage chart";
		} else if(type === GlobalData.SERVER_MEMORY_CHART){
			title = "Real time memory usage chart";
		} else if(type === GlobalData.SERVER_NETIN_CHART){
			title = "Real time incomming network traffic chart";
		} else if(type === GlobalData.SERVER_NETOUT_CHART){
			title = "Real time outgoing network traffic chart";
		}
		var minsAgo = window.down("#realtimePeriod").getValue();
		window.down("#chartTypeHiddenField").setValue(type);
		this.reloadRealTimeChart(window, type, minsAgo);
		window.setTitle(title);
		window.show();
	},
	
	containerActivate: function(container, opts){
		var serverId = GlobalData.lastSelectedMenuId
		this.loadServerInfo(container, serverId);
		this.reloadDetailServerStaticChart();
	},
	
	containerDeactivate: function(container, eOpts) {
		this.clearInterval();
	},
	
	clearInterval:function(){
		while(interval = GlobalData.detailMonServerIntervalArray.pop()){
			webapp.app.getController("globalController").stopRealtimeAjaxRequest(interval);
		}
	},
	
	loadServerInfo: function(container, serverId){
		var form = container.down("form");
		var url = GlobalData.urlPrefix + "res/server/get";
		form.load({
			url:url,
			params:{"id": serverId},
			method:"GET",
			waitMsg: 'Loading...'
		});
	},
	
	loadServersChart:function(chart, type, url, unit, isRealTime, monThresholdValue){
		var store = Ext.create('Ext.data.JsonStore', {fields: [], data:[]});
		var callback = function(json){
			//hide message box when reload
			Ext.MessageBox.hide();
			if(json.list.length <= 0){
				return;
			}
			//extract keys
			var fields = Object.keys(json.list[0].value);
			var dataArr = [];
			
			Ext.Array.each(json.list, function(name, index, record) {
				var value = json.list[index].value;
				var data = {};
				data.monDtString = json.list[index].monDtString;
				for(key in fields) {
					var field = fields[key];
					data[field] = value[field];
				}
				dataArr.push(data);
			});
			
			//set max value
			currentMaxValue = chart.getAxes()[1].getMaximum();
			var newMax = webapp.app.getController("globalController").getMonitoringMaxValue(dataArr, fields);
			if(newMax >= currentMaxValue) {
				chart.getAxes()[1].setMaximum(newMax + monThresholdValue);
			}
			
			store.setFields(fields);
			store.loadData(dataArr);
			chart.setStore(store);
			chart.setSeries([]);
			chart.redraw(true);

            Ext.Array.each(fields, function(name, index, record) {
				chart.addSeries({
				   type: 'line',
				   axis: 'left',
				   xField:"monDtString",
				   yField: fields[index],
				   style: {
						lineWidth: 2,
						strokeStyle: GlobalData.seriesColors[ (index  + type)% GlobalData.seriesColors.length ],
						fillOpacity: 0.6,
						miterLimit: 3,
						lineCap: 'miter'
				   },
				   tips: {
						trackMouse: true,
						style: 'background: yellow',
						height: 20,
						showDelay: 0,
						dismissDelay: 0,
						hideDelay: 0,
						renderer: function(storeItem, item) {
							this.setTitle(fields[index] + " at "+ storeItem.get('monDtString') + ': ' + storeItem.get(fields[index]).toFixed(2) + " (" + unit + ")");
						}
					}
			   });
			});

		}
		this.clearInterval();
		
		webapp.app.getController("globalController").ajaxRequest(url, {}, "GET", callback, null);
		
		if (isRealTime) {
			webapp.app.getController("globalController").realtimeAjaxRequest(5, url, {}, "GET", callback, 
			function(timeInterval){
				if(timeInterval > 0) {
					GlobalData.monServerIntervalArray.push(timeInterval);
				}
			});
		}
			
	},
	
	monitoringMachineContainerActivate: function(container, eOpts){
		
		this.reloadServersStaticCharts();
	},
	
	clearInterval:function(){
		while(interval = GlobalData.monServerIntervalArray.pop()){
			webapp.app.getController("globalController").stopRealtimeAjaxRequest(interval);
		}
	},
	
	reloadDetailServerStaticChartBtnClick: function(button, eOpts){
		this.reloadDetailServerStaticChart();
	},
	
	reloadDetailServerStaticChart: function(){
		var container = Ext.getCmp("DetailMonitoringMachineContainer");
		var serverId = GlobalData.lastSelectedMenuId;
		//clear charts
		var charts = container.query("cartesian");
		Ext.each(charts, function(name, index, record){
			charts[index].getStore().loadData({});
			charts[index].redraw();
		});
		
		Ext.MessageBox.show({
			msg: 'Loading ...',
			progressText: 'Loading...',
			width:300,
			wait:true,
			waitConfig: {interval:200}
		});
		var minsAgo = container.down("#staticPeriodMonitoringCombobox").getValue();
		var detailCpuUrl = GlobalData.urlPrefix + "monitor/server/cpu.used_per/" + serverId + "/" + minsAgo;
		this.loadServersChart(container.down("#machineCPUChart"), 0, detailCpuUrl, "%", false, GlobalData.monValueThreshold);
		container.down("#machineCPUChart").legend.hide();
		
		var detailMemoryUrl = GlobalData.urlPrefix + "monitor/server/mem.used/" + serverId + "/" + minsAgo;
		this.loadServersChart(container.down("#machineMemoryChart"), 1, detailMemoryUrl, "MBs", false, GlobalData.monValueThreshold);
		container.down("#machineMemoryChart").legend.hide();
		
		var detailNetInUrl = GlobalData.urlPrefix + "monitor/server/net.in/" + serverId + "/" + minsAgo;
		this.loadServersChart(container.down("#machineNetInChart"), 2, detailNetInUrl, "Kbs", false, GlobalData.netMonValueThreshold);
		container.down("#machineNetInChart").legend.hide();
		
		var detailNetOutUrl = GlobalData.urlPrefix + "monitor/server/net.out/" + serverId + "/" + minsAgo;
		this.loadServersChart(container.down("#machineNetOutChart"), 3, detailNetOutUrl, "Kbs", false, GlobalData.netMonValueThreshold);
		container.down("#machineNetOutChart").legend.hide();
		
		this.loadDiskMonitoring(serverId);
	},
	
	reloadServersStaticCharts: function(){
		Ext.MessageBox.show({
            msg: 'Loading, please wait...',
            progressText: 'Loading...',
            width:300,
            wait:true,
            waitConfig: {interval:200}

        });
		var container = Ext.getCmp("MonitoringMachineContainer");
		var minsAgo = container.down("#realtimePeriod").getValue();
		var cpuUrl = GlobalData.urlPrefix + "monitor/server/cpu.used_per/" + minsAgo + "/all";
		this.loadServersChart(container.down("#machineCPUChart"), 0, cpuUrl, "%", false,GlobalData.monValueThreshold);
		
		var memoryUrl = GlobalData.urlPrefix + "monitor/server/mem.used/" + minsAgo + "/all";
		this.loadServersChart(container.down("#machineMemoryChart"), 1, memoryUrl, "MBs", false, GlobalData.monValueThreshold);
		
		var netInUrl = GlobalData.urlPrefix + "monitor/server/net.in/" + minsAgo + "/all";
		this.loadServersChart(container.down("#machineNetInChart"), 2, netInUrl, "Kbs", false, GlobalData.netMonValueThreshold);
		
		var netOutUrl = GlobalData.urlPrefix + "monitor/server/net.out/" + minsAgo + "/all";
		this.loadServersChart(container.down("#machineNetOutChart"), 3, netOutUrl, "Kbs", false, GlobalData.netMonValueThreshold);
	},
	
	reloadServersStaticChartBtnClick: function(button, eOpts){
		this.reloadServersStaticCharts();
	},
	
	realtimeWindowClose: function(window, eOpts){
		this.clearInterval();
	},
	
	loadDiskMonitoring: function(serverId) {
		var url = GlobalData.urlPrefix + "monitor/server/diskmon";
		var grid = Ext.getCmp("diskItemGrid");
		grid.getStore().getProxy().url = url;
		grid.getStore().getProxy().extraParams = {"serverId":serverId};
		grid.getStore().load();
    },
	
	realtimeServerReloadChartBtnClick: function(button, eOpts){
		var container = button.up("window");
		var minsAgo = container.down("#realtimePeriod").getValue();
		var type = container.down("#chartTypeHiddenField").getValue();
		this.reloadRealTimeChart(container, type, minsAgo);
	},
	
	reloadRealTimeChart: function(container, type, minsAgo){
		var url = "";
		var unit = "";
		var chart = null;
		var monValueThreshold = GlobalData.monValueThreshold;
		var serverId = GlobalData.lastSelectedMenuId;
		var colorSeriesType = 0;
		if(type === GlobalData.SERVER_CPU_CHART){
			title = "Real time CPU Usage chart";
			colorSeriesType = 0;
			chart = container.down("#machineCPUChart");
			if(serverId > 0){
				url = GlobalData.urlPrefix + "monitor/server/cpu.used_per/" + serverId + "/" + minsAgo;
			} else {
				url = GlobalData.urlPrefix + "monitor/server/cpu.used_per/" + minsAgo + "/all";
			}
			
			unit = "%";
		} else if(type === GlobalData.SERVER_MEMORY_CHART){
			title = "Real time memory usage chart";
			chart = container.down("#machineMemoryChart");
			colorSeriesType = 1;
			if(serverId > 0){
				url = GlobalData.urlPrefix + "monitor/server/mem.used/" + serverId + "/" + minsAgo;
			} else {
				url = GlobalData.urlPrefix + "monitor/server/mem.used/" + minsAgo + "/all";
			}
			
			unit = "MBs";
		} else if(type === GlobalData.SERVER_NETIN_CHART){
			title = "Real time incomming network traffic chart";
			chart = container.down("#machineNetInChart");
			colorSeriesType = 2;
			if(serverId > 0){
				url = GlobalData.urlPrefix + "monitor/server/net.in/" + serverId + "/" + minsAgo;
			} else {
				url = GlobalData.urlPrefix + "monitor/server/net.in/" + minsAgo + "/all";
			}
			unit = "Kbs";
			monValueThreshold = GlobalData.netMonValueThreshold;
		} else if(type === GlobalData.SERVER_NETOUT_CHART){
			title = "Real time outgoing network traffic chart";
			chart = container.down("#machineNetOutChart");
			colorSeriesType = 3;
			if(serverId > 0){
				url = GlobalData.urlPrefix + "monitor/server/net.out/" + serverId + "/" + minsAgo;
			} else {
				url = GlobalData.urlPrefix + "monitor/server/net.out/" + minsAgo + "/all";
			}
			unit = "Kbs";
			monValueThreshold = GlobalData.netMonValueThreshold;
		}
		this.loadServersChart(chart, colorSeriesType, url, unit, true, monValueThreshold);
		chart.up("form").setVisible(true);
		if(serverId > 0){
			chart.legend.hide();
		}
	}
});
