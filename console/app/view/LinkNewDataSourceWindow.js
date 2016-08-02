/*
 * File: app/view/LinkNewDataSourceWindow.js
 */

Ext.define('webapp.view.LinkNewDataSourceWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.LinkNewDataSourceWindow',

    requires: [
        'Ext.form.field.Display',
        'Ext.grid.Panel',
        'Ext.grid.column.Check',
        'Ext.view.Table',
        'Ext.form.field.Checkbox',
        'Ext.button.Button'
    ],
    height: 288,
    id: 'linkNewDataSourceWindow',
    width: 574,
    title: 'Link to datasource',
    modal: true,
    defaultListenerScope: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    dockedItems: [
        {
            xtype: 'container',
            flex: 2,
            dock: 'top',
            margin: '10 0 0 10',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    id: 'linkDatasourceTomcatNameField',
                    width: 213,
                    fieldLabel: 'Tomcat instance',
                    value: 'Tomcat 1'
                },
                {
                    xtype: 'displayfield',
                    id: 'linkDatasourceTomcatStatusField',
                    margin: '0 10 0 100',
                    width: 148,
                    fieldLabel: 'Status:',
                    value: 'Started'
                }
            ]
        }
    ],
    items: [
        {
            xtype: 'gridpanel',
            flex: 6,
			emptyText:"No data.",
            id: 'allDatasourceGrid',
            margin: '5 5 5 5',
            forceFit: true,
            store: 'LinkingTomcatDatasourceStore',
            columns: [
                {
                    xtype: 'checkcolumn',
                    width: 50,
                    dataIndex: 'selected',
                    text: 'Select'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'name',
                    text: 'Data source Name'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'jdbcUrl',
                    text: 'JDBC URL'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'bool',
                    text: 'Server No'
                }
            ]
        },
        {
            xtype: 'checkboxfield',
            flex: 0.5,
            id: 'restartTomcatCheckbox',
            margin: '5 5 5 5',
            boxLabel: 'Restart tomcat instance after linking new datasource.'
        },
        {
            xtype: 'container',
            flex: 1.5,
            margin: '10 5 5 5',
            items: [
                {
                    xtype: 'button',
                    itemId: 'mybutton38',
                    margin: '0 0 0 220',
                    text: 'Save'
                },
                {
                    xtype: 'button',
                    margin: '0 0 0 10',
                    text: 'Cancel',
                    listeners: {
                        click: 'onButtonClick'
                    }
                }
            ]
        }
    ],

    onButtonClick: function(button, e, eOpts) {
         Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

             if(btn == "yes"){
                 button.up("window").close();
             }
         });
    }

});