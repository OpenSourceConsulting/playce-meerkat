/*
 * File: app/view/MonitoringRealTimeServerChartWindow.js
 */

Ext.define('webapp.view.MonitoringRealTimeServerChartWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.MonitoringRealTimeServerChartWindow',
	itemId:'monitoringRealTimeServerChartWindow',
    requires: [
    ],
	modal: true,
    height: 500,
    width: 1024,
	title: 'Realtime chart',
	layout: 'fit',
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
					itemId:'realtimeServerReloadChartBtn',
					iconCls:'fa fa-refresh',
					text: 'Reload'
				}
			]
		}
	],
    items: [
		{
			xtype:'hiddenfield',
			itemId:'chartTypeHiddenField'
		},
        {
			xtype:'monitoringmachinecpuchartform',
			hidden:true
		},
		{
			xtype:'monitoringmachinememorychartform',
			hidden:true
		},
		{
			xtype:'monitoringmachinenetinchartform',
			hidden:true
		},
		{
			xtype:'monitoringmachinenetoutchartform',
			hidden:true
		}
    ]

});