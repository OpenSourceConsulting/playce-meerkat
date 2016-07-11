/*
 * File: app/store/MonitoringStore.js
 */

Ext.define('webapp.store.MonitoringStore', {
    extend: 'Ext.data.Store',
	alias: 'store.MonitoringStore',
    requires: [
        'webapp.model.MonitoringModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'MonitoringStore',
            model: 'webapp.model.MonitoringModel',
            proxy: {
                type: 'ajax',
                url: 'monitoring/server/cpumon',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});