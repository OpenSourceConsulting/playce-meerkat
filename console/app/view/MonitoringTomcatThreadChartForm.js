/*
 * File: app/view/MonitoringTomcatThreadChartForm.js
 */

Ext.define('webapp.view.MonitoringTomcatThreadChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.tomcatthreadchartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],

    itemId: 'tomcatThreadChartForm',
    bodyPadding: 10,
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'tomcatThreadChart',
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
						'monValue',
						'monValue2'
					],
					grid: true,
					maximum: 20,
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
						fillStyle: '#68B2E3',
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
							this.setTitle(storeItem.get('monDtString') + ': ' + storeItem.get('monValue') + " thread(s)");
						}
					}
				},
				{
					//max value
					type: 'line',
					axis: 'left',
					xField: 'monDtString',
					yField: 'monValue2',
					style: {
						lineWidth: 2,
						strokeStyle: 'red',
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
							this.setTitle("Max value at "+storeItem.get('monDtString')  + ': ' + storeItem.get('monValue2') + " thread(s)");
						}
					}
				}
			]
		}
	]
});