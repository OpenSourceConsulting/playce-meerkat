/*
 * File: app/model/DatagridModel.js
 */

Ext.define('webapp.model.DatagridModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'serverName'
        },
        {
            name: 'groupName'
        },
        {
            name: 'ipAddress'
        },
        {
            name: 'hostName'
        },
        {
            name: 'sessions'
        },
        {
            name: 'status'
        },
        {
            name: 'type'
        }
    ]
});