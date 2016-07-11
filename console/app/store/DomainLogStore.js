/*
 * File: app/store/DomainLogStore.js
 */

Ext.define('webapp.store.DomainLogStore', {
    extend: 'Ext.data.Store',

    requires: [
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json',
        'Ext.data.field.Field'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'DomainLogStore',
            proxy: {
                type: 'ajax',
                url: 'resources/loglist.json',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            fields: [
                {
                    name: 'jobNo'
                },
                {
                    name: 'logDate'
                }
            ]
        }, cfg)]);
    }
});