/*
 * File: app/model/ServerModel.js
 */

Ext.define('webapp.model.ServerModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Integer'
    ],

    fields: [
        {
            type: 'int',
            name: 'id'
        },
        {
            name: 'name'
        },
        {
            name: 'osName'
        },
        {
            name: 'sshNiId'
        },
        {
            name: 'sshPort'
        },
        {
            name: 'hostName'
        },
        {
            name: 'instName'
        },
        {
            name: 'sshIPAddr'
        },
        {
            name: 'groupName'
        },
        {
            name: 'sessionNo'
        },
		{
			name: 'selected'
		},
		{
			name:'tomcatInstanceNo'
		},
		{
			name:'runningAgent'
		},
		{
			name:'agentInstalled'
		}
		
    ]
});