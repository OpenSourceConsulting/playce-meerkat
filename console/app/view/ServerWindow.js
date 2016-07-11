/*
 * File: app/view/ServerWindow.js
 */

Ext.define('webapp.view.ServerWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.ServerWindow',

    requires: [
        'webapp.view.ServerFormPanel',
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],
	modal : true,
    height: 310,
    id: 'serverWindow',
    width: 325,
    title: 'Server 등록',
    defaultListenerScope: true,
	defaultFocus: 'serverName',

    items: [
        {
            xtype: 'serverformpanel',
            height: 254
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
                    id: 'testServerBtn',
                    text: 'Test connect'
                },
                {
                    xtype: 'button',
                    disabled: true,
                    id: 'submitServerBtn',
                    text: '저장'
                },
                {
                    xtype: 'button',
                    id: 'cancelServerBtn',
                    text: 'Cancel',
                    listeners: {
                        click: 'onCancelServerBtnClick'
                    }
                }
            ]
        }
    ],

    onCancelServerBtnClick: function(button, e, eOpts) {
        Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

            if(btn == "yes"){
                button.up("window").close();
            }
        });
    }

});