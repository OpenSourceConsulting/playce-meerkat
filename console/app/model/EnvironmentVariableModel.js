/*
 * File: app/model/EnvironmentVariableModel.js
 */

Ext.define('webapp.model.EnvironmentVariableModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'Id'
        },
        {
            name: 'name'
        },
        {
            name: 'value'
        },
        {
            name: 'revision'
        }
    ]
});