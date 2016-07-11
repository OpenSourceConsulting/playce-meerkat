/*
 * File: app/view/MonitoringMachineNetOutChartForm.js
 */

Ext.define('webapp.view.MonitoringMachineNetOutChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.monitoringmachinenetoutchartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],

    itemId: 'monitoringMachineNetOutChartForm',
    bodyPadding: 10,
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'machineNetOutChart',
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