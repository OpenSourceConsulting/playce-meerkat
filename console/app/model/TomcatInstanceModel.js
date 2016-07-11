/*
 * File: app/model/TomcatInstanceModel.js
 */

Ext.define('webapp.model.TomcatInstanceModel', {
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
            type: 'string',
            name: 'ipaddress'
        },
        {
            type: 'string',
            name: 'catalinaHome'
        },
        {
            type: 'string',
            name: 'catalinaBase'
        },
        {
            type: 'int',
            name: 'sshPort'
        },
        {
            type: 'string',
            name: 'sshUsername'
        },
        {
            type: 'string',
            name: 'sshPassword'
        },
        {
            type: 'int',
            name: 'httpPort'
        },
        {
            type: 'string',
            name: 'state'
        },
        {
            type: 'int',
            name: 'applications'
        },
        {
            type: 'string',
            name: 'hostName'
        },
        {
            type: 'string',
            name: 'stateNm'
        }
    ]
});