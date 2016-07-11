/*
 * File: app/view/MonitoringMachineContainer.js
 */

Ext.define('webapp.view.MonitoringMachineContainer', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.monitoringmachinecontainer',
	id:'MonitoringMachineContainer',
    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.toolbar.Toolbar',
        'Ext.form.Label',
        'Ext.menu.Menu',
        'Ext.menu.Item',
        'Ext.chart.CartesianChart',
        'Ext.chart.axis.Category',
        'Ext.chart.axis.Numeric',
        'Ext.chart.series.Bar',
        'Ext.chart.Legend',
        'Ext.chart.series.Line',
		'webapp.view.MonitoringMachineCPUChartForm',
		'webapp.view.MonitoringMachineMemoryChartForm',
		'webapp.view.MonitoringMachineNetInChartForm',
		'webapp.view.MonitoringMachineNetOutChartForm'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
	bodyCls: 'osc-body',
	dockedItems:[
		{
			xtype: 'toolbar',
			flex: 1,
			dock: 'top',
			padding:'10 10 0 0',
			layout:{
				type: 'hbox',
				pack : 'end',
				defaultMargins: 5
			},
			items: [
				{
					xtype: 'combobox',
					itemId: 'realtimePeriod',
					store:'MonitoringPeriodStore',
					value: 30,
					mode: 'local',
					valueField:'idValue',
					displayField:'displayValue',
					editable:false,
					listeners: {
						render: function() {
							this.store.load();
						}
					}
				},
				{
					xtype: 'button',
					itemId:'reloadServersStaticChartBtn',
					iconCls:'fa fa-refresh',
					text: 'Reload'
				}
			]
		}
	],
    items: [
		{
            xtype: 'container',
            height: 30,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			padding : '0 10',
			defaults: {
				layout: {
					type: 'vbox',
					align: 'left',
					pack: 'end',
					padding : '0 10',
					margin: '0 10 0 0'
				},
				cls:'dash-title'
			},
			items:[
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: 'CPU usage (%)'
						}
					]
				},
				{
					xtype: 'container',
					items:[
						{
							xtype: 'toolbar',
							cls: 'osc-body',
							padding: 0,
							width: 80,
							items:[
								{
									iconCls: 'icon-preview',
									handler: function(){
										webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_CPU_CHART);
									},
									tooltip: 'View realtime chart'
								}
							]
						}
					]
				},
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: 'Memory usage (MBs)'
						}
					]
				},
				{
					xtype: 'container',
					items:[
						{
							xtype: 'toolbar',
							cls: 'osc-body',
							padding: 0,
							width: 80,
							items:[
								{
									iconCls: 'icon-preview',
									handler: function(event, toolEl, panel){
										webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_MEMORY_CHART);
									},
									tooltip: 'View realtime chart'
								}
							]
						}
					]
				}
			]
		},
        {
            xtype: 'container',
            flex: 1,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			margin: '0 0 0 0',
			padding: 10,
            items: [
				{	
					flex: 1,
					xtype:'monitoringmachinecpuchartform'
						
                },
				{
					flex: 1,
					xtype:'monitoringmachinememorychartform'
                }
			]
		},
		{
            xtype: 'container',
            height: 30,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			padding : '0 10',
			defaults: {
				layout: {
					type: 'vbox',
					align: 'left',
					pack: 'end',
					padding : '0 10',
					margin: '0 10 0 0'
				},
				cls:'dash-title'
			},
			items:[
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: 'Incomming network traffic (Kbs)'
						}
					]
				},
				{
					xtype: 'container',
					items:[
						{
							xtype: 'toolbar',
							cls: 'osc-body',
							padding: 0,
							width: 80,
							items:[
								{
									iconCls: 'icon-preview',
									handler: function(){
										webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_NETIN_CHART);
									},
									tooltip: 'View realtime chart'
								}
							]
						}
					]
				},
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: 'Outgoing network traffic (Kbs)'
						}
					]
				},
				{
					xtype: 'container',
					items:[
						{
							xtype: 'toolbar',
							cls: 'osc-body',
							padding: 0,
							width: 80,
							items:[
								{
									iconCls: 'icon-preview',
									handler: function(event, toolEl, panel){
										webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_NETOUT_CHART);
									},
									tooltip: 'View realtime chart'
								}
							]
						}
					]
				}
			]
		},
		{
            xtype: 'container',
            flex: 1,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			margin: '0 0 0 0',
			padding: 10,
			items: [
				{
					flex: 1,
					xtype:'monitoringmachinenetinchartform'
                },
				{
					flex: 1,
					xtype:'monitoringmachinenetoutchartform'	
                }
			]
        }
    ]

});