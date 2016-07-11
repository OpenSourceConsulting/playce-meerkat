/*
 * File: app/store/UserStore.js
 */

Ext.define('webapp.store.UserStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.UserModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'UserStore',
            model: 'webapp.model.UserModel',
            proxy: {
                type: 'ajax',
                url: 'user/list',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});