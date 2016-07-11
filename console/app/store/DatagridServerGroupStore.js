/*
 * File: app/store/DatagridServerGroupStore.js
 */

Ext.define('webapp.store.DatagridServerGroupStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.DatagridServerGroupModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'DatagridServerGroupStore',
            model: 'webapp.model.DatagridServerGroupModel',
            proxy: {
                type: 'ajax',
                url: 'res/datagrid/group/list',
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }, cfg)]);
    }
});