/*
 * File: app/view/EnvironmentVariableWindow.js

 */

Ext.define('webapp.view.EnvironmentVariableWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.environmentvariablewindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.Text',
        'Ext.form.field.Hidden',
        'Ext.button.Button'
    ],
    height: 153,
    id: 'environmentVariableWindow',
    width: 440,
    title: 'New environment variable',
	modal: true,
    items: [
        {
            xtype: 'form',
            height: 154,
            id: 'userForm1',
            bodyPadding: 10,
            frameHeader: false,
            header: false,
            title: 'My Form',
            items: [
                {
                    xtype: 'textfield',
                    id: 'nameEnvironmentVariableTextField',
                    itemId: 'userIDTextField',
                    width: 388,
                    fieldLabel: 'Name',
                    name: 'NameEnvironmentVariableTextField',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    maxLength: 20,
                    minLength: 3,
                    validateBlank: true
                },
                {
                    xtype: 'textfield',
                    id: 'valueEnvironmentVariableTextField',
                    width: 389,
                    fieldLabel: 'Value',
                    name: 'ValueEnvironmentVariableTextField',
                    allowBlank: false,
                    maxLength: 20,
                    minLength: 5
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    id: 'serverIDEnvironmentVariableHiddenField',
                    fieldLabel: 'Label',
                    name: 'ServerIDHiddenField'
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    id: 'environmentVariableIDHiddenField',
                    fieldLabel: 'Label',
                    name: 'IDHiddenField'
                },
                {
                    xtype: 'container',
                    height: 38,
                    layout: {
                        type: 'hbox',
                        align: 'middle',
                        defaultMargins: {
                            top: 0,
                            right: 10,
                            bottom: 10,
                            left: 0
                        },
                        pack: 'center'
                    },
                    items: [
                        {
                            xtype: 'button',
                            margins: '10 10 10 10',
                            id: 'btnSubmit1',
                            itemId: 'btnSubmit',
                            text: 'Create'
                        },
                        {
                            xtype: 'button',
                            handler: function(button, e) {
                                Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

                                    if(btn == "yes"){
                                        button.up("window").close();
                                    }
                                });
                            },
                            text: 'Cancel'
                        }
                    ]
                }
            ]
        }
    ]

});