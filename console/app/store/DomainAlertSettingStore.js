/*
 * File: app/store/DomainAlertSettingStore.js
 */

Ext.define('webapp.store.DomainAlertSettingStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.DomainAlertSettingModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ApplicationStore',
            model: 'webapp.model.DomainAlertSettingModel',
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