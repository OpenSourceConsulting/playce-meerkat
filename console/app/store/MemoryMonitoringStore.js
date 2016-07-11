/*
 * File: app/store/MonitoringStore.js
 */

Ext.define('webapp.store.MemoryMonitoringStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.MemoryMonitoringModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'MemoryMonitoringStore',
            model: 'webapp.model.MemoryMonitoringModel',
            proxy: {
                type: 'ajax',
                url: 'monitoring/server/memorymon',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});