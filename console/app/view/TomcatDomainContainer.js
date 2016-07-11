/*
 * File: app/view/TomcatDomainContainer.js
 */

Ext.define('webapp.view.TomcatDomainContainer', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.tomcatdomaincontainer',
	id:'TomcatDomainContainer',
	itemId:'TomcatDomainContainer',
    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.toolbar.Toolbar'
    ],
    layout:'fit',
	title: '도메인 목록',
	bodyCls: 'osc-body',
	dockedItems: [
		{
			xtype: 'toolbar',
			dock: 'top',
			items: [
				{
					xtype: 'button',
					text: '도메인 생성',
					handler: function(){
						webapp.app.getController("DomainController").showDomainWindow("new", 0);
					}
				}
			]
		}
	],
    items: [
		{
            xtype: 'gridpanel',
            height: 450,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
			forceFit: true,
			store: 'DomainStore',
			dockedItems: [
				{
					xtype: 'pagingtoolbar',
					dock: 'bottom',
					width: 360,
					displayInfo: true,
					store: 'DomainStore'
				}
			],
			columns: [
				{
					xtype: 'gridcolumn',
					dataIndex: 'name',
					text: '도메인명'
				},
				{
					xtype: 'gridcolumn',
					dataIndex: 'datagridServerGroupName',
					text: 'Session server group'
				},
				{
					xtype: 'gridcolumn',
					dataIndex: 'tomcatInstancesCount',
					text: 'Tomcat instance 수'
				},
				{
					xtype: 'gridcolumn',
					dataIndex: 'applicationCount',
					text: 'Application 수'
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
								webapp.app.getController("DomainController").showDomainWindow("edit", record.get("id"));
							},
							iconCls: 'icon-edit',
							tooltip: '수정'
						},
						{
							handler: function(view, rowIndex, colIndex, item, e, record, row) {
								 webapp.app.getController("DomainController").deleteDomain(record.get("id"));
							},
							iconCls: 'icon-delete',
							tooltip: '삭제'
						}
					]
				}
			]
		}	
    ]

});