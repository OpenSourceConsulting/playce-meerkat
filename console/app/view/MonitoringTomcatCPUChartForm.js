/*
 * File: app/view/MonitoringTomcatCPUChartForm.js
 */

Ext.define('webapp.view.MonitoringTomcatCPUChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.tomcatcpuchartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],
	bodyPadding: 10,
    itemId: 'tomcatCPUChartForm',
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'cpuChart',
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
					position: 'bottom'
				},
				{
					type: 'numeric',
					fields: [
						'monValue'
					],
					grid: true,
					maximum: 100,
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
						fillStyle: '#006699',
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
							this.setTitle(storeItem.get('monDtString') + ': ' + Ext.util.Format.number(storeItem.get('monValue'), "00.00") + " (%)");
						}
					}
				}
			]
		}
	]
});