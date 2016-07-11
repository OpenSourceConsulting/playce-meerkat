/*
 * File: app/view/ServerFormPanel.js
 */

Ext.define('webapp.view.ServerFormPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.serverformpanel',

    requires: [
        'Ext.form.field.Number',
        'Ext.form.FieldSet',
        'Ext.form.Label'
    ],
    height: 251,
    id: 'serverFormPanel',
    width: 305,
    bodyPadding: 10,

    items: [
        {
            xtype: 'textfield',
            anchor: '100%',
            name: 'name',
			itemId: 'serverName',
            fieldLabel: 'Server name'
        },
        {
            xtype: 'textfield',
            anchor: '100%',
            name: 'sshIPAddr',
            fieldLabel: 'SSH IP Address'
        },
        {
            xtype: 'numberfield',
            anchor: '100%',
            name: 'sshPort',
            fieldLabel: 'SSH Port',
			value: '22'
        },
		{
            xtype: 'hiddenfield',
            id: 'parentButtonHiddenId'
        },
        {
            xtype: 'fieldset',
            height: 114,
            title: 'SSH Account',
            items: [
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    name: 'sshUserName',
                    fieldLabel: 'Username'
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    name: 'sshPassword',
                    fieldLabel: 'Password',
					inputType: 'password'
                },
                {
                    xtype: 'label',
                    height: 16,
                    text: '계정을 생성하지 않습니다.  생성된 계정을 입력해주세요.',
					cls: 'osc-panel-tip'
                }
            ]
        }
    ]

});