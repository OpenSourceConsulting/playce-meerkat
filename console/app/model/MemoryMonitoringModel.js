/*
 * File: app/model/MonitoringModel.js
 */

Ext.define('webapp.model.MemoryMonitoringModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'monDtString'
        },
        {
            name: 'memUsed'
        },
        {
            name: 'memUsedPer'
        }
    ]
});