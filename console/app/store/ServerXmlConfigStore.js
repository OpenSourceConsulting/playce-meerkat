/*
 * File: app/store/ServerXmlConfigStore.js
 */

Ext.define('webapp.store.ServerXmlConfigStore', {
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
            storeId: 'ServerXmlConfigStore',
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