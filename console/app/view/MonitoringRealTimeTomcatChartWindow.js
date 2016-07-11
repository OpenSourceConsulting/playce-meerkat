/*
 * File: app/view/MonitoringRealTimeTomcatChartWindow.js
 */

Ext.define('webapp.view.MonitoringRealTimeTomcatChartWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.MonitoringRealTimeTomcatChartWindow',
	itemId:'MonitoringRealTimeTomcatChartWindow',
    requires: [
    ],
    height: 500,
    width: 1024,
	modal: true,
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
					itemId:'realtimeReloadChart',
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
			xtype:'tomcatcpuchartform',
			hidden:true
		},
		{
			xtype:'tomcatheapmemorychartform',
			hidden:true
		},
		{
			xtype:'tomcatthreadchartform',
			hidden:true
		},
		{
			xtype:'tomcatjdbcchartform',
			hidden:true
		}
    ]

});