/*
 * File: app/view/LinkNewDataSourceWindow.js
 *
 * This file was generated by Sencha Architect version 3.2.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('webapp.view.LinkNewDataSourceWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.LinkNewDataSourceWindow',

    requires: [
        'Ext.form.field.Display',
        'Ext.grid.Panel',
        'Ext.grid.column.Number',
        'Ext.grid.column.Date',
        'Ext.grid.column.Boolean',
        'Ext.grid.View',
        'Ext.form.field.Checkbox',
        'Ext.button.Button'
    ],

    height: 288,
    width: 574,
    title: 'Link to new Datasource',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'container',
                    flex: 2,
                    dock: 'top',
                    margin: '10 0 0 10',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'displayfield',
                            width: 213,
                            fieldLabel: 'Tomcat instance',
                            value: 'Tomcat 1'
                        },
                        {
                            xtype: 'displayfield',
                            margin: '0 10 0 100',
                            width: 148,
                            fieldLabel: 'Status:',
                            value: 'Started'
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'gridpanel',
                    flex: 6,
                    margin: '5 5 5 5',
                    title: '',
                    forceFit: true,
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'string',
                            text: 'Select'
                        },
                        {
                            xtype: 'numbercolumn',
                            dataIndex: 'number',
                            text: 'Data source Name'
                        },
                        {
                            xtype: 'datecolumn',
                            dataIndex: 'date',
                            text: 'JDBC URL'
                        },
                        {
                            xtype: 'booleancolumn',
                            dataIndex: 'bool',
                            text: 'Server No'
                        }
                    ]
                },
                {
                    xtype: 'checkboxfield',
                    flex: 0.5,
                    margin: '5 5 5 5',
                    fieldLabel: '',
                    boxLabel: 'Restart tomcat instance after linking new datasource.'
                },
                {
                    xtype: 'container',
                    flex: 1.5,
                    margin: '10 5 5 5',
                    items: [
                        {
                            xtype: 'button',
                            margin: '0 0 0 220',
                            text: 'OK'
                        },
                        {
                            xtype: 'button',
                            margin: '0 0 0 10',
                            text: 'Cancel'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});