/*
 * File: app/model/DatagridServerGroupModel.js
 */

Ext.define('webapp.model.DatagridServerGroupModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Integer',
        'Ext.data.field.String'
    ],

    fields: [
        {
            type: 'int',
            name: 'id'
        },
        {
            type: 'string',
            name: 'name'
        },
        {
            name: 'typeNm'
        },
        {
            name: 'serverNo'
        }
    ]
});