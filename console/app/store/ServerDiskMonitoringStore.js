/*
 * File: app/store/UserStore.js
 */

Ext.define('webapp.store.ServerDiskMonitoringStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.ServerDiskMonitoringModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ServerDiskMonitoringStore',
            model: 'webapp.model.ServerDiskMonitoringModel',
            proxy: {
                type: 'ajax',
                url: 'monitor/server/diskmon',
                reader: {
                    type: 'json',
				    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});