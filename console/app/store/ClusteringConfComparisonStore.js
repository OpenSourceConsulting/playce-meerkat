/*
 * File: app/store/ClusteringConfComparisonStore.js
 */

Ext.define('webapp.store.ClusteringConfComparisonStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.ClusteringConfComparisonModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ClusteringConfComparisonStore',
            model: 'webapp.model.ClusteringConfComparisonModel',
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