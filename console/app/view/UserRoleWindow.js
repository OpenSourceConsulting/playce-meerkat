/*
 * File: app/view/UserRoleWindow.js
 */

Ext.define('webapp.view.UserRoleWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.UserRoleWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.Text',
        'Ext.form.field.Hidden',
        'Ext.button.Button',
        'Ext.form.Label'
    ],

    height: 124,
    id: 'UserRoleWindow',
    scrollable: true,
    width: 440,
    layout: 'fit',
    title: 'New User Role',

    items: [
        {
            xtype: 'form',
            height: 260,
            id: 'userRoleForm',
            bodyPadding: 10,
            frameHeader: false,
            header: false,
            title: 'My Form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'textfield',
                    id: 'userRoleNameTextField',
                    itemId: 'userRoleNameTextField',
                    width: 388,
                    fieldLabel: 'User Role',
                    name: 'UserRoleNameTextField',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    validateBlank: true
                },
                {
                    xtype: 'hiddenfield',
                    fieldLabel: 'Label',
                    name: 'IDHiddenField'
                },
                {
                    xtype: 'label',
                    flex: 1,
                    text: 'My Label'
                }
            ],
            dockedItems: [
                {
                    xtype: 'container',
                    flex: 0.5,
                    dock: 'bottom',
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
                            margin: '10 10 5 10',
                            id: 'btnUserRoleSubmit',
                            itemId: 'btnUserRoleSubmit',
                            text: 'Create'
                        },
                        {
                            xtype: 'button',
							margin: '10 10 10 5',
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