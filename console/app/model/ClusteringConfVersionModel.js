/*
 * File: app/model/ClusteringConfVersionModel.js
 */

Ext.define('webapp.model.ClusteringConfVersionModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'id'
        },
        {
            name: 'version'
        },
        {
            name: 'createdTime'
        },
        {
            name: 'versionAndCreatedTime'
        }
    ]
});