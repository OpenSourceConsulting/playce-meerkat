/*
 * File: app/store/ContextXmlConfigStore.js
 */

Ext.define('webapp.store.ContextXmlConfigStore', {
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
            storeId: 'ContextXmlConfigStore',
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