/*
 * File: app/store/DomainStore.js
 */

Ext.define('webapp.store.DomainStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.DomainModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'DomainStore',
            model: 'webapp.model.DomainModel',
            proxy: {
                type: 'ajax',
                url: 'domain/list',
                reader: {
                    type: 'json',
					rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});