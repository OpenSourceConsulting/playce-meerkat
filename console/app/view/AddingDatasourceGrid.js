/*
 * File: app/view/AddingDatasourceGrid.js
 */

Ext.define('webapp.view.AddingDatasourceGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.AddingDatasourceGrid',

    requires: [
        'Ext.selection.CheckboxModel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    itemId: 'dsGrid',
	forceFit: true,
    scrollable: true,
    bodyBorder: true,
    bodyStyle: 'background: #fff',
    header: false,
    iconCls: 'icon-grid',
    title: 'Datasources',
    store: 'DatasourceStore',

    columns: [
        {
            xtype: 'gridcolumn',
            width: 40,
            dataIndex: 'name',
            text: 'Datasource Name'
        },
        {
            xtype: 'gridcolumn',
            dataIndex: 'jdbcUrl',
            text: 'JDBC Url'
        },
        {
            xtype: 'gridcolumn',
            dataIndex: 'tomcatInstancesNo',
            text: '사용중 서버 개수',
			width: 40
        }
    ],
	
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'button',
                    itemId: 'btnWCreateDs',
                    iconCls: 'add',
                    text: '데이타소스 생성'
                }
            ]
        }
    ],

    initConfig: function(instanceConfig) {
        var me = this,
            config = {
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    selType: 'checkboxmodel'
                })
            };
        if (instanceConfig) {
            me.getConfigurator().merge(me, config, instanceConfig);
        }
        return me.callParent([config]);
    }
});