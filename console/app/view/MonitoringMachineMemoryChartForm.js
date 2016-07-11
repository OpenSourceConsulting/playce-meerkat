/*
 * File: app/view/MonitoringMachineMemoryChartForm.js
 */

Ext.define('webapp.view.MonitoringMachineMemoryChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.monitoringmachinememorychartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],

    itemId: 'monitoringMachineMemoryChartForm',
    bodyPadding: 10,
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'machineMemoryChart',
			store: {type:'MonitoringStore'},
			theme: 'Yellow',
			legend: {
                docked: 'right'
            },
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
					position: 'bottom'
				},
				{
					type: 'numeric',
					fields: [],
					grid: true,
					maximum: 1024,
					minimum: 0,
					position: 'left'
				}
			],
			series: []
		}
	]
});