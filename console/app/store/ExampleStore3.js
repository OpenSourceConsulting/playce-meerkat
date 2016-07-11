/*
 * File: app/store/ExampleStore3.js
 */

Ext.define('webapp.store.ExampleStore3', {
    extend: 'Ext.data.Store',

    requires: [
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json',
        'Ext.data.field.Field'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply(me.processExampleStore2({
            storeId: 'ExampleStore3',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            },
            fields: [
                {
                    name: 'name'
                },
                {
                    name: 'data1'
                },
                {
                    name: 'data2'
                },
                {
                    name: 'data3'
                },
                {
                    name: 'data4'
                },
                {
                    name: 'data5'
                },
                {
                    name: 'data6'
                },
                {
                    name: 'data7'
                },
                {
                    name: 'data8'
                },
                {
                    name: 'data9'
                }
            ]
        }), cfg)]);
    },

    processExampleStore2: function(config) {
        config.data = this.genData();
        return config;
    },

    genData: function(n, floor) {
    	var data = [],
        p = (Math.random() *  11) + 1,
        i;
        
	    floor = (!floor && floor !== 0)? 20 : floor;
	        
	    for (i = 0; i < (n || 12); i++) {
	        data.push({
	            name: Ext.Date.monthNames[i % 12],
	            data1: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data2: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data3: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data4: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data5: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data6: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data7: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data8: Math.floor(((Math.random() - 0.5) * 100), floor),
	            data9: Math.floor(((Math.random() - 0.5) * 100), floor)
	        });
	    }
	    return data;
    }

});