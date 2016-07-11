/*
 * File: app/model/ClusteringConfigurationModel.js
 */

Ext.define('webapp.model.ClusteringConfigurationModel', {
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