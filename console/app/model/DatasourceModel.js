/*
 * File: app/model/DatasourceModel.js
 */

Ext.define('webapp.model.DatasourceModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Boolean'
    ],

    fields: [
        {
            name: 'Id'
        },
        {
            name: 'name'
        },
        {
            name: 'dbType'
        },
		{
            name: 'dbTypeName'
        },
        {
            name: 'userName'
        },
        {
            name: 'maxConnection'
        },
        {
            name: 'timeout'
        },
        {
            name: 'minConnectionPool'
        },
        {
            name: 'maxConnectionPool'
        },
        {
            name: 'jdbcUrl'
        },
        {
            type: 'boolean',
            name: 'selected'
        },
        {
            name: 'tomcatInstancesNo'
        },
        {
            name: 'password'
        }
    ]
});