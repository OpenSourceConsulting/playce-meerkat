/*
 * File: app/view/DomainForm.js
 */
var MSG_HOST_NM='Host name';
var MSG_TI_STATUS='상태';//tomcat instance 상태
var MSG_IP_ADDRESS='IP 주소';
var MSG_HTTP_PORT='Http Port';

Ext.define('webapp.view.TomcatInstanceForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.tomcatinstanceform',

    requires: [
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden'
    ],

    itemId: 'tomcatForm',
    bodyPadding: 10,
	bodyCls: 'osc-body',
	cls: 'osc-vform',
    frameHeader: false,
    header: false,
    manageHeight: false,
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 100,
        labelAlign: 'right',
		width: 180
    },
    items: [
		{
			xtype: 'hiddenfield',
			name :'httpPort',
			itemId: 'tHttpPort'
		},
		{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			hideLabel: true,
			items: [
				{
					xtype: 'displayfield',
					name:'name',
					fieldLabel: 'Instance 명',
					width: 260
				},
				{
					xtype: 'displayfield',
					name:'hostName',
					fieldLabel: MSG_HOST_NM,
					width: 260
				},
				{
					xtype: 'displayfield',
					name:'stateNm',
					fieldLabel: MSG_TI_STATUS
				},
				{
					xtype: 'displayfield',
					name: 'ipAddress',
					itemId: 'tIpAddr',
					fieldLabel: MSG_IP_ADDRESS
				},
				{
					xtype: 'displayfield',
					name :'tomcatPorts',
					fieldLabel: MSG_HTTP_PORT,
					width: 320
				}
			]
		},
		{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			hideLabel: true,
			items: [
				{
					xtype: 'displayfield',
					name:'tomcatVersion',
					fieldLabel: 'Tomcat version',
					width: 260
				},
				{
					xtype: 'displayfield',
					name:'jvmVersion',
					fieldLabel: 'JVM Version',
					width: 260
				},
				{
					xtype: 'displayfield',
					name:'osName',
					fieldLabel: 'OS Name'
				},
				{
					xtype: 'displayfield',
					name:'jmxEnable',
					fieldLabel: 'JMX',
					renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
						if (value) {
							return '<span style="color:blue;">enabled</span>';
						} 
						return '<span style="color:red;">disabled</span>';
					}
				}
			]
		}
		
	]
});