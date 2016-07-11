/*
 * File: app/view/NewMenuPanel.js
 */

var MSG_DASHBOARD='대시보드';
var MSG_TOMCAT_MANAGEMENT='톰캣 관리';
var MSG_MONITORING='모니터링';
var MSG_RESOURCE_MANAGEMENT='리소스 관리';
var MSG_LOG_MANAGEMENT='로그 관리';
var MSG_USER_MANAGEMENT='사용자 관리';
Ext.define('webapp.view.NewMenuPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.leftMenuPanel',
    requires: [
        'Ext.tree.Panel',
        'Ext.tree.View'
    ],
    cls: 'osc-menu',
    height: 362,
    width: 250,
    layout: 'accordion',
    title: 'Menu',
    defaultListenerScope: true,
    items: [
        {
            xtype: 'panel',
            collapsed: true,
            hideCollapseTool: true,
			id: 'dashMenuPanel',
            iconCls: 'icon-list1',
            title: MSG_DASHBOARD,
            listeners: {
                beforeexpand: 'onPanelBeforeExpand'
            }
        },
        {
            xtype: 'treepanel',
            componentCls: 'menuTree',
            id: 'menuTreePanel',
            collapseFirst: false,
            collapsed: false,
            iconCls: 'icon-tomcat',
            title: MSG_TOMCAT_MANAGEMENT,
            store: new Ext.data.TreeStore({
                proxy: {
                    type: 'ajax'
                },
				root: {
					id: 'tomcatMngRoot',
					expanded: true,
					children: [
						{
							text: '도메인 및 톰캣 구성',
							id: 'create-wizard',
							leaf: true
						},
						{
							text: '톰캣 도메인',
							id: 'tomcatMng'
						}
					]
				}
			
            }),
            rootVisible: false
			
        },
        {
            xtype: 'treepanel',
			id:'monitoringTreePanel',
            componentCls: 'menuTree',
            collapseFirst: false,
            collapsed: false,
            iconCls: 'fa fa-bar-chart',
            title: MSG_MONITORING,
			store: new Ext.data.TreeStore({
                proxy: {
                    type: 'ajax'
                },
				root: {
					expanded: true,
					children: [
						{
							text: '머신',
							id: 'mon_servers'
						},
						{
							text: '톰캣 인스턴스',
							id: 'mon_tomcats'
						}
					]
				}
            }),
            rootVisible: false
        },
        {
            xtype: 'treepanel',
            componentCls: 'menuTree',
            collapseFirst: false,
            collapsed: false,
            iconCls: 'fa fa-server',
            title: MSG_RESOURCE_MANAGEMENT,
            store: new Ext.data.TreeStore({
                proxy: {
                    type: 'ajax'
                },
				root: {
					children: [
						{
							text: '머신',
							id: 'resm_servers',
							leaf: true
						},
						{
							text: '세션그룹',
							id: 'resm_sessions',
							leaf: true
						},
						{
							text: '데이타소스',
							id: 'resm_ds',
							leaf: true
						}
						
					]
				}
            }),
            rootVisible: false
        },
        /*{
            xtype: 'treepanel',
            componentCls: 'menuTree',
            collapseFirst: false,
            collapsed: false,
            iconCls: 'icon-list1',
            title: MSG_LOG_MANAGEMENT,
            rootVisible: false
        },*/
        {
            xtype: 'treepanel',
            componentCls: 'menuTree',
            collapseFirst: false,
            collapsed: false,
            iconCls: 'fa fa-users',
            title: MSG_USER_MANAGEMENT,
			store: new Ext.data.TreeStore({
                proxy: {
                    type: 'ajax'
                },
				root: {
					children: [
						{
							text: '사용자 관리',
							id: 'usermnt',
							leaf: true
						}
					]
				}
            }),
            rootVisible: false
        }
    ],

    onPanelBeforeExpand: function(p, animate, eOpts) {
        return false;
    }
});