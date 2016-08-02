/*
 * File: app/store/TopCPUMonitorStore.js
 */

Ext.define('webapp.store.TopCPUMonitorStore', {
    extend: 'Ext.data.Store',

    requires: [
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json',
        'Ext.data.field.Field'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'TopCPUMonitorStore',
            proxy: {
                type: 'ajax',
                url: 'dashboard/get/alerts',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            fields: [
                {
                    name: 'name'
                },
                {
                    name: 'decimalMonValue'
                },
				{
					name:'type'
				},
				{
                    name: 'alertStatus'
                }
            ]
        }, cfg)]);
    }
});