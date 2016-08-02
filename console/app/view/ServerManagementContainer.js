/*
 * File: app/view/ServerManagementContainer.js
 */

Ext.define('webapp.view.ServerManagementContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.servermanagementcontainer',
	id: 'ServerManagementContainer',
    requires: [
        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.view.Table',
        'Ext.toolbar.Paging',
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.form.Panel',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Number',
        'Ext.form.field.Display',
        'Ext.form.field.Hidden',
		'webapp.view.AlertSettingGridPanel'
    ],
    itemId: 'ServerManagementContainer',
    layout: 'border',
    defaultListenerScope: true,

    items: [
        {
            xtype: 'gridpanel',
            region: 'north',
            split: true,
            height: 450,
            id: 'serverGrid',
			itemId: 'serverGrid',
            title: 'Servers',
			scrollable: true,
            forceFit: true,
            store: 'ServerStore',
			emptyText:"No data.",
            columns: [
				{
                    //xtype: 'gridcolumn',//default is gridcolumn
                    dataIndex: 'id',
                    text: 'ID',
					width: 40
                },
                {
                    //xtype: 'gridcolumn',//default is gridcolumn
                    dataIndex: 'name',
                    text: 'Server name'
                },
				{
                    dataIndex: 'hostName',
                    text: 'Host Name'
                },
				{
                    dataIndex: 'sshIPAddr',
                    text: 'IP Address'
                },
                {
                    dataIndex: 'osName',
                    text: 'Operating System'
                },
				{
                    dataIndex: 'tomcatInstanceNo',
                    text: 'Tomcat Inst No'
                },
				{
                    dataIndex: 'sessionServerNo',
                    text: 'Session Server No'
                },
				{
                    dataIndex: 'runningAgent',
                    text: 'Agent 상태',
					width: 60,
					renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
						if(record.get('agentInstalled') == false){
							return '<span style="color: red;">not installed</span>';
						}else if(value == true){
							return '<span style="color: blue;">running</span>';
						}
						return 'stop';
					}
                },
				{
					text: 'Agent 설치',
					width: 50,
					dataIndex: 'agentInstalled',
					bubbleEvents: ['click'],
					renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
						var btnName = (value)? '재설치': '설치';
						var id = Ext.id();
						Ext.defer(function () {
							Ext.widget('button', {
								renderTo: id,
								text:  btnName,
								width: '90%',
								handler: function () { 
									webapp.app.getController("ServerManagementController").installAgent(record.get('id'), value);
								}
							});
						}, 50);
						return Ext.String.format('<div id="{0}"></div>', id);
					}
				},
                {
                    xtype: 'actioncolumn',
                    dataIndex: 'id',
                    text: 'Action',
					items: [
					{
						handler: function(view, rowIndex, colIndex, item, e, record, row) {
							var tomcatInstNo = record.get('tomcatInstanceNo');
							var sessionServerNo = record.get('sessionServerNo');
							if(tomcatInstNo > 0){
								Ext.Msg.show({
									title: "Message",
									msg: "삭제할수 없습니다. 설치된 Tomcat instance가 존재합니다.",
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.WARNING
								});
							} else if (sessionServerNo > 0){
								
								if(sessionServerNo > 0){
									Ext.Msg.show({
										title: "Message",
										msg: "삭제할수 없습니다. 설치된 Session server 가 존재합니다.",
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.WARNING
									});
								}
							} else {
								webapp.app.getController("ServerManagementController").deleteServer(record.get('id'));	
							}
							
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
                            id: 'newServerBtn',
                            text: '서버 등록'
                        },
						{
							xtype:'tbseparator'
						},
						{
							xtype: 'textfield',
							itemId:'keywordTextField',
							fieldLabel: 'Filtering',
							name: 'keywordTextField',
							emptyText: 'Name'
						}
                    ]
                },
                {
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    width: 1125,
                    displayInfo: true,
                    store: 'ServerStore'
                }
            ]
        },
		{
			xtype: 'panel',
			region: 'center',
			layout: 'card',
			id: 'serverDetailPanel',
			items:[
						{
							xtype: 'panel',
							items:[
								{
									xtype: 'displayfield',
									value: 'Select a server above to see more information.',
									itemId: 'messageField',
									fieldStyle:
									{
										color: 'blue',
										'font-weight':'bold',
										'margin-left': '10px'
									}
								}
							]
						},
						{
							xtype: 'tabpanel',
							id: 'detailServerTab',
							hidden: true,
							items: [
								{
									xtype: 'panel',
									scrollable: true,
									itemId: 'infoTab',
									title: 'Information',
									items: [
										{
											xtype: 'form',
											itemId: 'infoForm',
											height: 228,
											bodyPadding: 10,
											items: [
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'serverNameTextField',
													name:'name',
													fieldLabel: 'Server name',
													readOnly: true,
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'serverHostNameTextField',
													name:'hostName',
													fieldLabel: 'Host name',
													readOnly: true
												},
												{
													xtype: 'combobox',
													anchor: '100%',
													id: 'serverSSHIPAddressCombobox',
													name:'sshNi',
													fieldLabel: 'SSH IPAddress',
													readOnly: true,
													editable: false,
													displayField: 'ipv4',
													store: 'NetworkInterfaceStore',
													valueField: 'id'
												},
												{
													xtype: 'numberfield',
													anchor: '100%',
													id: 'serverSSHPortTextField',
													fieldLabel: 'SSH Port',
													name:'sshPort',
													readOnly: true,
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'displayfield',
													anchor: '100%',
													id: 'serverOSNameDisplayField',
													fieldLabel: 'OS Name',
													name: 'osName'
												},
												{
													xtype: 'hiddenfield',
													id: 'serverIDHiddenField_',
													fieldLabel: 'Label',
													name:'id'
												}
											],
											dockedItems:[
												{
													xtype:'toolbar',
													items: [
														{
															xtype: 'button',
															id: 'serverEditBtn',
															itemId: 'serverEditBtn',
															text: 'Edit'
														},
														{
															xtype: 'button',
															hidden: true,
															id: 'serverSaveBtn',
															margin: '0 10 0 0',
															text: 'Save'
														},
														{
															xtype: 'button',
															hidden: true,
															id: 'serverCancelBtn',
															text: 'Cancel'
														}
													]
												}
											]
										}
									]
								},
								{
									xtype: 'panel',
									scrollable: true,
									layout: 'fit',
									title: 'SSH Account',
									items: [
										{
											xtype: 'container',
											layout: {
												type: 'hbox',
												align: 'stretch'
											},
											items: [
												{
													xtype: 'gridpanel',
													flex: 2,
													emptyText:"No data.",
													height: 277,
													id: 'sshAccountGrid',
													name :'sshGrid',
													forceFit: true,
													sortableColumns: false,
													store: 'SSHAccountStore',
													columns: [
														{
															xtype: 'gridcolumn',
															align: 'center',
															dataIndex: 'username',
															text: 'SSH Accounts'
														}
													]
												},
												{
													xtype: 'panel',
													flex: 5,
													layout: 'vbox',
													items: [
														{
															xtype: 'button',
															itemId: 'serverAddSSHAccountBtn',
															margin: '50 0 0 5',
															text: '추가',
															hidden: true
														},
														{
															xtype: 'button',
															disabled: true,
															id: 'serverEditSSHAccountBtn',
															margin: 5,
															text: '비밀번호 변경'
														},
														{
															xtype: 'button',
															disabled: true,
															id: 'serverDeleteSSHAccountBtn',
															margin: 5,
															text: '삭제',
															hidden: true
														},
														{
															xtype: 'button',
															disabled: true,
															id: 'serverTestSSHAccountBtn',
															margin: 5,
															text: 'Test  connection'
														}
													]
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
									//tabConfig: {
									//	xtype: 'tab'
									//},
									items: [
										{
											xtype:'alertSettingGridPanel'
										}
									]
								}
							]
							/*,
							listeners: {
								tabchange: 'onDetailServerTabTabChange'
							}*/
						}
			]
		}
    ]
/*
    onDetailServerTabTabChange: function(tabPanel, newCard, oldCard, eOpts) {


        var activeTab = tabPanel.getActiveTab();
        var activeTabIndex = tabPanel.items.findIndex('id', activeTab.id);
        if(activeTabIndex === 2) { //env tab
            try{
                var selectedRecords;
                selectedRecords = Ext.getCmp('serverGrid').getSelectionModel().getSelection();
                var serverId = selectedRecords[0].get("id");

                //load EV revisions
                webapp.app.getController("ServerManagementController").loadEVRevisions(serverId, function(data){
                        Ext.getCmp("evRevisionComboBox").getStore().loadData(data);
                    });

                webapp.app.getController("ServerManagementController").loadEnvironmentVariables(serverId, function(data){
                    Ext.getCmp("environmentVariablesGridPanel").getStore().loadData(data);
                });
            }catch(e){

                Ext.Msg.show({
                    title: "Message",
                    msg: "Please choose tomcat server",
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.WARNING
                });
            }

        }
    }
*/
});