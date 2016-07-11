/*
 * File: app/store/NetworkInterfaceStore.js
 */

Ext.define('webapp.store.NetworkInterfaceStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.NetworkInterfaceModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'NetworkInterfaceStore',
            model: 'webapp.model.NetworkInterfaceModel',
            proxy: {
                type: 'ajax',
				url: 'res/server/1/nis',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});