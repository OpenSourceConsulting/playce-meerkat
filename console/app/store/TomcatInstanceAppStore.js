/*
 * File: app/store/TomcatInstanceAppStore.js
 */

Ext.define('webapp.store.TomcatInstanceAppStore', {
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
            storeId: 'TomcatInstanceAppStore',
			autoLoad: false,
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
                    name: 'perm'
                },
                {
                    name: 'own'
                },
				{
                    name: 'date'
                },
				{
					name: 'appName'
				}
            ]
        }, cfg)]);
    }
});