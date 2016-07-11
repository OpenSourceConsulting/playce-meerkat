/*
 * File: app/view/MonitoringMachineCPUChartForm.js
 */

Ext.define('webapp.view.MonitoringMachineCPUChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.monitoringmachinecpuchartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],

    itemId: 'monitoringMachineCPUChartForm',
    bodyPadding: 10,
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'machineCPUChart',
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
					fields: [
				
					],
					grid: true,
					maximum: 100,
					minimum: 0,
					position: 'left'
				}
			],
			series: []
		}
	]
});