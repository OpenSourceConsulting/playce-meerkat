/*
 * File: app/view/loginWindow.js
 */

Ext.define('webapp.view.loginWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.loginWindow',

    requires: [
        'Ext.form.Label',
        'Ext.form.Panel',
        'Ext.form.field.Text',
        'Ext.button.Button'
    ],
	
    height: 250,
    id: 'loginWindow',
    itemId: 'loginWindow',
    resizable: false,
    width: 420,
    closable: false,
    title: 'Athena Meerkat',
	defaultFocus: 'userName',
    modal: true,
    defaultListenerScope: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'panel',
            flex: 1,
            id: 'loginPanel',
            itemId: 'loginPanel',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'label',
                    height: 55,
                    html: '<h3>환영합니다.<br/>사용하시려면 <font color="red">로그인</font>을 해주십시오.</h3>',
                    id: 'welcomeLabel',
                    itemId: 'welcomeLabel',
                    margin: '5 0 0 0',
                    style: '{text-align: center;}'
                },
                {
                    xtype: 'form',
                    id: 'loginForm',
                    itemId: 'loginForm',
                    margin: '5 0 0 0',
                    bodyPadding: 10,
                    items: [
                        {
                            xtype: 'textfield',
                            anchor: '100%',
                            id: 'userName',
                            itemId: 'userName',
                            fieldLabel: '사용자아이디',
                            labelAlign: 'right',
                            labelStyle: 'search',
                            labelWidth: 90,
                            name: 'username',
                            allowBlank: false,
                            blankText: '사용자아이디는 필수 입력 항목입니다.'
                        },
                        {
                            xtype: 'textfield',
                            anchor: '100%',
                            id: 'password',
                            itemId: 'password',
                            fieldLabel: '비밀번호',
                            labelAlign: 'right',
                            labelWidth: 90,
                            name: 'password',
                            inputType: 'password',
                            allowBlank: false,
                            blankText: '비밀번호는 필수 입력 항목입니다.'
                        },
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'center',
                                defaultMargins: {
                                    top: 20,
                                    right: 5,
                                    bottom: 5,
                                    left: 5
                                }
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    id: 'loginBtn',
                                    itemId: 'loginBtn',
                                    scale: 'medium',
                                    text: '로그인'
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]

});