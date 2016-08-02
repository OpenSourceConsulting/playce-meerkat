/*
 * File: app/view/JMXMonitoringChartForm.js
 */

Ext.define('webapp.view.JMXMonitoringChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.jmxmonitoringchartform',
    requires: [
		'Ext.chart.CartesianChart'
    ],
    itemId: 'jmxMonitoringChartForm',
    bodyPadding: 10,
	layout: 'fit',
	title:'JMX Monitoring chart',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'tomcatJdbcChart',
			store: {type:'MonitoringStore'},
			theme: 'Yellow',
			axes: [
				{
					type: 'category',
					fields: [
						'monDtString'
					],
					grid: true,
					label: {
						rotate: {
							degrees: -45
						}
					},
					title: 'Time',
					position: 'bottom'
				},
				{
					type: 'numeric',
					fields: [
						'monValue'
					],
					grid: true,
					maximum: 20,
					minimum: 0,
					position: 'left'
				}
			],
			series: [
				{
					type: 'line',
					axis: 'left',
					xField: 'monDtString',
					yField: 'monValue',
					style: {
						lineWidth: 2,
						fillStyle: '#23ad43',
						strokeStyle: '#115fa6',
						fillOpacity: 0.6,
						miterLimit: 3,
						lineCap: 'miter'
					},
					markerConfig: {
						radius: 4
					},
				
					tips: {
						trackMouse: true,
						style: 'background: yellow',
						height: 20,
						showDelay: 0,
						dismissDelay: 0,
						hideDelay: 0,
						renderer: function(storeItem, item) {
							this.setTitle(storeItem.get('monDtString') + ': ' + Ext.util.Format.number(storeItem.get('monValue'), "00.00"));
						}
					}
				}
			]
		}
	],
	
	loadChart: function(json, type){
		//config for specific type of chart
		var title = "";
		var fillChartColor = "";
		var unit = "";
		var defaultMaxValue = 100;
		var defaultMinValue = 0;
		var chart = this.down("cartesian");
		var valueThreshold = 0;
		
		if(type === GlobalData.JMX_TOMCAT_CPU_USAGE_CHART) {
			title = "CPU Usage (%)";
			fillChartColor = "#006699";
			unit = "%";
		} else if(type === GlobalData.JMX_TOMCAT_HEAP_MEMORY_CHART) {
			title = "Heap memory usage (MBs)";
			fillChartColor = "#23ad43";
			unit = "MBs";
			valueThreshold = GlobalData.heapMemoryValueThreshold;
		} else if(type === GlobalData.JMX_TOMCAT_ACTIVE_THREADS_CHART) {
			title = "Active threads";
			fillChartColor = "#23ad43";
			unit = "";
			defaultMaxValue = 20;
		}
		
		this.setTitle(title);
		
		chart.getAxes()[1].setMaximum(defaultMaxValue);
		chart.getSeries()[0].setStyle({fillStyle:fillChartColor});
		
		
		var list = [];
		Ext.Array.each(json, function(name, index, record) {
			list.push({"monDtString": json[index].monDtString, "monValue":json[index]["monValue"]});
		});
		
		var newMax = webapp.app.getController("globalController").getMonitoringMaxValue(list, "monValue");
		if(newMax >= defaultMaxValue) {
			chart.getAxes()[1].setMaximum(newMax + valueThreshold);
		}
	
		chart.getStore().loadData(list,false);
	}

});