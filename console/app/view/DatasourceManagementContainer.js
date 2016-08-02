/*
 * File: app/view/DatasourceManagementContainer.js
 */

Ext.define('webapp.view.DatasourceManagementContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.datasourcemanagementcontainer',
	id:'ResDataSourceContainer',
	itemId:'ResDataSourceContainer',
    requires: [
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Separator',
        'Ext.toolbar.Paging',
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.form.Panel',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden',
        'Ext.form.field.Checkbox',
        'Ext.form.FieldSet',
        'Ext.form.Label'
    ],
    layout: 'border',
    defaultListenerScope: true,

    items: [
        {
            xtype: 'gridpanel',
			emptyText:"No data.",
            region: 'north',
            split: true,
            height: 300,
            id: 'datasourceList',
            title: '데이타소스 목록',
            forceFit: true,
            store: 'DatasourceStore',
            columns: [
                {
                    dataIndex: 'name',
                    text: 'Name',
					width: 20
                },
                {
                    dataIndex: 'jdbcUrl',
                    text: 'JDBC URL'
                },
                {
                    dataIndex: 'tomcatInstancesNo',
					width: 20,
                    text: '사용중 서버 수'
                },
				{
                    xtype: 'actioncolumn',
                    dataIndex: 'id',
                    text: 'Action',
					width: 20,
					items: [
					{
						iconCls:"icon-delete",
						tooltip: "Delete",
						handler: function(view, rowIndex, colIndex, item, e, record, row) {
							webapp.app.getController("DatasourceController").deleteDS(record.get("id"));
						}
					}]
				}
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            itemId: 'btnLinkNewDatasource',
                            text: '생성'
                        },
                        {
                            xtype: 'tbseparator'
                        },
                        {
                            xtype: 'textfield',
                            itemId: 'dsKeywordField',
                            fieldLabel: 'Filtering'
                        }
                    ]
                },
                {
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    displayInfo: true,
                    store: 'DatasourceStore'
                }
            ]
        },
		{
			xtype: 'panel',
			region: 'center',
			layout: 'card',
			id: 'datasourceDetailPanel',
			items:[
				{
					xtype: 'panel',
					items:[
						{
							xtype: 'displayfield',
							value: 'Select a datasource above to see more information.',
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
					hidden: true,
					id: 'dsDetailTab',
					activeTab: 0,
					items: [
						{
							xtype: 'panel',
							layout: 'fit',
							title: '상세정보',
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											xtype: 'button',
											id: 'dsConnectionTestBtn',
											text: '접속 테스트'
										},
										{
											xtype: 'button',
											id: 'dsModifiedBtn',
											itemId: 'dsModifiedBtn',
											text: '수정'
										}
									]
								}
							],
							items: [
								{
									xtype: 'container',
									layout: {
										type: 'hbox'
										//align: 'stretch'
									},
									items: [
										{
											xtype: 'form',
											//flex: 1,
											//height: 120,
											width: 600,
											bodyPadding: 10,
											margin: '0 20 0 0',
											items: [
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'dsNameField',
													fieldLabel: 'Datasource name',
													labelWidth: 120,
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'combobox',
													anchor: '100%',
													id: 'dsDBTypeCombobox',
													autoLoadOnValue: true,
													fieldLabel: 'Database 유형 :',
													labelWidth: 120,
													editable: false,
													displayField: 'codeNm',
													store: 'DBTypeStore',
													valueField: 'id'
												},
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'dsJDBCUrlField',
													fieldLabel: 'JDBC Url',
													labelWidth: 120,
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'dsUsernameField',
													fieldLabel: 'Username',
													labelWidth: 120,
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'hiddenfield',
													anchor: '100%',
													id: 'dsIDField',
													fieldLabel: 'Label'
												},
												{
													xtype: 'fieldcontainer',
													layout: {
														type: 'hbox',
														align: 'stretch'
													},
													items: [
														{
															xtype: 'textfield',
															flex: 2,
															id: 'dsPasswordField',
															fieldLabel: '비밀번호',
															labelWidth: 120,
															inputType: 'password',
															allowBlank: false,
															allowOnlyWhitespace: false
														},
														{
															xtype: 'checkboxfield',
															flex: 1,
															margin: '0 0 0 10',
															fieldLabel: '',
															boxLabel: 'Show password',
															listeners: {
																change: 'onCheckboxfieldChange'
															}
														}
													]
												}
											]
										},
										{
											xtype: 'fieldset',
											//flex: 1,
											title: 'Connection pool',
											padding: 10,
											items: [
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'dsMaxConnectionField',
													fieldLabel: '최대 연결 개수 ',
													labelWidth: 120,
													inputType: 'number',
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'dsMaxConnectionPoolField',
													fieldLabel: '최대 연결 Pool 개수',
													labelWidth: 120,
													inputType: 'number',
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'textfield',
													anchor: '100%',
													id: 'dsMinConnectionPoolField',
													fieldLabel: '최소 연결 Pool 개수',
													labelWidth: 120,
													inputType: 'number',
													allowBlank: false,
													allowOnlyWhitespace: false
												},
												{
													xtype: 'container',
													layout: {
														type: 'hbox',
														align: 'stretch'
													},
													items: [
														{
															xtype: 'textfield',
															id: 'dsTimeoutField',
															fieldLabel: '최대 연결 대기 시간',
															labelWidth: 120,
															inputType: 'number',
															allowBlank: false,
															allowOnlyWhitespace: false
														},
														{
															xtype: 'label',
															text: '(ms)'
														}
													]
												}
											]
										}
									]
								}
							]
						},
						{
							xtype: 'gridpanel',
							id: 'dsTomcatInstanceGrid',
							title: '사용중 서버',
							forceFit: true,
							emptyText:"No data.",
							store: 'TomcatInstanceListStore',
							columns: [
								{
									xtype: 'gridcolumn',
									dataIndex: 'name',
									text: 'Instance name'
								},
								{
									xtype: 'gridcolumn',
									dataIndex: 'ipaddress',
									text: 'IP Address'
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											xtype: 'label',
											text: '아래 서버에 대해 DataSource 가 적용되었습니다. '
										}
									]
								}
							]
						}
					]
				}
			]
		}
	],

    onCheckboxfieldChange: function(field, newValue, oldValue, eOpts) {
        var passwordField = Ext.getCmp("dsPasswordField");
        if(!newValue){
            passwordField.inputEl.dom.setAttribute('type', 'password');
            passwordField.inputEl.addCls('x-form-password');
        }
        else{
            passwordField.inputEl.dom.setAttribute('type', 'text');
            passwordField.inputEl.addCls('x-form-text');
            passwordField.inputEl.removeCls('x-form-password');
        }
    }

});