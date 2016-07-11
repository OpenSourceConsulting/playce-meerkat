/*
 * File: app/view/CreateTomcatInstanceGrid.js
 *
 */

Ext.define('webapp.view.CreateTomcatInstanceGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.CreateTomcatInstanceGrid',

    requires: [
        'Ext.selection.CheckboxModel',
        'Ext.grid.column.Column',
        'Ext.form.field.Text',
        'Ext.view.Table',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.toolbar.Separator',
        'Ext.grid.plugin.CellEditing',
		'webapp.store.ServerStore'
    ],

    height: 250,
    itemId: 'targetServerGrid',
    scrollable: true,
    bodyBorder: true,
    bodyStyle: 'background: #fff',
    header: false,
    iconCls: 'icon-grid',
    store: {type: 'ServerStore'},

    columns: [
        {
            xtype: 'gridcolumn',
            width: 150,
            dataIndex: 'instName',
            text: 'Instance Name',
            editor: {
                xtype: 'textfield',
                allowBlank: false,
				selectOnFocus: true
            },
			renderer: function(value, metaData){
				if (Ext.isEmpty(value)) {
					metaData.tdStyle = 'font-style: italic; color: red';
					return 'Please edit';
				}
				
				return value;
			}
        },
        {
            xtype: 'gridcolumn',
            width: 150,
            dataIndex: 'name',
            text: 'Server Name'
        },
        {
            xtype: 'gridcolumn',
            width: 150,
            dataIndex: 'hostName',
            text: 'Host Name'
        },
        {
            xtype: 'gridcolumn',
			width: 150,
            dataIndex: 'sshIPAddr',
            text: 'IP Address'
        }
    ],
	
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
			
            items: [
                {
                    xtype: 'button',
                    itemId: 'newServerBtn',
                    iconCls: 'add',
                    text: 'Create Server'
                },
                {
                    xtype: 'tbseparator'
                },
                {
                    xtype: 'textfield',
                    width: 250,
                    fieldLabel: 'Search',
                    labelWidth: 50
                }
            ]
        }
    ],

    initConfig: function(instanceConfig) {
        var me = this,
            config = {
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    selType: 'checkboxmodel',
                    checkOnly: true
                }),
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1
                    })
                ]
            };
        if (instanceConfig) {
            me.getConfigurator().merge(me, config, instanceConfig);
        }
        return me.callParent([config]);
    }
});