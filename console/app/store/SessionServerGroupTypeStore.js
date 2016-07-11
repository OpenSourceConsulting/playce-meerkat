/*
 * File: app/store/ApplicationStore.js
 */

Ext.define('webapp.store.SessionServerGroupTypeStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.SessionServerGroupTypeModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'SessionServerGroupTypeStore',
            model: 'webapp.model.SessionServerGroupTypeModel',
            proxy: {
                type: 'ajax',
                url: 'res/datagrid/group/types',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});