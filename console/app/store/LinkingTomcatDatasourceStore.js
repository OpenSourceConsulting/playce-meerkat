/*
 * File: app/store/LinkingTomcatDatasourceStore.js
 */

Ext.define('webapp.store.LinkingTomcatDatasourceStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.DatasourceModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'LinkingTomcatDatasourceStore',
            model: 'webapp.model.DatasourceModel',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});