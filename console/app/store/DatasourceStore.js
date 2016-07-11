/*
 * File: app/store/DatasourceStore.js
 */

Ext.define('webapp.store.DatasourceStore', {
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
            storeId: 'DatasourceStore',
            model: 'webapp.model.DatasourceModel',
            proxy: {
                type: 'ajax',
                url: 'res/ds/list',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});