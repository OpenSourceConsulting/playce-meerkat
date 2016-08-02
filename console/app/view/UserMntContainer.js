/*
 * File: app/view/UserMntContainer.js
 */

Ext.define('webapp.view.UserMntContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.usermntcontainer',

    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Separator',
        'Ext.form.field.Text',
        'Ext.toolbar.Paging'
    ],

    itemId: 'mycontainer38',
    width: 1111,
    layout: 'fit',
    defaultListenerScope: true,

    items: [
        {
            xtype: 'tabpanel',
            activeTab: 0,
            items: [
                {
                    xtype: 'panel',
                    layout: 'fit',
                    title: 'User',
                    items: [
                        {
                            xtype: 'gridpanel',
							emptyText:"No data.",
                            forceFit: true,
                            store: 'UserStore',
                            columns: [
                                {
                                    //xtype: 'gridcolumn', //gridcolumn is default
                                    width: 80,
                                    dataIndex: 'username',
                                    hideable: false,
                                    text: 'UserID'
                                },
                                {
                                    width: 100,
                                    dataIndex: 'fullName',
                                    text: 'Full Name'
                                },
                                {
                                    dataIndex: 'userRolesString',
                                    text: 'User Roles',
									width: '25%'
                                },
                                {
                                    width: 100,
                                    dataIndex: 'email',
                                    text: 'Email'
                                },
                                {
                                    width: 100,
                                    dataIndex: 'createdDate',
                                    text: 'Created Date'
                                },
                                {
                                    width: 100,
                                    dataIndex: 'lastLoginDate',
                                    text: 'Last Login'
                                },
								{
                                   
                                    xtype: 'actioncolumn',
                                    sortable: true,
                                    dataIndex: 'id',
                                    menuDisabled: true,
									width: 80,
                                    text: 'Actions',
                                    items: [
										{
											handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                webapp.app.getController("UserController").showUserWindow("edit", record.get("id"));
                                            },
											iconCls: 'icon-edit',
                                            tooltip: 'Edit',
											margin: '0 10'
										},
										{
                                            handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                webapp.app.getController("UserController").deleteUser(record.get("id"));
                                            },
                                            iconCls: 'icon-delete',
                                            tooltip: 'Delete',
											margin: '0 10'
                                        }
                                    ]
                                
								}
                            ],
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'button',
                                            itemId: 'createBtn',
                                            text: 'New'
                                        },
                                        {
                                            xtype: 'tbseparator'
                                        },
                                        {
                                            xtype: 'textfield',
                                            itemId: 'mytextfield',
                                            fieldLabel: 'Filtering',
                                            name: 'SearchTextField',
                                            emptyText: 'User ID'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'toolbar',
                                    dock: 'bottom',
                                    items: [
                                        {
                                            xtype: 'pagingtoolbar',
                                            width: 1099,
                                            displayInfo: true,
                                            store: 'UserStore'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    layout: 'fit',
                    title: 'User Role',
					disabled: true,
                    items: [
                        {
                            xtype: 'gridpanel',
							emptyText:"No data.",
                            height: 349,
                            margin: '5 0 0 0 ',
                            forceFit: true,
                            store: 'UserRoleStore',
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'name',
                                    text: 'Name'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    width: 133,
                                    dataIndex: 'userCount',
                                    text: 'User Count'
                                },
								{
                                   
                                    xtype: 'actioncolumn',
                                    sortable: true,
                                    dataIndex: 'id',
                                    menuDisabled: true,
                                    text: 'Actions',
                                    items: [
										{
											handler: function(view, rowIndex, colIndex, item, e, record, row) {
												webapp.app.getController("UserController").showUserRoleWindow(record.get("id"),"edit");
                                            },
											iconCls: 'icon-edit',
                                            tooltip: 'Edit',
											margin: '0 10'
										},
										{
                                            handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                webapp.app.getController("UserController").deleteUserRole(record.get("id"));
                                            },
                                            iconCls: 'icon-delete',
                                            tooltip: 'Delete',
											margin: '0 10'
                                        }
                                    ]
                                
								}
                            ],
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'bottom',
                                    items: [
                                        {
                                            xtype: 'pagingtoolbar',
                                            width: 1099,
                                            displayInfo: true,
                                            store: 'UserRoleStore'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'button',
                                            id: 'userRoleCreateBtn',
                                            text: 'New'
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