/*
 * File: app/store/ExampleStore1.js
 */

Ext.define('webapp.store.ExampleStore1', {
    extend: 'Ext.data.JsonStore',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'ExampleStore1',
            fields: ['os', 'data1' ],
            data: [
                { os: 'Android', data1: 68.3 },
                { os: 'BlackBerry', data1: 1.7 },
                { os: 'iOS', data1: 17.9 },
                { os: 'Windows Phone', data1: 10.2 },
                { os: 'Others', data1: 1.9 }
            ]
        }, cfg)]);
    }


});