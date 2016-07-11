/*
 * File: app/model/UserRoleModel.js
 */

Ext.define('webapp.model.UserRoleModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.String',
        'Ext.data.field.Integer'
    ],

    fields: [
        {
            type: 'string',
            name: 'name'
        },
        {
            type: 'int',
            name: 'id'
        },
        {
            name: 'userCount'
        }
    ]
});