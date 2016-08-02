/*
 * File: app/view/DomainContainer.js
 */
var MSG_TITLE_DOMAIN_DETAIL='도메인 상세';
var MSG_DOMAIN_NM='도메인명';
var MSG_SESSION_SVR_GRP='세션서버 그룹';
var MSG_TINSTANCE_NUM='톰캣 인스턴스 수';
var MSG_TINSTANCE_LIST='톰캣 인스턴스 목록';
var MSG_TINSTANCE_CONF='톰캣 인스턴스 설정';
var MSG_DS_CONF='데이타소스 설정';
var MSG_SESSION_SVR_CONF='세션서버 설정';

Ext.define('webapp.view.DomainContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.domaincontainer',
	id :"DomainContainer",
	itemId :"DomainContainer",
    requires: [
        'webapp.view.TomcatForm',
        'webapp.view.ConfigurationFileForm',
        'Ext.form.Panel',
        'Ext.form.FieldContainer',
        'Ext.form.field.Display',
        'Ext.form.field.Hidden',
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.toolbar.Separator',
        'Ext.form.field.Text',
        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.view.Table',
        'Ext.toolbar.Paging'
    ],
    layout: 'border',
    defaultListenerScope: true,

    items: [
        {
            xtype: 'form',
            region: 'north',
            id: 'domainViewForm',
            itemId: 'domainViewForm',
            bodyPadding: 10,
			bodyCls: ['osc-body', 'osc-vform'],
            title: MSG_TITLE_DOMAIN_DETAIL,
            
			method: 'GET',
            items: [
                {
                    xtype: 'fieldcontainer',
					padding: 0,
                    layout: 'hbox',
					fieldDefaults: {
						labelAlign: 'right',
						width: 180
					},
                    hideLabel: true,
                    items: [
						{
                            xtype: 'displayfield',
                            fieldLabel: '도메인 ID',
                            name: 'id',
							labelWidth: 70,
							width: 130
                        },
                        {
                            xtype: 'displayfield',
                            id: 'domainNameField',
                            fieldLabel: MSG_DOMAIN_NM,
                            name: 'name',
							labelWidth: 70,
							width: 220
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: MSG_SESSION_SVR_GRP,
                            name: 'datagridServerGroupName',
							renderer: function (value, field) {
								if(Ext.isEmpty(value)){
									return '<span style="color:#bdbdbd;">NONE</span>';
								}
								field.setWidth(260);
								return value;
							}
                        },
						{
                            xtype: 'displayfield',
                            fieldLabel: 'Scouter Agent',
                            name: 'scouterAgentInstallPath',
							width: 160,
							renderer: function (value, field) {
								if(Ext.isEmpty(value)){
									return '<span style="color:#bdbdbd;">NONE</span>';
								}

								return '<span style="color:blue;">installed</span>';
							}
                        },
						{
                            xtype: 'button',
							text: '설정조회',
							icon: 'resources/images/icons/fam/scouter.png',
							id: 'scouterConfigViewBtn',
							handler: function () { 
								Ext.create('widget.ScouterMngWindow').show();
							}
                        },
						{
                            xtype: 'displayfield',
							labelWidth: 150,
                            fieldLabel: MSG_TINSTANCE_NUM,
                            name: 'tomcatInstancesCount',
                            value: '0'
                        }
                    ]
                },
                {
                    xtype: 'hiddenfield',
                    name: 'isDefault',
					value: 'true'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    flex: 1,
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            id: 'mybutton68',
                            text: '수정',
							iconCls: 'icon-edit'
                        },
						{
                            xtype: 'button',
                            id: 'scouterInstallBtn',
                            text: 'Scouter Agent 설치',
							icon: 'resources/images/icons/fam/scouter.png',
							handler: function(){
								Ext.create('widget.ScouterMngWindow').show();
							}
                        },
						{
							xtype: 'label',
							itemId: 'domainWarnMsg',
							cls: 'osc-panel-tip',
							hidden: true
						}
                    ]
                }
            ]
        },
        {
            xtype: 'tabpanel',
            region: 'center',
            split: true,
            id: 'domainTabs',
            itemId: 'domainTabs',
            activeTab: 0,
            items: [
                {
                    //xtype: 'panel',// default is panel
                    id: 'domainTomcatTab',
                    itemId: 'domainTomcatTab',
                    layout: 'fit',
                    title: MSG_TINSTANCE_LIST,
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    id: 'btnNewTomcat',
                                    itemId: 'btnNewTomcat',
                                    iconCls: 'add',
                                    text: '톰캣 인스턴스 추가'
                                },
								{
									//xtype: 'splitbutton',// default xtype is 'button'
									text: 'Actions',
									id: 'dActionsMenu',
									iconCls: 'icon-menu',
									menu: [
									{
										id: 'dTomcatStart',
										text: '톰캣 시작',
										disabled: true
									},
									{
										id: 'dTomcatStop',
										text: '톰캣 중지',
										disabled: true
									},
									'-',
									{
										text:'JDBC Driver 설치',
										iconCls: 'fa fa-database',
										menu: [
											{
												text:'mysql-connector-java-5.1.38.jar 설치',
												id: 'installMySQLDriver',
												handler: function(){
													webapp.app.getController("DomainController").installJDBCDriverButtonClick('MySQL', 106);
												},
												iconCls: 'fa fa-database'
											},
											{
												text:'ojdbc14-10.2.0.4.0.jar 설치',
												handler: function(){
													webapp.app.getController("DomainController").installJDBCDriverButtonClick('Oracle', 118);
												},
												iconCls: 'fa fa-database'
											},
											{
												text:'파일 Upload 설치',
												handler: function(){
													Ext.create('widget.JDBCUploadWindow').show();
												}
											}
										]
									}
									/*'-',
									{
										text:'Test menu',
										handler: function(){
											Ext.create('widget.RealtimeChartExampleWindow').show();
										}
									}*/
									]
								},
                                {
                                    xtype: 'tbseparator'
                                },
                                {
                                    xtype: 'textfield',
                                    id: 'tomcatIntKeywordField',
                                    fieldLabel: 'Filtering'
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'associatedTomcatGridView',
                            forceFit: true,
							emptyText: "등록된 Tomcat Instance가 없습니다.",
                            store: 'TomcatInstanceListStore',
							selType: 'checkboxmodel',
                            columns: [
								{
                                    dataIndex: 'id',
                                    text: 'ID',
									width: 30
                                },
                                {
                                    //xtype: 'gridcolumn',// default is gridcolumn
                                    dataIndex: 'name',
                                    text: 'Tomcat instance'
                                },
                                {
                                    dataIndex: 'hostName',
                                    text: 'Host Name'
                                },
                                {
                                    dataIndex: 'ipaddress',
                                    text: 'IP Address'
                                },
								{
									text: 'Status',
									dataIndex: 'stateNm',
									renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
										var state = record.get('state');
										if (state == GlobalData.tomcatStateStarting) {
											metaData.tdCls = 'img-loading';
											return 'Starting';
										} else if (state == GlobalData.tomcatStateStopping) {
											metaData.tdCls = 'img-loading';
											return 'Stopping';
										} else if(state == GlobalData.tomcatStateRunning) {
											return '<span style="color:blue;">running</span>';
										} else if(state > 21) {
											return '<span style="color:red;">'+ value +'</span>';
										}
										return value;
									}
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
                                                webapp.app.getController("TomcatController").editTomcat(record.get('id'));
                                            },
                                            iconCls: 'icon-edit',
                                            tooltip: 'Edit',
											margin: '0 10'
                                        },
										{
                                            handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                webapp.app.getController("TomcatController").uninstallTomcat(record.get('id'), GlobalData.lastSelectedMenuId);
                                            },
                                            iconCls: 'icon-delete',
                                            tooltip: 'Delete',
											margin: '0 10',
											isDisabled : function(view, rowIndex, colIndex, item, record){
												return record.get("state") != GlobalData.tomcatStateStop && record.get("state") < 22;
											}
                                        },
										{
                                            handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                MUtils.showTaskWindow(record.get('lastTaskHistoryId'));
                                            },
                                            iconCls: 'icon-search',
                                            tooltip: '마지막 작업 로그 보기',
											margin: '0 10'
										}
                                    ]
                                }
                            ],
                            viewConfig: {
                                id: 'associatedTomcatListView'
                            },
                            dockedItems: [
                                {
                                    xtype: 'pagingtoolbar',
                                    dock: 'bottom',
                                    width: 360,
                                    displayInfo: true,
                                    store: 'TomcatInstanceListStore'
                                }
                            ]
                        }
                    ],
                    listeners: {
                        activate: 'onDomainTomcatTabActivate'
                    }
                },
                {
                    xtype: 'panel',
                    id: 'domainTomcatConfigTab',
                    itemId: 'domainTomcatConfigTab',
                    layout: 'fit',
                    title: MSG_TINSTANCE_CONF,
                    tabConfig: {
                        xtype: 'tab',
                        id: 'tomcatTabConfig1'
                    },
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    id: 'editTomcatConfigBtn',
                                    itemId: 'editTomcatConfigBtn',
                                    text: '수정하기'
                                },
                                {
                                    xtype: 'button',
                                    hidden: true,
                                    id: 'saveTomcatConfigBtn',
                                    itemId: 'saveTomcatConfigBtn',
                                    text: 'Save'
                                },
								{
                                    xtype: 'button',
                                    hidden: true,
                                    itemId: 'cancelTomcatConfigBtn',
                                    text: 'Cancel'
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                            xtype: 'tomcatform'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'domainTomcatTab2',
                    itemId: 'domainTomcatTab2',
                    layout: 'fit',
                    title: MSG_DS_CONF,
                    tabConfig: {
                        xtype: 'tab',
                        id: 'tomcatTabConfig2'
                    },
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    id: 'tomcatAddDSBtn',
                                    itemId: 'tomcatAddDSBtn',
                                    iconCls: 'add',
                                    text: 'Add'
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'domainDatasourceGrid',
							emptyText: 'No data.',
                            title: '',
                            forceFit: true,
                            store: 'DomainDatasourceStore',
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'name',
                                    text: 'Datasource name',
									width: 30
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'jdbcUrl',
                                    text: 'JDBC Url'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'dbTypeName',
                                    text: 'Database type',
									width: 20
                                },
                                {
                                    xtype: 'actioncolumn',
                                    dataIndex: 'Id',
                                    text: 'Action',
									width: 30,
                                    items: [
                                        {
                                            icon: 'resources/images/icons/fam/delete.gif',
                                            tooltip: 'Delete',
											handler: function(view, rowIndex, colIndex, item, e, record, row) {
												Ext.MessageBox.confirm('Confirm', '톰캣 서버에도 설정이 제거됩니다. 제거하시겠습니까??', function(btn){
													if(btn == "yes"){
														var domainId = GlobalData.lastSelectedMenuId;
														var dsId = record.get("id");
														webapp.app.getController("DomainController").deleteTomcatDatasource(domainId, dsId);
														/*
														var url = GlobalData.urlPrefix + "domain/datasource/delete";
														webapp.app.getController("globalController").ajaxRequest(url,{"domainId":domainId, "dsId": dsId}, "POST", function(json){
															  view.up("grid").getStore().reload();
														}, null);
														*/
													}
												});
                                            }
                                        }
                                    ]
                                }
                            ],
                            viewConfig: {
                                id: 'associatedTomcatListView2'
                            },
                            dockedItems: [
                                {
                                    xtype: 'pagingtoolbar',
                                    dock: 'bottom',
                                    id: 'domainDatasourcePaging',
                                    width: 360,
                                    displayInfo: true,
                                    store: 'DomainDatasourceStore'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'domainTomcatServerXmlTab',
                    itemId: 'domainTomcatServerXmlTab',
                    layout: 'fit',
                    title: 'Server.xml',
                    tabConfig: {
                        xtype: 'tab',
                        id: 'tomcatTabConfig3'
                    },
                    items: [
                        {
                            xtype: 'configurationfileform'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'domainTomcatContextXmlTab',
                    itemId: 'domainTomcatContextXmlTab',
                    layout: 'fit',
                    title: 'Context.xml',
                    tabConfig: {
                        xtype: 'tab',
                        id: 'tomcatTabConfig4'
                    },
                    items: [
                        {
                            xtype: 'configurationfileform'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    layout: 'fit',
                    title: 'Applications',
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'associatedApplicationListView',
                            store: 'ApplicationStore',
							forceFit:true,
							emptyText: "등록된 application이 없습니다.",
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'contextPath',
                                    text: 'Context path',
									width: 200
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'version',
                                    text: 'Version'
                                },
                                /*{
                                    xtype: 'gridcolumn',
                                    dataIndex: 'state',
                                    text: 'Status'
                                },*/
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'deployedTime',
                                    text: 'Deploy time',
									width: 160
                                },
								{
									xtype: 'actioncolumn',
									text: 'Actions',
									items: [
										{
                                            icon: 'resources/images/icons/fam/delete.gif',
                                            tooltip: 'Undeploy',
											handler: function(view, rowIndex, colIndex, item, e, record, row) {
												Ext.MessageBox.confirm('Confirm', '배포된 application 이 톰캣서버에서도 제거됩니다. 제거하시겠습니까?', function(btn){
													if(btn == "yes"){
														var appId = record.get("id");
														webapp.app.getController("ApplicationController").undeployApp(appId);
													}
												});
                                            }
                                        },
										{
                                            handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                MUtils.showTaskWindow(record.get('taskHistoryId'));
                                            },
                                            iconCls: 'icon-search',
                                            tooltip: '최근 작업 로그',
											margin: '0 10'
										}
									]
								}
                            ],
                            viewConfig: {
                                listeners: {
                                    itemclick: 'onViewItemClick'
                                }
                            },
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'button',
                                            id: 'btnApplicationDeploy',
                                            text: 'Deploy'
                                        },
                                        {
                                            xtype: 'button',
                                            disabled: true,
                                            hidden: true,
                                            id: 'btnApplicationStart',
                                            text: 'Start'
                                        },
                                        {
                                            xtype: 'button',
                                            disabled: true,
                                            hidden: true,
                                            id: 'btnApplicationRestart',
                                            text: 'Restart'
                                        },
                                        {
                                            xtype: 'button',
                                            disabled: true,
                                            hidden: true,
                                            id: 'btnApplicationStop',
                                            text: 'Stop'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'pagingtoolbar',
                                    dock: 'bottom',
                                    displayInfo: true,
                                    store: 'ApplicationStore'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    layout: 'fit',
					itemId: 'sessionTab',
                    title: 'Sessions',
                    items: [
                        {
                            xtype: 'gridpanel',
                            id: 'domainSessionGridView',
							emptyText: "생성된 세션이 없습니다.",
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
                                    dataIndex: 'location',
                                    text: 'Location'
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
                                                Ext.create('widget.sessionsWin', {sessionKey: record.get('key')}).show();
                                            },
											iconCls: 'icon-search',
                                            tooltip: 'session data 보기'
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
                                            xtype: 'textfield',
                                            fieldLabel: 'Filtering'
                                        }
                                    ]
                                },
								{
									xtype: 'pagingtoolbar',
									dock: 'bottom',
									displayInfo: true,
									store: 'SessionStore'
								}
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    id: 'clusteringConfigTab',
					hidden: true,
                    itemId: 'clusteringConfigTab',
                    layout: 'fit',
                    title: MSG_SESSION_SVR_CONF,
                    items: [
                        {
                            xtype: 'clusteringconfform'
                        }
                    ]
                },
				{
                    xtype: 'panel',
                    layout: 'fit',
					itemId: 'logTab',
                    title: '작업 이력',
                    items: [
                        {
							xtype: 'gridpanel',
							flex: 1,
							border: 1,
							forceFit: true,
							emptyText: "No data.",
							store: 'TaskHistoryStore',
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
									dataIndex: 'name',
									text: 'Tomcat Instance'
								},
								{
									xtype: 'gridcolumn',
									dataIndex: 'ipaddress',
									text: 'IP 주소'
								},
								{
									xtype: 'gridcolumn',
									dataIndex: 'finishedTime',
									text: '작업일시',
									width: 50
								},
								{
									text: '작업상태',
									dataIndex: 'status',
									width: 50,
									renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
										
										if (Ext.isEmpty(record.get('ipaddress') )) {
											return '';
										}else if (value == 0) {
											return '작업 대기중';
										} else if (value == 1) {
											metaData.tdCls = 'img-loading';
											return '<span style="color: blue;">작업중...</span>';
										} else if (value == 2) {
											metaData.tdCls = 'img-success';
											return '<span style="color: green;">작업 완료</span>';
										} else if (value == 3) {
											metaData.tdCls = 'img-fail';
											return '<span style="color: red;">작업 실패</span>';
										}
										return value;
									}
								},
								{
									xtype: 'actioncolumn',
									text: '로그',
									items: [
										{
                                            handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                                webapp.app.getController("DomainController").showViewLogWindow(record.get('taskDetailId'));
                                            },
                                            iconCls: 'icon-search',
                                            tooltip: '로그 조회',
											margin: '0 10',
											isDisabled : function(view, rowIndex, colIndex, item, record){
												return record.get("status") == 0;
											}
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
                                            xtype: 'textfield',
											itemId: 'logFilteringTextField',
                                            fieldLabel: 'Filtering'
                                        }
                                    ]
                                },
								{
									xtype: 'pagingtoolbar',
									dock: 'bottom',
									displayInfo: true,
									store: 'TaskHistoryStore'
								}
                            ]
						}
                    ]
                },
				{
                    xtype: 'panel',
                    itemId: 'alertTab',
                    layout: 'fit',
                    title: 'Alert 설정',
                    tabConfig: {
                        xtype: 'tab'
                    },
                    items: [
						{
							xtype:'alertSettingGridPanel'
						}
                    ]
                }
            ]
        }
    ],
	
    onDomainTomcatTabActivate: function(component, eOpts) {
        var tabpanel = component.up('tabpanel');
        tabpanel.fireEvent('tabchange', tabpanel, component);// refer DomainController
    },

    onViewItemClick: function(dataview, record, item, index, e, eOpts) {

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