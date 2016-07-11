/*
 * File: app/model/ApplicationModel.js
 */

Ext.define('webapp.model.ApplicationModel', {
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
            name: 'contextPath'
        },
        {
            type: 'string',
            name: 'deployedTimeString'
        },
        {
            type: 'string',
            name: 'version'
        },
        {
            type: 'string',
            name: 'warPath'
        },
        {
            type: 'string',
            name: 'lastModifiedTimeString'
        },
        {
            type: 'int',
            name: 'state'
        },
        {
            name: 'tomcatName'
        },
        {
            name: 'sessionTimeOut'
        },
        {
            name: 'sessions'
        }
    ]
});