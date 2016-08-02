/*
 * File: app/view/DetailMonitoringMachineContainer.js
 */

Ext.define('webapp.view.DetailMonitoringMachineContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.detailmonitoringmachinecontainer',
	id:'DetailMonitoringMachineContainer',
	
    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.form.field.Display',
        'Ext.chart.CartesianChart',
        'Ext.chart.axis.Category',
        'Ext.chart.axis.Numeric',
        'Ext.chart.series.Line',
        'Ext.chart.Legend',
        'Ext.chart.series.Bar',
        'Ext.grid.Panel',
        'Ext.grid.column.Number',
        'Ext.grid.column.Date',
        'Ext.grid.column.Boolean',
        'Ext.view.Table',
        'Ext.toolbar.Toolbar',
		'webapp.store.MonitoringStore',
		'webapp.view.ServerInformationForm'
    ],
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
	
    items: [
		{
			xtype:'panel',
			title: '머신 모니터링',
			items: [
				{
                    xtype:'serverinformationform'
                }
			]
		},
		{
            xtype: 'panel',
			flex: 1,
			overflowY: 'scroll',
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
						padding: '5px',
						width:'450px'
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
							itemId:'reloadStaticChartBtn',
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
								webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_CPU_CHART);
							}
						}
					],
					items:[
						{
							xtype:'monitoringmachinecpuchartform'
						}
					]
                },
				{
					xtype: 'panel',
					title:'Memory usage (MBs)',
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_MEMORY_CHART);
							}
						}
					],
					items:[
						{
							xtype:'monitoringmachinememorychartform'
						}
					]
					
                },
				{
					xtype: 'panel',
					title:'Incomming network traffic (Kbs)',
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_NETIN_CHART);
							}
						}
					],
					items:[
						{
							xtype:'monitoringmachinenetinchartform'
						}
					]
					
                },
				{
					xtype: 'panel',
					title:'Outgoing network traffic (Kbs)',
					tools:[
						{
							tooltip: 'View realtime chart',
							tooltipType:'qtip',
							type: 'customType',
							cls: 'icon-live-update',
							handler: function(event, toolEl, panel){
								webapp.app.getController("ServerMonitoringController").showRealTimeChartWindow(GlobalData.SERVER_NETOUT_CHART);
							}
						}
					],
					items:[
						{
							xtype:'monitoringmachinenetoutchartform'
						}
					]
					
                },
				{
					colspan: 2,
					xtype: 'panel',
					title: 'Disk',
					layout: {
						type: 'fit'
					},
					items: [
						{
							xtype: 'gridpanel',
							id: 'diskItemGrid',
							store: 'ServerDiskMonitoringStore',
							emptyText:"No data.",
							columns: [
								{
									dataIndex: 'fsName',
									text: 'Filesystem',
									width: 140
								},
								{
									text: 'Total (kb)',
									xtype: 'numbercolumn',
									align: 'right',
									format:'0,000',
									dataIndex: 'total',
									width: 120
								},
								{
									xtype: 'widgetcolumn',
									dataIndex: 'reCalUsePer',
									text: 'Percentage',
									widget: {
										xtype: 'progressbarwidget',
										textTpl: '{percent}%'
									},
									width: 250
								},
								{
									dataIndex: 'used',
									xtype: 'numbercolumn',
									align: 'right',
									format:'0,000',
									text: 'Used (kb)',
									width: 120
								},
								{
									dataIndex: 'avail',
									xtype: 'numbercolumn',
									align: 'right',
									format:'0,000',
									text: 'Free (kb)',
									width: 120
								}
							]
						}
					]
					
				}
			]
        }
    ]
});