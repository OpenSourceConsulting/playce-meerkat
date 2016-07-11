/*
 * File: app/store/SessionStore.js
 */

Ext.define('webapp.store.SessionStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.SessionKeyData',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'SessionStore',
            model: 'webapp.model.SessionKeyData',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});