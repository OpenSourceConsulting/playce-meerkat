/*
 * File: app/view/DatasourceWindow.js
 */

Ext.define('webapp.view.DatasourceWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.DatasourceWindow',

    requires: [
        'webapp.view.DatasourceForm',
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],
    height: 371,
    id: 'datasourceWindow',
    width: 441,
    title: 'New Datasource Window',
    defaultListenerScope: true,
	modal: true,
    items: [
        {
            xtype: 'datasourceform'
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'bottom',
            layout: {
                type: 'hbox',
                pack: 'center'
            },
            items: [
                {
                    xtype: 'button',
                    id: 'formDSTestBtn',
                    text: 'Test'
                },
                {
                    xtype: 'button',
                    itemId: 'formDSSubmitBtn',
                    text: 'Create'
                },
                {
                    xtype: 'button',
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