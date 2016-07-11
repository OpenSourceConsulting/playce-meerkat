/*
 * File: app/model/MonitoringModel.js
 */

Ext.define('webapp.model.MonitoringModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'type'
        },
        {
            name: 'serverId'
        },
        {
            name: 'monDt'
        },
        {
            name: 'monValue'
        },
		{
            name: 'monValue2' //max value, other values
        },
        {
            name: 'monDtString'
        }
    ]
});