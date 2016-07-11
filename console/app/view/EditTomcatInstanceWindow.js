/*
 * File: app/view/TomcatInstanceWindow.js
 */

Ext.define('webapp.view.EditTomcatInstanceWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.EditTomcatInstanceWindow',

    requires: [
        'Ext.button.Button',
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar'
    ],
    id: 'TomcatInstanceWindow',
    itemId: 'TomcatInstanceWindow',
    scrollable: true,
	width:350,
    title: 'Edit Tomcat Instance',
    defaultListenerScope: true,
	modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'form',
            flex: 1,
            margin: '10 0 10 10',
            items: [
                {
                    xtype: 'textfield',
                    id: 'newTomcatNameField',
					name: 'name',
					width:300,
                    fieldLabel: 'Instance name',
                    allowBlank: false,
                    allowOnlyWhitespace: false
                },
				{
                    xtype: 'hiddenfield',
                    id: 'idHiddenField',
					name: 'id',
                    allowBlank: false,
                    allowOnlyWhitespace: false
                }
            ]
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            flex: 1,
            dock: 'bottom',
            ui: 'footer',
            layout: {
                type: 'hbox',
                pack: 'center'
            },
            items: [
                {
                    xtype: 'button',
                    id: 'btnEditTomcatSubmit',
                    text: 'Save'
                },
                {
                    xtype: 'button',
                    id: 'btnCancelTomcat',
                    text: 'Cancel',
                    listeners: {
                        click: 'onBtnCancelTomcatClick'
                    }
                }
            ]
        }
    ],

    onBtnCancelTomcatClick: function(button, e, eOpts) {
         Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

             if(btn == "yes"){
                button.up("window").close();
             }
         });
    }

});