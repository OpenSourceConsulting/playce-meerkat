/*
 * File: app/model/SSHAccountModel.js
 */

Ext.define('webapp.model.SSHAccountModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'id'
        },
        {
            name: 'username'
        },
        {
            name: 'password'
        },
        {
            name: 'root'
        }
    ]
});