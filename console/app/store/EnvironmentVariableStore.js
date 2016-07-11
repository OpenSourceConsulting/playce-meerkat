/*
 * File: app/store/EnvironmentVariableStore.js
 */

Ext.define('webapp.store.EnvironmentVariableStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.EnvironmentVariableModel',
        'Ext.data.proxy.JsonP',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'EnvironmentVariableStore',
            model: 'webapp.model.EnvironmentVariableModel',
            proxy: {
                type: 'jsonp',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});