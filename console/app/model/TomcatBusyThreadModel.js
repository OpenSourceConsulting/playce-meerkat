/*
 * File: app/model/TomcatBusyThreadModel.js
 */

Ext.define('webapp.model.TomcatBusyThreadModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'time'
        },
        {
            name: 'value'
        },
        {
            name: 'value2'
        }
    ]
});