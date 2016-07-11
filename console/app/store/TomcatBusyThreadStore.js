/*
 * File: app/store/TomcatBusyThreadStore.js
 */

Ext.define('webapp.store.TomcatBusyThreadStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.TomcatBusyThreadModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'TomcatBusyThreadStore',
            model: 'webapp.model.TomcatBusyThreadModel',
            proxy: {
                type: 'ajax',
                url: 'tomcat/monitoring/busythreads?tomcatId=1',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});