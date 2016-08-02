/*
 * File: app/view/DeployWindow.js
 */

Ext.define('webapp.view.DeployWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.DeployWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.File',
        'Ext.button.Button',
        'Ext.form.field.Hidden'
    ],
    height: 170,
    id: 'deployWindow',
    width: 600,
    layout: 'fit',
    title: 'Deploy',
	modal: true,
    items: [
        {
            xtype: 'form',
            id: 'applicationDeployForm',
            bodyPadding: 15,
            method: 'POST',
			buttonAlign: 'center',
			layout: {
				type: 'vbox',
				align: 'stretch'
			},
			fieldDefaults: {
				labelWidth: 170,
				labelAlign: 'right'
			},
            items: [
				{
                    xtype: 'hiddenfield',
                    fieldLabel: 'Label',
                    name: 'tomcatDomain'
                },
                {
                    xtype: 'filefield',
                    fieldLabel: 'Choose application (*.war)',
                    name: 'warFile',
					msgTarget: 'side',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    emptyText: 'Select *.war file',
					listeners: {
                        change: function(fld, value) {
                            var newValue = value.replace(/^.*(\\|\/|\:)/, '');
                            fld.setRawValue(newValue);
                        }
                    }
                },
                {
                    xtype: 'textfield',
                    fieldLabel: 'Context path',
                    name: 'contextPath',
					msgTarget: 'side',
                    allowBlank: false,
                    allowOnlyWhitespace: false
                }
            ],
			buttons: [
				{
                    id: 'btnSubmitDeploy',
                    text: 'Deploy'
                },
                {
                    xtype: 'button',
                    itemId: 'mybutton37',
                    text: 'Cancel'
                }
			]
        }
    ]

});