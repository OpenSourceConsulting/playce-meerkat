/*
 * File: app/store/UserRoleStore.js
 */

Ext.define('webapp.store.UserRoleStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.UserRoleModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'UserRoleStore',
            model: 'webapp.model.UserRoleModel',
            proxy: {
                type: 'ajax',
                url: 'user/rolelist',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});