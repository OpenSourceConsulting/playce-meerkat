/*
 * File: app/controller/ServerMonitoringController.js
 */

Ext.define('webapp.controller.TomcatMonitoringController', {
    extend: 'Ext.app.Controller',
	requires:[
		'webapp.view.MonitoringRealTimeTomcatChartWindow'
	],
    control: {
		"#DetailMonitoringTomcatInstance": {
			deactivate: 'containerDeactivate',
			activate: 'containerActivate'
		},
		"#reloadChart":{
			click: 'onButtonReloadChartClick'	
		},
		"#realtimeReloadChart":{
			click: 'onButtonReloadRealtimeChartClick'	
		},
		"#MonitoringRealTimeTomcatChartWindow":{
			destroy: 'realtimeWindowClose'
		},
		"#datasourceCombobox":{
			select: 'datasourceComboboxSelect'
		}
    },
	
	containerActivate: function(container, opts){
		var me = this;
		var tomcatId = GlobalData.lastSelectedMenuId;
		//display tomcat config
		var form = container.down("#tomcatForm");
		webapp.app.getController("TomcatController").displayTomcatInstance(form, tomcatId, function(data){
			if(!data.jmxEnable){
				Ext.Msg.show({
					title: "Message",
					msg: "JMX 설정이 disable 상태이기때문에 수집된 JMX 모니터링 데이타가 없습니다.",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.INFO
				});
				return;
			}
		});
		
		//clear charts
		var charts = container.query("cartesian");
		Ext.each(charts, function(name, index, record){
			charts[index].getStore().loadData({});
			charts[index].redraw();
		});
		
		//load ds store
		var dsCombobox = container.down("#datasourceCombobox");
		var url = GlobalData.urlPrefix + "tomcat/instance/" + tomcatId + "/ds";
		dsCombobox.getStore().getProxy().url = url;
		dsCombobox.getStore().load({callback: function(){
			var recordSelected = dsCombobox.getStore().getAt(0);
			if(recordSelected !== null) {
				dsCombobox.setValue(recordSelected.get('id'));
				me.loadJMXChart(container, tomcatId, GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART, false, minsAgo, dsCombobox.getValue());
			}
		}});
		
		var minsAgo = container.down("#staticPeriodMonitoringCombobox").getValue();
		this.loadJMXChart(container, tomcatId, GlobalData.JMX_TOMCAT_CPU_USAGE_CHART, false, minsAgo);
		this.loadJMXChart(container, tomcatId, GlobalData.JMX_TOMCAT_HEAP_MEMORY_CHART, false, minsAgo);
		this.loadJMXChart(container, tomcatId, GlobalData.JMX_TOMCAT_ACTIVE_THREADS_CHART, false, minsAgo);
	},
	
	containerDeactivate: function(container, opts){
		this.clearInterval();
	},
	
	//dsId for only jdbc chart, othewise set null or dont pass this param
	loadJMXChart: function(container, tomcatId, type, isRealTime, minutesAgo, dsId){
		var me = this;
		var url = GlobalData.urlPrefix + "monitor/jmx/tomcat/"+ tomcatId;
		var form = null;
		var chart = null;
		var threshold = 0;
		var isSetMaxValue = false;
		var params = {};
		if(type === GlobalData.JMX_TOMCAT_CPU_USAGE_CHART){
			url += "/cpu";
			form = container.down("#tomcatCPUChartForm");
		} else if(type === GlobalData.JMX_TOMCAT_HEAP_MEMORY_CHART){
			url += "/memory";
			form = container.down("#heapMemoryChartForm");
			threshold = GlobalData.heapMemoryValueThreshold;
			isSetMaxValue = true;
		} else if(type === GlobalData.JMX_TOMCAT_ACTIVE_THREADS_CHART){
			url += "/threads";
			form = container.down("#tomcatThreadChartForm");
			threshold = GlobalData.activeThreadChartThreshold;
			isSetMaxValue = true;
		} else if(type === GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART){
			url += "/jdbc";
			form = container.down("#tomcatJdbcChartForm");
			isSetMaxValue = true;
			params = {"dsId":dsId};
		}
		
		url += "/" + minutesAgo;
		chart = form.down("cartesian");
		this.clearInterval();
		webapp.app.getController("globalController").ajaxRequest(url, params, "GET", function(json){
				me.renderChart(json.list, chart, threshold, isSetMaxValue);
				//hide message box when reload
				Ext.MessageBox.hide();
			}, null);
			
		if(isRealTime) {
			webapp.app.getController("globalController").realtimeAjaxRequest(5, url, params, "GET", function(json){
				me.renderChart(json.list, chart, threshold, isSetMaxValue);
			}, function(timeInterval){
				if(timeInterval > 0) {
					GlobalData.detailMonTomcatIntervalArray.push(timeInterval);
				}
			});
		}
	},
	
	renderChart: function(json, chart, threshold, isSetMaxValue){
		var maxValue = chart.getAxes()[1].getMaximum();
		var list = [];
		Ext.Array.each(json, function(name, index, record) {
			list.push({"monDtString": json[index].monDtString, "monValue":json[index]["monValue"],"monValue2":json[index]["monValue2"]});
		});
		if(isSetMaxValue){
			var newMax = webapp.app.getController("globalController").getMonitoringMaxValue(list, ["monValue2"]);
			if(newMax >= maxValue) {
				chart.getAxes()[1].setMaximum(newMax + threshold);
			}
		}
	
		chart.getStore().loadData(list, false);
	},
	
	clearInterval:function(){
		while(interval = GlobalData.detailMonTomcatIntervalArray.pop()){
			webapp.app.getController("globalController").stopRealtimeAjaxRequest(interval);
		}
	},
	
	showRealTimeChartWindow: function(type) {
		var window = Ext.create("widget.MonitoringRealTimeTomcatChartWindow");
		var title = "";
		var dsId = 0 ;//for only jdbc chart
		if(type === GlobalData.JMX_TOMCAT_CPU_USAGE_CHART){
			title = "Real time CPU Usage chart";
			form = window.down("#tomcatCPUChartForm");
		} else if(type === GlobalData.JMX_TOMCAT_HEAP_MEMORY_CHART){
			title = "Real time memory usage chart";
			form = window.down("#heapMemoryChartForm");
			threshold = GlobalData.heapMemoryValueThreshold
		} else if(type === GlobalData.JMX_TOMCAT_ACTIVE_THREADS_CHART){
			title = "Real time active thread chart";
			form = window.down("#tomcatThreadChartForm");
			threshold = GlobalData.activeThreadChartThreshold;
		} else if(type === GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART){
			var container = Ext.getCmp("DetailMonitoringTomcatInstance");
			dsId = container.down("#datasourceCombobox").getValue();
			var dsName = container.down("#datasourceCombobox").getRawValue();
			title = "Real time jdbc connections chart of " + dsName;
			form = window.down("#tomcatJdbcChartForm");
		}
		
		var minsAgo = window.down("#realtimePeriod").getValue();
		window.down("#chartTypeHiddenField").setValue(type);
		this.loadJMXChart(window, GlobalData.lastSelectedMenuId, type, true, minsAgo, dsId);
		form.setVisible(true);
		window.setTitle(title);
		window.show();
	},
	
	onButtonReloadChartClick: function(button, eOpts){
		var container = Ext.getCmp("DetailMonitoringTomcatInstance");
		Ext.MessageBox.show({
			msg: 'Loading ...',
			progressText: 'Loading...',
			width:300,
			wait:true,
			waitConfig: {interval:200}
		});
		
		var minsAgo = container.down("#staticPeriodMonitoringCombobox").getValue();
		var dsId = container.down("#datasourceCombobox").getValue();
		this.loadJMXChart(container, GlobalData.lastSelectedMenuId, GlobalData.JMX_TOMCAT_CPU_USAGE_CHART, false, minsAgo);
		this.loadJMXChart(container, GlobalData.lastSelectedMenuId, GlobalData.JMX_TOMCAT_HEAP_MEMORY_CHART, false, minsAgo);
		this.loadJMXChart(container, GlobalData.lastSelectedMenuId, GlobalData.JMX_TOMCAT_ACTIVE_THREADS_CHART, false, minsAgo);
		if(dsId > 0){
			this.loadJMXChart(container, GlobalData.lastSelectedMenuId, GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART, false, minsAgo, dsId);
		}
	},
	
	realtimeWindowClose: function(window, eOpts){
		this.clearInterval();
	},
	
	onButtonReloadRealtimeChartClick: function(button, eOpts) {
		var container = Ext.getCmp("DetailMonitoringTomcatInstance");
		var dsId = 0;
		Ext.MessageBox.show({
			msg: 'Loading ...',
			progressText: 'Loading...',
			width:300,
			wait:true,
			waitConfig: {interval:200}
		});
		var minsAgo = button.up("window").down("#realtimePeriod").getValue();
		var type  = button.up("window").down("#chartTypeHiddenField").getValue();
		if(type == GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART) {
			dsId = container.down("#datasourceCombobox").getValue();
		}
		
		this.loadJMXChart( button.up("window"), GlobalData.lastSelectedMenuId, type, true, minsAgo, dsId);
	},
	
	datasourceComboboxSelect: function(combo, record, eOpts){
		var dsId = combo.getValue();
		var container = Ext.getCmp("DetailMonitoringTomcatInstance");
		var minsAgo = container.down("#staticPeriodMonitoringCombobox").getValue();
		var tomcatId = GlobalData.lastSelectedMenuId;
		if(tomcatId > 0){ //prevent when init view
			this.loadJMXChart(container, tomcatId, GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART, false, minsAgo, dsId);
		}
	}

});
