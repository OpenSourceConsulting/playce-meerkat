/*
 * File: app/view/DetailMonitoringTomcatInstance.js
 */

Ext.define('webapp.view.DetailMonitoringTomcatInstance', {
    extend: 'Ext.container.Container',
    alias: 'widget.detailmonitoringtomcatinstance',
	id:'DetailMonitoringTomcatInstance',
    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.view.Table',
		'webapp.store.MonitoringPeriodStore',
		'webapp.view.TomcatInstanceForm',
		'webapp.view.MonitoringTomcatHeapMemoryChartForm',
		'webapp.view.MonitoringTomcatCPUChartForm',
		'webapp.view.MonitoringTomcatThreadChartForm',
		'webapp.view.MonitoringTomcatJDBCChartForm',
		'webapp.store.DatasourceStore'
    ],

    scrollable: true,
    defaultListenerScope: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
		{
			flex: 0.8,
			xtype:'panel',
			title: '톰캣 인스턴스 모니터링',
			items: [
				{
                    xtype:'tomcatinstanceform'
                }
			]
		},
        {
			flex: 5,
            xtype: 'panel',
			scrollable: true,
			layout: {
				type: 'table',
				columns: 2,
				tableAttrs: {
					style: {
						width: '100%'
					}
				},
				tdAttrs: {
				    style: {
						padding: '05px',
						width:'500px'
					}
				}
			},
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
							itemId: 'staticPeriodMonitoringCombobox',
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
							itemId:'reloadChart',
							iconCls:'fa fa-refresh',
							text: 'Reload'
						}
					]
				}
			],
            items: [
				{	
					xtype: 'panel',
					title:'CPU usage (%)',
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("TomcatMonitoringController").showRealTimeChartWindow(GlobalData.JMX_TOMCAT_CPU_USAGE_CHART);
							}
						}
					],
					items:[
						{
							xtype:'tomcatcpuchartform'
						}
					]
                },
				{
					xtype: 'panel',
					title:'Heap memory usage (MBs)',
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("TomcatMonitoringController").showRealTimeChartWindow(GlobalData.JMX_TOMCAT_HEAP_MEMORY_CHART);
							}
						}
					],
					items:[
						{
							xtype:'tomcatheapmemorychartform'
						}
					]
					
                },
				{
					xtype: 'panel',
					title:'Active threads',
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("TomcatMonitoringController").showRealTimeChartWindow(GlobalData.JMX_TOMCAT_ACTIVE_THREADS_CHART);
							}
						}
					],
					items:[
						{
							xtype:'tomcatthreadchartform'
						}
					]
					
                },
				{
					xtype: 'panel',
					title:'JDBC Connections',
					header:{
						padding: '6 10',
						items:[{
							xtype:'combobox',
							itemId: 'datasourceCombobox',
							store: 'DatasourceStore',
							displayField: 'name',
							valueField:'id',
							editable:false
						}]    
					},
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("TomcatMonitoringController").showRealTimeChartWindow(GlobalData.JMX_TOMCAT_JDBC_CONNECTION_CHART);
							}
						}
					],
					items:[
						{
							xtype:'tomcatjdbcchartform'
						}
					]
					
                }
			]
        }
    ]
});