/*
 * File: app/view/SSHAccountWindow.js
 */

Ext.define('webapp.view.SSHAccountWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.SSHAccountWindow',

    requires: [
        'webapp.view.SSHFormPanel',
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],
    height: 300,
    id: 'sshAccountWindow',
    width: 432,
    title: 'SSH Account',
    modal: true,
    defaultListenerScope: true,

    items: [
        {
            xtype: 'sshformpanel'
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
                    id: 'serverTestSSHConnectionBtn',
                    text: 'Test'
                },
                {
                    xtype: 'button',
                    disabled: true,
                    id: 'serverSubmitSSHBtn',
                    itemId: 'serverSubmitSSHBtn',
                    text: 'Add'
                },
                {
                    xtype: 'button',
                    id: 'serverCancelSSHBtn',
                    text: 'Cancel',
                    listeners: {
                        click: 'onServerCancelSSHBtnClick'
                    }
                }
            ]
        }
    ],

    onServerCancelSSHBtnClick: function(button, e, eOpts) {
        Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

            if(btn == "yes"){
                button.up("window").close();
            }
        });
    }

});