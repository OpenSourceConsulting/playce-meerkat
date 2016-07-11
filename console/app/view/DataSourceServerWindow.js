/*
 * File: app/view/DataSourceServerWindow.js
 */

Ext.define('webapp.view.DataSourceServerWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.datasourceserverwindow',

    requires: [
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.button.Button'
    ],
    height: 273,
    width: 517,
    title: 'Target Server',
	modal: true,
    items: [
        {
            xtype: 'container',
            margin: '10 10 10 10',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'gridpanel',
                    flex: 4,
                    title: 'Available Servers',
                    titleAlign: 'center',
                    forceFit: true,
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            align: 'center',
                            dataIndex: 'string',
                            text: 'Name'
                        },
                        {
                            xtype: 'gridcolumn',
                            align: 'center',
                            dataIndex: 'string',
                            text: 'IP Address'
                        }
                    ]
                },
                {
                    xtype: 'container',
                    flex: 1,
                    width: 100,
                    items: [
                        {
                            xtype: 'button',
                            margin: '30 0 10 10',
                            text: '&nbsp;>&nbsp;'
                        },
                        {
                            xtype: 'button',
                            margin: '0 0 10 10',
                            text: '>>'
                        },
                        {
                            xtype: 'button',
                            margin: '0 0 10 10',
                            text: '&nbsp;<&nbsp;'
                        },
                        {
                            xtype: 'button',
                            margin: '0 0 0 10',
                            text: '<<'
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    flex: 4,
                    title: 'Selected Servers',
                    titleAlign: 'center',
                    forceFit: true,
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            align: 'center',
                            dataIndex: 'string',
                            text: 'Name'
                        },
                        {
                            xtype: 'gridcolumn',
                            align: 'center',
                            dataIndex: 'string',
                            text: 'IP Address'
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'button',
            margin: '0 0 0 200',
            text: 'Save'
        },
        {
            xtype: 'button',
            margin: '0 0 0 10',
            text: 'Cancel'
        }
    ]
});