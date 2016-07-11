/*
 * File: app/model/DomainModel.js
 */

Ext.define('webapp.model.DomainModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'id'
        },
        {
            name: 'name'
        },
        {
            name: 'tomcatInstancesCount'
        },
        {
            name: 'datagridServerGroupName'
        },
		{
			name: 'applicationCount'
		},
        {
            name: 'clustering'
        }
    ]
});