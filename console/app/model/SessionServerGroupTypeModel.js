/*
 * File: app/model/DatagridModel.js
 */

Ext.define('webapp.model.SessionServerGroupTypeModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'id'
        },
        {
            name: 'codeNm'
        }
    ]
});