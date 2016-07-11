/*
 * File: app/store/ClusteringConfVersionStore.js
 */

Ext.define('webapp.store.ClusteringConfVersionStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.ClusteringConfVersionModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ClusteringConfVersionStore',
            model: 'webapp.model.ClusteringConfVersionModel',
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