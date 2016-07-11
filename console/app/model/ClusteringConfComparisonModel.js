/*
 * File: app/model/ClusteringConfComparisonModel.js
 */

Ext.define('webapp.model.ClusteringConfComparisonModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'firstId'
        },
        {
            name: 'secondId'
        },
        {
            name: 'name'
        },
        {
            name: 'firstValue'
        },
        {
            name: 'secondValue'
        },
        {
            name: 'id'
        }
    ]
});