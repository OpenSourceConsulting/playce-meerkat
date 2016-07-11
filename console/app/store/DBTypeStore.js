/*
 * File: app/store/DBTypeStore.js
 */

Ext.define('webapp.store.DBTypeStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.CommonCodeModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'DBTypeStore',
            model: 'webapp.model.CommonCodeModel',
            proxy: {
                type: 'ajax',
                url: 'code/list/dbType',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});