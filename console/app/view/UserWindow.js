/*
 * File: app/view/UserWindow.js
 */

Ext.define('webapp.view.UserWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.UserWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden',
        'Ext.button.Button'
    ],
    height: 500,
    id: 'UserWindow',
    width: 440,
    title: 'New User',
	defaultFocus: 'username',
    items: [
        {
            xtype: 'form',
            height: 460,
            id: 'userForm',
            bodyPadding: 10,
            frameHeader: false,
            header: false,
            items: [
                {
                    xtype: 'textfield',
                    width: 388,
                    fieldLabel: 'User ID',
                    name: 'username',
					itemId: 'username',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    maxLength: 20,
                    minLength: 3,
                    validateBlank: true
                },
                {
                    xtype: 'textfield',
                    width: 390,
                    fieldLabel: 'Password',
                    name: 'password',
                    inputType: 'password',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    maxLength: 20,
                    minLength: 5
                },
                {
                    xtype: 'textfield',
                    width: 390,
                    fieldLabel: 'Retype-Password',
                    name: 'retypePassword',
                    inputType: 'password',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    maxLength: 20,
                    minLength: 5
                },
                {
                    xtype: 'textfield',
                    width: 389,
                    fieldLabel: 'Full Name:',
                    name: 'fullName',
                    allowBlank: false,
                    maxLength: 20,
                    minLength: 5
                },
                {
                    xtype: 'textfield',
                    width: 391,
                    fieldLabel: 'Email:',
                    name: 'email',
                    inputType: 'email',
                    maxLength: 20,
                    minLength: 5,
                    validateBlank: true,
                    vtype: 'email'
                },
                {
                    xtype: 'fieldset',
					itemId: 'userRoleFieldSet',
					title: 'User Role',
					items: [
						{
							xtype: 'gridpanel',
							name: 'userRoles',
							margin: '5 0 0 0 ',
							forceFit: true,
							store: 'UserRoleStore',
							columns: [
								{
									xtype:'checkcolumn',
									dataIndex: 'selected',
									text: 'Select',
									width: 30
								},
								{
									xtype: 'gridcolumn',
									dataIndex: 'name',
									text: 'Name'
								},
								{
									xtype: 'gridcolumn',
									width: 60,
									dataIndex: 'userCount',
									text: 'User Count'
								}
							]
						}
					]
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    fieldLabel: 'Label',
                    name: 'id'
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
                            margin: '10 10 10 10',
                            id: 'btnSubmit',
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