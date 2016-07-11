/*
 * File: app/store/DomainStatsStore.js
 */

Ext.define('webapp.store.DomainStatsStore', {
    extend: 'Ext.data.Store',
	
	requires: [
        'webapp.model.DomainStatsModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],
    
	constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'DomainStatsStore',
            model: 'webapp.model.DomainStatsModel',
            proxy: {
                type: 'ajax',
                url: 'dashboard/get/domain/stats',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }

});