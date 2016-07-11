/*
 * File: app/store/TempoStore.js
 */

Ext.define('webapp.store.TempoStore', {
    extend: 'Ext.data.Store',

    requires: [
        'webapp.model.DomainModel',
        'Ext.data.field.Field'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'TempoStore',
            model: 'webapp.model.DomainModel',
            fields: [
                {
                    name: 'id'
                },
                {
                    name: 'name'
                }
            ]
        }, cfg)]);
    }
});