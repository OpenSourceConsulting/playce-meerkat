/*
 * File: app/model/DomainStatsModel.js
 */

Ext.define('webapp.model.DomainStatsModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'id'
        },
        {
            name: 'name'
        },
        {
            name: 'tomcatInstancesCount'
        }
    ]
});