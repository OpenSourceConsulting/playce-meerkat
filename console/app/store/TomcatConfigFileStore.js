/*
 * File: app/store/TomcatConfigFileStore.js
 */

Ext.define('webapp.store.TomcatConfigFileStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.TomcatConfigFileModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'TomcatConfigFileStore',
            model: 'webapp.model.TomcatConfigFileModel',
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