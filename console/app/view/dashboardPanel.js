/*
 * File: app/view/dashboardPanel.js
 */

var MSG_DATASOURCE='데이터소스';
var MSG_DOMAIN='도메인';
var MSG_ACTIVITY_HISTORY='작업 이력';
var MSG_NUMBER_OF_INSTANCES_OF_DOMAIN='도메인별 인스턴스 수';
var MSG_NUMBER_OF_CONNECTIONS_OF_DATASOURCE='데이타소스별 DB connection 수';
var MSG_ALERT_TOP_10='중요 경고 10개';
var MSG_CPU='CPU';

Ext.define('webapp.view.dashboardPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.dashboardpanel',

    requires: [
        'Ext.chart.CartesianChart',
		'Ext.layout.container.Center'
    ],
	
    layout: {
    	type: 'vbox',
        align: 'stretch'
    },
	bodyCls: 'osc-body',
    itemId: 'dashboardPanel',
	id: 'dashboardPanel',
	scrollable: true,
    items: [
		{
            xtype: 'container',
            height: 80,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			margin: '0 0 0 0',
			cls: 'osc-body',
			defaults: {
				border: true,
				layout: 'center',
				bodyPadding: 10,
				bodyStyle: 'background:#fff; border-left-style: none; border-color: #ececec !important;',
				header: false,
				shadow: 'drop',
				shadowOffset: 5
			},
			items: [
				{
					xtype: 'panel',
					flex: 1,
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'vbox',
								align: 'stretch',
								pack: 'center'
							},
							bodyStyle: 'background:#fff;',
							width: '60%',
							height: '80%',
							items: [
								{
									xtype: 'label',
									cls: 'stats-label1',
									text: '5',
									itemId:'domainNoDisplayField',
									margin: '0 0 5 0'
								},
								{
									xtype: 'label',
									cls: 'stats-label2',
									text: 'Domains'
								}
							]
						}
					]
				},
				{
					xtype: 'panel',
					flex: 1,
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'vbox',
								align: 'stretch',
								pack: 'center'
							},
							bodyStyle: 'background:#fff;',
							width: '60%',
							height: '80%',
							items: [
								{
									xtype: 'label',
									cls: 'stats-label1',
									itemId:'tomcatInstNoDisplayField',
									text: '13',
									margin: '0 0 5 0'
								},
								{
									xtype: 'label',
									cls: 'stats-label2',
									text: 'Tomcat Instances'
								}
							]
						}
					]
				},
				{
					xtype: 'panel',
					flex: 1,
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'vbox',
								align: 'stretch',
								pack: 'center'
							},
							bodyStyle: 'background:#fff;',
							width: '60%',
							height: '80%',
							items: [
								{
									xtype: 'label',
									cls: 'stats-label1',
									itemId:'serverNoDisplayField',
									text: '11',
									margin: '0 0 5 0'
								},
								{
									xtype: 'label',
									cls: 'stats-label2',
									text: 'Server Machines'
								}
							]
						}
					]
				},
				{
					xtype: 'panel',
					flex: 1,
					items: [
						{
							xtype: 'panel',
							layout: {
								type: 'vbox',
								align: 'stretch',
								pack: 'center'
							},
							bodyStyle: 'background:#fff;',
							width: '60%',
							height: '80%',
							items: [
								{
									xtype: 'label',
									cls: 'stats-label1',
									itemId:'dsNoDisplayField',
									text: '4',
									margin: '0 0 5 0'
								},
								{
									xtype: 'label',
									cls: 'stats-label2',
									text: 'DataSources'
								}
							]
						}
					]
				}
			]
		},
		{
            xtype: 'container',
            height: 30,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			padding : '0 10',
			defaults: {
				layout: {
					type: 'vbox',
					align: 'left',
					pack: 'end',
					padding : '0 10',
					margin: '0 10 0 0'
				},
				cls:'dash-title'
			},
			items:[
				{
					xtype: 'container',
					flex: 2,
					items:[
						{
							xtype: 'label',
							text: MSG_CPU
						}
					]
				},
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: MSG_ALERT_TOP_10
						}
					]
				}
			]
		},
		{
            xtype: 'container',
            flex: 1,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			margin: '0 0 0 0',
			padding: 10,
			items: [
				{	
					flex:2,
					xtype:'monitoringmachinecpuchartform'
                },
				{
					xtype: 'panel',
					header: false,
					border: 1,
					flex: 1,
					bodyStyle: 'background:#fff;',
					margin: '0 0 0 10',
					overflow: "scroll",
					items: [
						{
							xtype: 'gridpanel',
							itemId: 'alertGrid',
							emptyText: "No items.",
							forceFit: true,
							store: 'TopCPUMonitorStore',
							columns: [
								{
									xtype: 'gridcolumn',
									dataIndex: 'name',
									text: 'Target name',
									width: 80
								},
								{
									xtype: 'gridcolumn',
									dataIndex: 'type',
									text: 'Type',
									width: 55
								},
								{
									xtype: 'widgetcolumn',
									dataIndex: 'decimalMonValue',
									text: 'Utilization',
									widget: {
										xtype: 'progressbarwidget',
										textTpl: '{value:percent}'
									}
								},
								{
									xtype: 'gridcolumn',
									dataIndex: 'alertStatus',
									text: 'Alert',
									width: 25,
									align: 'center',
									renderer: function (value, metaData, record, row, col, store, gridView) {
										
										if(value){
											return '<i class="fa fa-exclamation-triangle" aria-hidden="true" style="color:red"></i>';
										}
										return '<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>';
									}
								}
							],
							viewConfig: {
								id: 'topCpuGridView1'
							}
						}
					]
				}
			]
		},
		{
            xtype: 'container',
            height: 30,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			padding : '0 10',
			defaults: {
				layout: {
					type: 'vbox',
					align: 'left',
					pack: 'end',
					padding : '0 10',
					margin: '0 10 0 0'
				},
				cls:'dash-title'
			},
			items:[
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: MSG_DOMAIN
						}
					]
				},
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: MSG_DATASOURCE
						}
					]
				},
				{
					xtype: 'container',
					flex: 1,
					items:[
						{
							xtype: 'label',
							text: MSG_ACTIVITY_HISTORY
						}
					]
				}
			]
		},
		{
            xtype: 'container',
            flex: 1,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			cls: 'osc-body',
			margin: '0 0 0 0',
			padding: 10,
			items: [{
					xtype: 'polar',
					flex: 1,
					margin: '0 10 0 0',
					background: '#fff',
					insetPadding: 15,
					innerPadding: 20,
					/*legend: {
						docked: 'bottom'
					},*/
					store: 'DomainStatsStore',
					itemId: 'domainStatsChart',
					interactions: ['rotatePie3d', 'itemhighlight'],
					sprites: [{
						type: 'text',
						text: MSG_NUMBER_OF_INSTANCES_OF_DOMAIN,
						x: 12,
						y: 300
					}],
					series: [{
						type: 'pie',
						angleField: 'tomcatInstancesCount',
						donut: 50,
						label: {
							field: 'name',
							display: 'outside'
						},
						highlight: true,
						tooltip: {
							trackMouse: true,
							style: 'background: #fff',
							renderer: function(storeItem, item) {
								this.setHtml(storeItem.get('name') + ': ' + storeItem.get('tomcatInstancesCount') + " (tomcat instances)");
							}
						}
					}]
				},
				{
					xtype: 'cartesian',
					itemId:'jdbcConnStatsChart',
					flex: 1,
					interactions: {
						type: 'panzoom',
						zoomOnPanGesture: true
					},
					
					store: 'DatasourceConnStore',
					theme: {
						type: 'muted'
					},
					margin: '0 10 0 0',
					insetPadding: '10 10 20 10',
					innerPadding: '0 60 0 3',
					axes: [{
						type: 'numeric3d',
						fields: 'monValue',
						position: 'left',
						grid: true,
						minimum: 0,
						renderer: function (v, layoutContext) {
							// Custom renderer overrides the native axis label renderer.
							// Since we don't want to do anything fancy with the value
							// ourselves except appending a '%' sign, but at the same time
							// don't want to loose the formatting done by the native renderer,
							// we let the native renderer process the value first.
							//return layoutContext.renderer(v) + '%';
							return layoutContext.renderer(v);
						}
					}, {
						type: 'category',
						fields: 'dsName',
						position: 'bottom',
						grid: true,
						label: {
							rotate: {
								degrees: -45
							}
						},
						visibleRange: [0, 0.75]
					}],
					series: [{
						type: 'bar3d',
						xField: 'dsName',
						yField: 'monValue',
						label: {
							field: 'monValue',
							display: 'over'
						},
						highlight: {
							fillStyle: 'rgba(43, 130, 186, 1.0)',
							strokeStyle: 'white',
							showStroke: true,
							lineWidth: 2
						},
						tooltip: {
							trackMouse: true,
							style: 'background: #fff',
							showDelay: 0,
							dismissDelay: 0,
							hideDelay: 0,
							renderer: function (record, item) {
								this.setHtml(record.get('dsName') + ': ' + record.get('monValue') + ' (connections)');
							}
						},
						renderer: (function () {

							return function (sprite, config, data, index) {
								return {
									fillStyle: GlobalData.seriesColors[index  % GlobalData.seriesColors.length],
									strokeStyle: index % 2 ? 'none' : 'black',
									opacity: index % 2 ? 1 : 0.5
								};
							};
						})()

					}],
					sprites: [{
						type: 'text',
						text: MSG_NUMBER_OF_CONNECTIONS_OF_DATASOURCE,
						x: 12,
						y: 320
					}]
				},
				{
					xtype: 'gridpanel',
					flex: 1,
					border: 1,
					overflow: "scroll",
					forceFit: true,
					emptyText: "No items.",
					store: 'TaskHistoryStore',
					itemId: 'taskHistoryGrid',
					columns: [
						{
							xtype: 'gridcolumn',
							dataIndex: 'username',
							text: '관리자명',
							width: 30
						},
						{
							xtype: 'gridcolumn',
							dataIndex: 'taskName',
							text: '작업명'
						},
						{
							xtype: 'gridcolumn',
							dataIndex: 'targetName',
							text: 'Target Name'
						},
						{
							xtype: 'gridcolumn',
							dataIndex: 'finishedTime',
							text: '작업일시',
							width: 50
						}
					]
				}
			]
		}
    ]
});