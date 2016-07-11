/*
 * File: app/view/SSHFormPanel.js
 */

Ext.define('webapp.view.SSHFormPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.sshformpanel',

    requires: [
        'Ext.form.field.Display',
        'Ext.form.field.Text',
        'Ext.container.Container',
        'Ext.form.field.Checkbox',
        'Ext.form.field.Hidden'
    ],
    id: 'sshFormPanel',
    bodyPadding: 10,

    items: [
        {
            xtype: 'displayfield',
            id: 'serverNameDisplayField',
            width: 335,
            fieldLabel: 'Server name'
        },
        {
            xtype: 'displayfield',
            id: 'serverSSHIPAddressDisplayField',
            width: 372,
            fieldLabel: 'SSH IP Address:'
        },
        {
            xtype: 'displayfield',
            id: 'serverSSHPortDisplayField',
            width: 297,
            fieldLabel: 'SSH Port'
        },
        {
            xtype: 'textfield',
            id: 'serverSSHUserIDTextField',
            fieldLabel: 'User ID',
            allowBlank: false
        },
        {
            xtype: 'container',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'textfield',
                    id: 'serverSSHPasswordTextField',
                    width: 255,
                    fieldLabel: 'Password',
                    inputType: 'password',
                    allowBlank: false
                },
                {
                    xtype: 'checkboxfield',
                    flex: 1,
                    itemId: 'showPasswordCheckbox',
                    margin: '0 0 0 10',
                    boxLabel: 'Show password'
                },
                {
                    xtype: 'hiddenfield',
                    flex: 1,
                    id: 'serverIDHiddenField',
                    fieldLabel: 'Label'
                },
                {
                    xtype: 'hiddenfield',
                    flex: 1,
                    id: 'sshAccountHiddenField',
                    fieldLabel: 'Label'
                }
            ]
        },
        {
            xtype: 'checkboxfield',
            anchor: '100%',
            id: 'rootCheckBox',
            fieldLabel: 'Root permission'
        }
    ]

});