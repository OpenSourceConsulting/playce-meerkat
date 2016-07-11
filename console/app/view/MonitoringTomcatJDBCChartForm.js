/*
 * File: app/view/MonitoringTomcatJDBCChartForm.js
 */

Ext.define('webapp.view.MonitoringTomcatJDBCChartForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.tomcatjdbcchartform',

    requires: [
		'Ext.chart.CartesianChart'
    ],

    itemId: 'tomcatJdbcChartForm',
    bodyPadding: 10,
	layout: 'fit',
	height:300,
    items: [
		{
			xtype: 'cartesian',
			autoRender: true,
			itemId: 'tomcatJdbcChart',
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
					maximum: 10,
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
						fillStyle: '#B7C6BC',
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
							this.setTitle(storeItem.get('monDtString') + ': ' + storeItem.get('monValue') + " connection(s)");
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
							this.setTitle("Max value at "+storeItem.get('monDtString')  + ': ' + storeItem.get('monValue2') + " connection(s)");
						}
					}
				}
			]
		}
	]
});