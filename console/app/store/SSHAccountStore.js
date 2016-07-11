/*
 * File: app/store/SSHAccountStore.js
 */

Ext.define('webapp.store.SSHAccountStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.SSHAccountModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'SSHAccountStore',
            model: 'webapp.model.SSHAccountModel',
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