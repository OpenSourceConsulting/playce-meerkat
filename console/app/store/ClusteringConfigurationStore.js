/*
 * File: app/store/ClusteringConfigurationStore.js
 */

Ext.define('webapp.store.ClusteringConfigurationStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.ClusteringConfigurationModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ClusteringConfigurationStore',
            model: 'webapp.model.ClusteringConfigurationModel',
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