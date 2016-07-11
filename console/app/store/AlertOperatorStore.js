/*
 * File: app/store/AlertOperatorStore.js
 */

Ext.define('webapp.store.AlertOperatorStore', {
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
            storeId: 'DatasourceStore',
            model: 'webapp.model.CommonCodeModel',
            proxy: {
                type: 'ajax',
                url: 'monitor/alert/setting/operator/list',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});