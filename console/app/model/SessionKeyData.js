/*
 * File: app/model/SessionKeyData.js
 */

Ext.define('webapp.model.SessionKeyData', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'key'
        },
        {
            name: 'id'
        },
        {
            name: 'value'
        },
        {
            name: 'location'
        }
    ]
});