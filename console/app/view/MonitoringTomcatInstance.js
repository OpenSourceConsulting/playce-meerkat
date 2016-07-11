/*
 * File: app/view/MonitoringTomcatInstance.js
 */

Ext.define('webapp.view.MonitoringTomcatInstance', {
    extend: 'Ext.container.Container',
    alias: 'widget.monitoringtomcatinstance',

    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.toolbar.Toolbar',
        'Ext.form.Label',
        'Ext.menu.Menu',
        'Ext.menu.Item',
        'Ext.chart.CartesianChart',
        'Ext.chart.axis.Category',
        'Ext.chart.axis.Numeric',
        'Ext.chart.series.Bar',
        'Ext.chart.Legend',
        'Ext.chart.series.Line'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'tabpanel',
            flex: 9.5,
            id: 'tabPanel2',
            itemId: 'tabPanel',
            activeTab: 0,
            items: [
                {
                    xtype: 'panel',
                    title: 'Availability',
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'label',
                                    text: 'Availability Hisotry in '
                                },
                                {
                                    xtype: 'button',
                                    text: 'Last 24 hours',
                                    menu: {
                                        xtype: 'menu',
                                        items: [
                                            {
                                                xtype: 'menuitem',
                                                text: 'Last 30 days',
                                                focusable: true
                                            }
                                        ]
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            dock: 'top',
                            layout: 'fit',
                            items: [
                                {
                                    xtype: 'cartesian',
                                    height: 250,
                                    width: 400,
                                    insetPadding: 20,
                                    store: 'TempoStore',
                                    axes: [
                                        {
                                            type: 'category',
                                            fields: [
                                                'x'
                                            ],
                                            position: 'left'
                                        },
                                        {
                                            type: 'numeric',
                                            fields: [
                                                'y1',
                                                'y2',
                                                'y3'
                                            ],
                                            title: 'Numeric Axis',
                                            position: 'bottom'
                                        }
                                    ],
                                    series: [
                                        {
                                            type: 'bar',
                                            xField: 'x',
                                            yField: [
                                                'y1',
                                                'y2',
                                                'y3'
                                            ]
                                        }
                                    ],
                                    legend: {
                                        xtype: 'legend'
                                    }
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    scrollable: true,
                    title: 'Performance',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            flex: 0.5,
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'label',
                                    text: 'Tracking in '
                                },
                                {
                                    xtype: 'button',
                                    text: 'Last 24 hours',
                                    menu: {
                                        xtype: 'menu',
                                        items: [
                                            {
                                                xtype: 'menuitem',
                                                text: 'Last 30 days',
                                                focusable: true
                                            }
                                        ]
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            flex: 4.5,
                            dock: 'top',
                            layout: {
                                type: 'hbox',
                                align: 'stretch'
                            },
                            items: [
                                {
                                    xtype: 'panel',
                                    flex: 1,
                                    margin: '5 5 5 5',
                                    width: 1226,
                                    layout: 'fit',
                                    title: 'Used Memory',
                                    titleAlign: 'center',
                                    dockedItems: [
                                        {
                                            xtype: 'cartesian',
                                            dock: 'top',
                                            height: 275,
                                            width: 400,
                                            insetPadding: 20,
                                            store: 'TempoStore',
                                            axes: [
                                                {
                                                    type: 'category',
                                                    fields: [
                                                        'x'
                                                    ],
                                                    title: 'Category Axis',
                                                    position: 'bottom'
                                                },
                                                {
                                                    type: 'numeric',
                                                    fields: [
                                                        'y'
                                                    ],
                                                    title: 'Numeric Axis',
                                                    position: 'bottom'
                                                }
                                            ],
                                            series: [
                                                {
                                                    type: 'line',
                                                    xField: 'x',
                                                    yField: [
                                                        'y'
                                                    ],
                                                    smooth: 3
                                                },
                                                {
                                                    type: 'line',
                                                    xField: 'x',
                                                    yField: [
                                                        'y'
                                                    ],
                                                    smooth: 3
                                                }
                                            ],
                                            legend: {
                                                xtype: 'legend'
                                            }
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    flex: 1,
                                    margin: '5 5 5 5',
                                    width: 1226,
                                    layout: 'fit',
                                    title: 'Average Processing Time',
                                    titleAlign: 'center',
                                    dockedItems: [
                                        {
                                            xtype: 'cartesian',
                                            dock: 'top',
                                            height: 276,
                                            width: 400,
                                            insetPadding: 20,
                                            store: 'TempoStore',
                                            axes: [
                                                {
                                                    type: 'category',
                                                    fields: [
                                                        'x'
                                                    ],
                                                    title: 'Category Axis',
                                                    position: 'bottom'
                                                },
                                                {
                                                    type: 'numeric',
                                                    fields: [
                                                        'y'
                                                    ],
                                                    title: 'Numeric Axis',
                                                    position: 'bottom'
                                                }
                                            ],
                                            series: [
                                                {
                                                    type: 'line',
                                                    xField: 'x',
                                                    yField: [
                                                        'y'
                                                    ],
                                                    smooth: 3
                                                },
                                                {
                                                    type: 'line',
                                                    xField: 'x',
                                                    yField: [
                                                        'y'
                                                    ],
                                                    smooth: 3
                                                }
                                            ],
                                            legend: {
                                                xtype: 'legend'
                                            }
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            flex: 5,
                            dock: 'top',
                            margin: '5 0 0 0',
                            width: 400,
                            collapsible: true,
                            manageHeight: false,
                            title: 'Health history',
                            layout: {
                                type: 'hbox',
                                align: 'stretch'
                            },
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'label',
                                            text: 'Filtering in'
                                        },
                                        {
                                            xtype: 'button',
                                            text: 'Last 24 hours',
                                            menu: {
                                                xtype: 'menu',
                                                items: [
                                                    {
                                                        xtype: 'menuitem',
                                                        text: 'Lst 30 days',
                                                        focusable: true
                                                    }
                                                ]
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]

});