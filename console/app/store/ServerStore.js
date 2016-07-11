/*
 * File: app/store/ServerStore.js
 */

Ext.define('webapp.store.ServerStore', {
    extend: 'Ext.data.Store',
	alias: 'store.ServerStore',
    requires: [
        'webapp.model.ServerModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ServerStore',
            autoLoad: false,
            model: 'webapp.model.ServerModel',
            proxy: {
                type: 'ajax',
                url: 'res/server/list',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});