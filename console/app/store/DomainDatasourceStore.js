/*
 * File: app/store/DomainDatasourceStore.js
 */

Ext.define('webapp.store.DomainDatasourceStore', {
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
            storeId: 'DomainDatasourceStore',
            model: 'webapp.model.DatasourceModel',
            proxy: {
                type: 'ajax',
                url: 'res/ds/domain/link/list',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});