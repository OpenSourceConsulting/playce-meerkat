/*
 * File: app/model/DomainAlertSettingModel.js
 */

Ext.define('webapp.model.DomainAlertSettingModel', {
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
            name: 'tomcatDomainId'
        },
        {
            type: 'string',
            name: 'alertItemCdNm'
        },
        {
            type: 'string',
            name: 'threshold'
        },
        {
            name: 'status'
        }
    ]
});