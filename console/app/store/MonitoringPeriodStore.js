/*
 * File: app/store/MonitoringPeriodStore.js
 */

Ext.define('webapp.store.MonitoringPeriodStore', {
    extend: 'Ext.data.Store',
	fields: [
		{name:'displayValue'},
		{name:'idValue'}
	],
	data:[
	   {'displayValue': '1 minute ago', 'idValue': 1 },
       {'displayValue': '30 minutes ago', 'idValue': 30 },
	   {'displayValue': '1 hour ago', 'idValue': 60 },
	   {'displayValue': '2 hours ago', 'idValue': 120 },
	   {'displayValue': '6 hours ago', 'idValue': 360 },
	   {'displayValue': '1 day ago', 'idValue': 720}
    ]
});