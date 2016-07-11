/*
 * File: app/store/TaskStatusTreeStore.js
 */

Ext.define('webapp.store.TaskStatusTreeStore', {
    extend: 'Ext.data.TreeStore',
	
    requires: [
		//'webapp.model.TaskTreeGridModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json',
        'Ext.data.field.*'
    ],

    storeId: 'TaskStatusTreeStore',
	//model: 'webapp.model.TaskTreeGridModel',
	autoLoad: false,//==> false이면 tree data 조회후 treepanel.expanAll() 해줘야 함.
	proxy: {
		type: 'ajax',
		url: 'resources/task-list.json'
	},
	folderSort: true,
	fields: [
		{
			name: 'taskDetailId'
		},
		{
			name: 'name'
		},
		{
			name: 'hostName'
		},
		{
			name: 'ipaddress'
		},
		{
			name: 'status'
		},
		{
			name: 'statusName'
		},
		{
			name: 'logFilePath'
		}
	]
});