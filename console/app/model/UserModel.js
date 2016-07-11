/*
 * File: app/model/UserModel.js
 */

Ext.define('webapp.model.UserModel', {
    extend: 'Ext.data.Model',
    alias: 'model.UserModel',

    requires: [
        'Ext.data.field.String',
        'Ext.data.field.Integer'
    ],

    fields: [
        {
            type: 'string',
            name: 'password'
        },
        {
            name: 'fullName'
        },
        {
            name: 'userRolesString'
        },
        {
            name: 'email'
        },
        {
            name: 'createdDateString'
        },
        {
            name: 'lastLoginDateString'
        },
        {
            type: 'int',
            name: 'id'
        },
        {
            type: 'string',
            name: 'username'
        },
		{
			name:'selected'
		}
    ]
});