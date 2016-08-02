/*
 * File: app/view/SessionServerGroupContainer.js
 */

Ext.define('webapp.view.SessionServerGroupContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.sessionservergroupcontainer',
	id: 'SessionServerGroupContainer',
    requires: [
        'webapp.view.ClusteringConfForm',
        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.view.Table',
        'Ext.toolbar.Paging',
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.grid.column.Boolean',
        'Ext.toolbar.Separator',
        'Ext.form.field.Text',
        'Ext.form.Panel'
    ],

    layout: 'border',
    items: [
        {
            xtype: 'gridpanel',
			title: '세션 그룹 목록',
            region: 'north',
            height: 400,
			emptyText:"No data.",
            id: 'dataGridServerGroupGrid',
            itemId: 'dataGridServerGroupGrid',
			split: true,
            forceFit: true,
            store: 'DatagridServerGroupStore',
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'name',
                    text: '세션 그룹명'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'typeNm',
                    text: '서버타입'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'serverNo',
                    text: '세션서버 수'
                },
				{
                    xtype: 'gridcolumn',
                    dataIndex: 'domainSize',
                    text: '사용 도메인 수'
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
								var domainNo = record.get('domainSize');
								if(domainNo > 0){
									Ext.Msg.show({
										title: "Message",
										msg: "Could not modify session server group which is associated to domain",
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.WARNING
									});
								} else {
										webapp.app.getController("DatagridServerController").showServerGroupWindow("edit", record.get("id"), true);
								}
							
							},
							iconCls: 'icon-edit',
							tooltip: 'Edit'
						},
						{
							handler: function(view, rowIndex, colIndex, item, e, record, row) {
								var domainNo = record.get('domainSize');
								if(domainNo > 0){
									Ext.Msg.show({
										title: "Message",
										msg: "Could not delete session server group which is associated to domain",
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.WARNING
									});
								} else {
										webapp.app.getController("DatagridServerController").deleteDatagridServerGroup(record.get("id"));
								}
								
							},
							iconCls: 'icon-delete',
							tooltip: 'Delete'
						}
					]
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
                },
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            text: '그룹 생성',
                            id: 'newServerGroupBtn'
                        }
                    ]
                }
            ]
        },
		{
			xtype: 'panel',
			flex: 7,
			region: 'center',
			items:[
				{
					xtype: 'panel',
					items:[
						{
							xtype: 'displayfield',
							value: 'Select a session server group above to see more information.',
							itemId: 'messageField',
							fieldStyle:
							{
								color: 'blue',
								'font-weight':'bold',
								'margin-left': '10px'
							}
						},
						{
							xtype: 'tabpanel',
							region: 'center',
							itemId: 'datagridGroupTab',
							id: 'datagridGroupTab',
							hidden: true,
							activeTab: 0,
							items: [
								{
									xtype: 'panel',
									itemId: 'datagridServerGridTab',
									title: '세션 서버 목록',
									layout: 'fit',
									items: [
										{
											xtype: 'gridpanel',
											id: 'datagirdServerGrid',
											forceFit: true,
											store: 'ServerStore',
											columns: [
												{
													//xtype: 'gridcolumn', // default is gridcolumn
													dataIndex: 'name',
													text: 'Server Name'
												},
												{
													dataIndex: 'sshIPAddr',
													text: 'IP Address'
												},
												{
													dataIndex: 'hostName',
													text: 'Hostname'
												},
												{
													dataIndex: 'port',
													text: 'Port'
												},
												/*{
													dataIndex: 'sessionNo',
													text: 'Sessions'
												},
												{
													dataIndex: 'status',
													text: 'Status'
												},*/
												{
													xtype: 'actioncolumn',
													sortable: true,
													dataIndex: 'id',
													menuDisabled: true,
													text: 'Actions',
													items: [
														/*{
															handler: function(view, rowIndex, colIndex, item, e, record, row) {
																var selectedRecords;
																selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
																var groupId = selectedRecords[0].get("id");
																webapp.app.getController("DatagridServerController").showServerGroupWindow("edit", groupId, false);
															},
															iconCls: 'icon-edit',
															tooltip: 'Edit'
														},*/
														{
															handler: function(view, rowIndex, colIndex, item, e, record, row) {
																var selectedRecords;
																selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
																var groupId = selectedRecords[0].get("id");
																var serverId = record.get("id");
																webapp.app.getController("DatagridServerController").removeDatagridServerFromGroup(groupId, serverId);
															},
															iconCls: 'icon-delete',
															tooltip: 'Remove'
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
															text: '세션서버 수정',
															id: 'linkNewServerBtn'
														/*},
														{
															xtype: 'tbseparator'
														},
														{
															xtype: 'textfield',
															fieldLabel: 'Filtering'*/
														}                                    ]
												},
												{
													xtype: 'pagingtoolbar',
													dock: 'bottom',
													store: 'ServerStore'
												}
											]
										}
									]
								},
								{
									xtype: 'panel',
									id: 'clusteringConfigServerGroupTab',
									layout: 'fit',
									hidden:true,
									title: 'Configuration',
									tabConfig: {
										xtype: 'tab',
										itemId: 'datagridClusteringConfTab'
									},
									dockedItems:[
										{
											xtype: 'toolbar',
											dock: 'top',
											items: [
												{
													xtype: 'button',
													itemId: 'btnNewClutersingConfiguration',
													text: 'New'
												},
												{
													xtype: 'tbseparator'
												},
												{
													xtype: 'textfield',
													itemId: 'clusteringConfKeywordField',
													fieldLabel: 'Filtering'
												},
												{
													xtype: 'tbseparator'
												},
												{
													xtype: 'combobox',
													itemId: 'clusteringConfVersionCombobox',
													fieldLabel: 'Version',
													editable: false,
													displayField: 'versionAndCreatedTime',
													store: 'ClusteringConfVersionStore',
													valueField: 'id'
												},
												{
													xtype: 'label',
													text: 'compare to'
												},
												{
													xtype: 'combobox',
													itemId: 'compareClusteringConfVersionCombobox',
													editable: false,
													displayField: 'versionAndCreatedTime',
													store: 'ClusteringConfVersionStore',
													valueField: 'id'
												},
												{
													xtype: 'button',
													itemId: 'cluseringConfComparisonBtn',
													text: 'Diff'
												}
											]
										}
									],
									items: [
										{
											xtype: 'clusteringconfform'
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