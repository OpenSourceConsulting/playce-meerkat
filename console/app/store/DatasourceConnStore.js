/*
 * File: app/store/DatasourceConnStore.js
 */

Ext.define('webapp.store.DatasourceConnStore', {
    extend: 'Ext.data.JsonStore',
    storeId: 'DatasourceConnStore',
	autoLoad: false,
	proxy: {
        type: 'ajax',
        url: 'dashboard/get/jdbc/stats/2',
        reader: {
            type: 'json'
        }
    },
	fields: [
		{
			name: 'dsName'
		},
		{
			name: 'monValue'
		}
	]
});