/*
 * File: app/model/TomcatConfigFileModel.js
 */

Ext.define('webapp.model.TomcatConfigFileModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'id'
        },
        {
            name: 'domainId'
        },
        {
            name: 'fileTypeCdId'
        },
        {
            name: 'version'
        },
        {
            name: 'comment'
        },
        {
            name: 'filePath'
        },
        {
            name: 'createdTime'
        },
        {
            name: 'tomcatId'
        },
        {
            name: 'versionAndTimeAndTomcat'
        }
    ]
});