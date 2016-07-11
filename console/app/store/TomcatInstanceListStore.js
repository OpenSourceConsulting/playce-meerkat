/*
 * File: app/store/TomcatInstanceListStore.js
 */

Ext.define('webapp.store.TomcatInstanceListStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.TomcatInstanceModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'tomcatInstanceListStoreID',
            model: 'webapp.model.TomcatInstanceModel',
            proxy: {
                type: 'ajax',
                url: 'domain/tomcatlist',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});