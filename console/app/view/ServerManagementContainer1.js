/*
 * File: app/view/ServerManagementContainer1.js
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

Ext.define('webapp.view.ServerManagementContainer1', {
    extend: 'Ext.container.Container',

    requires: [
        'Ext.button.Button',
        'Ext.grid.Panel',
        'Ext.grid.View',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Boolean',
        'Ext.toolbar.Separator',
        'Ext.form.field.Text'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    flex: 9,
                    height: 597,
                    items: [
                        {
                            xtype: 'panel',
                            title: 'Datagrid Servers',
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'button',
                                            text: 'New Group'
                                        }
                                    ]
                                }
                            ],
                            items: [
                                {
                                    xtype: 'gridpanel',
                                    id: 'datagridServerGroupGrid1',
                                    title: 'Datagrid Server Group',
                                    forceFit: true,
                                    store: 'DatagridServerGroupStore',
                                    columns: [
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'name',
                                            text: 'Name'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'type',
                                            text: 'Type'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'serverNo',
                                            text: 'Servers'
                                        }
                                    ],
                                    dockedItems: [
                                        {
                                            xtype: 'pagingtoolbar',
                                            dock: 'bottom',
                                            id: 'datagridServerGroupPaging1',
                                            width: 360,
                                            displayInfo: true,
                                            store: 'DatagridServerGroupStore'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    title: 'Detail'
                                },
                                {
                                    xtype: 'gridpanel',
                                    id: 'datagirdServerGrid1',
                                    title: '',
                                    forceFit: true,
                                    store: 'DatagridServerStore',
                                    columns: [
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'serverName',
                                            text: 'Server Name'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'groupName',
                                            text: 'Group'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'ipAddress',
                                            text: 'IP Address'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'hostName',
                                            text: 'Hostname'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'sessions',
                                            text: 'Sessions'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'status',
                                            text: 'Status'
                                        },
                                        {
                                            xtype: 'booleancolumn',
                                            dataIndex: 'bool',
                                            text: 'Actions'
                                        }
                                    ],
                                    dockedItems: [
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            items: [
                                                {
                                                    xtype: 'button',
                                                    text: 'Add'
                                                },
                                                {
                                                    xtype: 'tbseparator'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    fieldLabel: 'Filtering'
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    listeners: {
                        tabchange: {
                            fn: me.onTabpanelTabChange,
                            scope: me
                        }
                    }
                }
            ],
            listeners: {
                activate: {
                    fn: me.onContainerActivate,
                    scope: me
                }
            }
        });

        me.callParent(arguments);
    },

    onTabpanelTabChange: function() {
        var activeTab = tabPanel.getActiveTab();
        var activeTabIndex = tabPanel.items.findIndex('id', activeTab.id);
        if(activeTabIndex === 0) {//tomcat server tab
            webapp.app.getController("ServerManagementController").loadTomcatServers(function(data){
                Ext.getCmp("tomcatServerGrid").getStore().loadData(data);
            });
        }
        else if(activeTabIndex === 1) {//datagrid server tab
            Ext.getCmp("datagridServerGroupGrid").getStore().reload();
        }

    },

    onContainerActivate: function(component, eOpts) {
         webapp.app.getController("ServerManagementController").loadTomcatServers(function(data){
                Ext.getCmp("tomcatServerGrid").getStore().loadData(data);
            });
    }

});