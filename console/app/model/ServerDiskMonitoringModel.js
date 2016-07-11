/*
 * File: app/model/UserModel.js
 */

Ext.define('webapp.model.ServerDiskMonitoringModel', {
    extend: 'Ext.data.Model',
    alias: 'model.ServerDiskMonitoringModel',

    requires: [
        'Ext.data.field.String',
        'Ext.data.field.Integer'
    ],

    fields: [
        {
            type: 'string',
            name: 'fsName'
        },
        {
            name: 'total'
        },
        {
            name: 'used'
        },
        {
            name: 'usePer'
        },
		{
			name: 'reCalUsePer'
		},
        {
            name: 'avail'
        },
        {
            name: 'availPer'
        },
        {
            type: 'int',
            name: 'id'
        }
    ]
});