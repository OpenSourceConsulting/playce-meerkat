/*
 * File: app/model/CommonCodeModel.js
 */

Ext.define('webapp.model.CommonCodeModel', {
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