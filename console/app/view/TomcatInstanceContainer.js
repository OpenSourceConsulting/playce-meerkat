/*
 * File: app/view/TomcatInstanceContainer.js
 *
 */
var MSG_TITLE_TINSTANCE_DETAIL='톰캣인스턴스 상세';
var MSG_TINSTANCE_CONF='톰캣 설정';
var MSG_DS_CONF='데이타소스 설정';

var MSG_DATASOURCE='데이터소스';

Ext.define('webapp.view.TomcatInstanceContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.tomcatinstancecontainer',

    requires: [
        'webapp.view.TomcatForm',
        'webapp.view.ConfigurationFileForm',
        'Ext.form.field.Display',
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.form.Panel',
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Paging',
        'Ext.form.field.Text'
    ],
    layout: 'border',
    defaultListenerScope: true,
	id:'TomcatInstanceContainerView',
	itemId:'TomcatInstanceContainerView',
    items: [
        {
            xtype: 'panel',
            region: 'north',
            height: 160,
            manageHeight: false,
            title: MSG_TITLE_TINSTANCE_DETAIL,
            layout: {
                type: 'fit'
            },
			id:'tomcatInstanceContainer',
            items: [
				{
					xtype: 'tomcatinstanceform'
				}
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            disabled: true,
                            id: 'btnTomcatStart',
                            text: '시작'
                        },
                        {
                            xtype: 'button',
                            id: 'btnTomcatStop',
                            text: '중지'
                        }
						/*
                        {
                            xtype: 'button',
                            id: 'btnTomcatRestart',
                            text: '재시작'
                        }
						*/
                    ]
                }
            ]
        },
        {
            xtype: 'tabpanel',
            flex: 7,
            region: 'center',
            id: 'tomcatTabs',
            activeTab: 0,
            split: true,
            items: [
                {
                    xtype: 'panel',
                    title: MSG_TINSTANCE_CONF,
                    items: [
                        {
                            xtype: 'tomcatform'
                        }
                    ],
					dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    itemId: 'btnTomcatConfEdit',
                                    iconCls: 'fa fa-edit',
                                    text: 'Edit'
                                },
								{
                                    xtype: 'button',
                                    itemId: 'btnTomcatConfSave',
									hidden: true,
                                    iconCls: 'fa fa-save',
                                    text: 'Save'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    title: MSG_DATASOURCE,
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'tomcatDatasourcesGrid',
                            margin: '5 0 0 0 ',
                            title: '',
                            forceFit: true,
                            store: 'DatasourceStore',
							emptyText: "No data.",
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'name',
                                    text: 'Name'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'jdbcUrl',
                                    text: 'JDBC URL'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'dbTypeName',
                                    text: 'Database type'
                                }
                            ]
                        }
                    ],
					dockedItems: [
						{
							xtype: 'pagingtoolbar',
							dock: 'bottom',
							id: 'tomcatDatasourcePaging',
							width: 360,
							displayInfo: true,
							store: 'DatasourceStore'
						}
					]
                },
                {
                    xtype: 'panel',
                    id: 'tomcatServerXmlTab',
                    layout: 'fit',
                    title: 'Server.xml',
                    items: [
                        {
                            xtype: 'configurationfileform'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'tomcatContextXmlTab',
                    layout: 'fit',
                    title: 'Context.xml',
                    items: [
                        {
                            xtype: 'configurationfileform'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    title: 'Applications',
					layout: 'fit',
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'tomcatApplicationGrid',
							forceFit:true,
							emptyText: "배포된 application이 없습니다.",
                            store: 'TomcatInstanceAppStore',
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'perm',
                                    text: 'Permission'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'own',
                                    text: 'Owner'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'date',
                                    text: 'Date'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'appName',
                                    text: 'App Name'
                                },
								{
									xtype: 'actioncolumn',
                                    dataIndex: 'id',
									width: 100,
                                    menuDisabled: true,
                                    text: 'Connect',
                                    items: [
										{
											handler: function(view, rowIndex, colIndex, item, e, record, row) {
												
												var topContainer = Ext.getCmp('TomcatInstanceContainerView');
												
												var ip = topContainer.queryById('tIpAddr').getValue();
												var port = topContainer.queryById('tHttpPort').getValue();
											
												var win = window.open('http://' + ip + ':' + port + '/' + record.get('appName'), '_blank');
												win.focus();
											},
											iconCls: 'icon-search',
											tooltip: 'Connect this application.'
										}
									]
								}
                            ],
                            viewConfig: {
                                listeners: {
                                    itemclick: 'onViewItemClick1'
                                }
                            },
                            dockedItems: [
								{
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'label',
                                            cls: 'osc-panel-tip',
											text: 'ls -al $CATALINA_BASE/webapps 의 결과입니다.'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'pagingtoolbar',
                                    dock: 'bottom',
                                    width: 360,
                                    displayInfo: true,
                                    store: 'TomcatInstanceAppStore'
                                }
                            ]
                        }
                    ]
                },
                /*{
                    xtype: 'panel',
                    title: 'Sessions',
                    dockedItems: [
                        {
                            xtype: 'gridpanel',
                            dock: 'top',
                            id: 'tomcatSessionGrid',
                            title: '',
                            forceFit: true,
                            store: 'SessionStore',
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'key',
                                    text: 'Key'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'value',
                                    text: 'Value'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'location',
                                    text: 'Location'
                                }
                            ],
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: 'Filtering'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            width: 360,
                            displayInfo: true,
                            store: 'SessionStore'
                        }
                    ]
                }*/
            ]
        }
    ],
    onViewItemClick1: function(dataview, record, item, index, e, eOpts) {

        var status = record.get("state");
        if(status === 1) { //started
            Ext.getCmp("btnApplicationStart").disable();
            Ext.getCmp("btnApplicationStop").enable();
            Ext.getCmp("btnApplicationRestart").enable();
            Ext.getCmp("btnApplicationUndeploy").disable();
        } else if(status  === 2) { //stopped
            Ext.getCmp("btnApplicationStart").enable();
            Ext.getCmp("btnApplicationStop").disable();
            Ext.getCmp("btnApplicationRestart").disable();
            Ext.getCmp("btnApplicationUndeploy").enable();
        }
    }

});