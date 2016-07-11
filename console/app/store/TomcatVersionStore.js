/*
 * File: app/store/TomcatVersionStore.js
 */

Ext.define('webapp.store.TomcatVersionStore', {
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
            storeId: 'TomcatVersionStore',
            proxy: {
                type: 'ajax',
                url: 'code/list/tever',
                reader: {
                    type: 'json'
                }
            },
            fields: [
                {
                    name: 'id'
                },
                {
                    name: 'codeNm'
                }
            ]
        }, cfg)]);
    }
});