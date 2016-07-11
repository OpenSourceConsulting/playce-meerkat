/*
 * File: app/store/TaskHistoryStore.js
 */

Ext.define('webapp.store.TaskHistoryStore', {
    extend: 'Ext.data.JsonStore',
	
    requires: [
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json',
        'Ext.data.field.Field'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'TaskHistoryStore',
            proxy: {
                type: 'ajax',
                url: 'dashboard/get/logs',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            fields: [
                {
                    name: 'username'
                },
                {
                    name: 'taskName'
                },
				{
                    name: 'finishedTime'
                },
				{
					name: 'ipaddress'
				},
				{
					name: 'name'
				},
				{
					name: 'id'
				}
            ]
        }, cfg)]);
    }
});