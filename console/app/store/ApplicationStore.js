/*
 * File: app/store/ApplicationStore.js
 */

Ext.define('webapp.store.ApplicationStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.ApplicationModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ApplicationStore',
            model: 'webapp.model.ApplicationModel',
            proxy: {
                type: 'ajax',
                url: 'domain/apps',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});