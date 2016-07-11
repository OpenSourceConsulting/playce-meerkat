/*
 * File: app/view/MonitoringMachineNetInChartForm.js
 */

Ext.define('webapp.view.MonitoringMachineNetInChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.monitoringmachinenetinchartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],

    itemId: 'monitoringMachineNetInChartForm',
    bodyPadding: 10,
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'machineNetInChart',
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
					maximum: 500,
					minimum: 0,
					position: 'left'
				}
			],
			series: []
		}
	]
});