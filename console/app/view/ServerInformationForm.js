/*
 * File: app/view/SSHFormPanel.js
 */

Ext.define('webapp.view.ServerInformationForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.serverinformationform',

     requires: [
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden'
    ],

    itemId: 'serverInfoForm',
    bodyPadding: 10,
    frameHeader: false,
    header: false,
	bodyCls: ['osc-body', 'osc-vform'],
    manageHeight: false,
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 100,
        labelAlign: 'right',
		width: 250
    },
    items: [
		{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			hideLabel: true,
			items: [
				{
					xtype: 'displayfield',
					name:'id',
					fieldLabel: 'Server ID',
					width: 150
				},
				{
					xtype: 'displayfield',
					name:'name',
					fieldLabel: 'Server'
				},
				{
					xtype: 'displayfield',
					name:'hostName',
					fieldLabel: 'Host name'
				},
				{
					xtype: 'displayfield',
					itemId: 'ipAddress',
					name: 'sshIPAddr',
					fieldLabel: 'IP Address'
				},
				{
					xtype: 'displayfield',
					itemId: 'osName',
					name :'osName',
					fieldLabel: 'OS Name:'
				},
				{
					xtype: 'displayfield',
					name:'runningAgent',
					fieldLabel: 'Agent 상태',
					width: 200,
					renderer: function (value, field) {
						if(value){
							return '<span style="color:blue;">running</span>';
						}
						return 'not running';
					}
				},
				{
					xtype: 'displayfield',
					name:'agentInstalled',
					hideLabel: true,
					width: 80,
					renderer: function (value, field) {
						if(value){
							return '';
						}
						return '(<span style="color:red;">not installed</span>)';
					}
				}
			]
		}
	]

});