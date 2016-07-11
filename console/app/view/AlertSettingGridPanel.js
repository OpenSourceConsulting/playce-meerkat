/*
 * File: app/view/AlertSettingGridPanel.js
 */

Ext.define('webapp.view.AlertSettingGridPanel', {
    extend: 'Ext.grid.Panel',
	emptyText: 'No settings',
    alias: 'widget.alertSettingGridPanel',
    itemId: 'alertSettingGridPanel',
	forceFit: true,
	scrollable: true,
	store: 'DomainAlertSettingStore',
	columns: [
		{
			xtype: 'gridcolumn',
			dataIndex: 'alertItemCdNm',
			text: 'Alert Item'
		},
		{
			xtype: 'gridcolumn',
			dataIndex: 'threshold',
			text: 'Alert 기준(Threshold)',
			renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
				if(record.get("alertItemCdId") == GlobalData.ALERT_AGENT_CD_ID) {
					return "Not Running";
				}
				return record.get("thresholdOpCdNm") + " " + record.get("thresholdValue") + "%" ;
			}
		},
		{
			xtype: 'gridcolumn',
			dataIndex: 'status',
			text: 'Status',									
			renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
				var str = value?"Enabled":"Disabled";
				var tooltip = value?"Click to disable alarm": "Click to enable alarm";
				var id = Ext.id();
				setTimeout(function() {
					var button = Ext.create('Ext.button.Button', {
						text: str,
						tooltip: tooltip,
						handler: function(){
							var id = record.get("id");
							webapp.app.getController("AlertSettingController").changeAlertStatus(view, id, value);
						}
					});
					button.setStyle('fontWeight', 'bold');
					if(!value) {
						button.setStyle('background', 'red !important');
					}
					if (Ext.get(id)) {
						button.render(Ext.get(id));
					}
				}, 1);
				return '<div id="' + id + '"></div>';
			}
		},
		{
			xtype: 'actioncolumn',
			sortable: true,
			dataIndex: 'status',
			menuDisabled: true,
			text: 'Actions',
			items: [
				{
					handler: function(view, rowIndex, colIndex, item, e, record, row) {
						webapp.app.getController("AlertSettingController").showAlertSettingWindow(record.get("id"), GlobalData.objTypeServer);
					},
					iconCls: 'icon-edit',
					tooltip: 'Modify setting',
					isDisabled : function(view, rowIndex, colIndex, item, record){
						return record.get("alertItemCdId") == GlobalData.ALERT_AGENT_CD_ID;
					}
				}
			]
		}
	],
	dockedItems:[
		{	
			xtype: 'pagingtoolbar',
			dock: 'bottom',
			displayInfo: true,
			store: 'DomainAlertSettingStore'
		},
		{	
			xtype: 'toolbar',
			dock: 'top',
			items:[
				{
					xtype: 'button',
					text: "Enable all",
					handler: function(button, eOpts){
						var grid = button.up("gridpanel");
						var objType = GlobalData.objTypeTomcatDomain;
						var objId = GlobalData.lastSelectedMenuId;
						if(objId === -1) {//server mnt
							objType = GlobalData.objTypeServer;
							var selectedRecords;
							selectedRecords = Ext.getCmp('serverGrid').getSelectionModel().getSelection();
							objId = selectedRecords[0].get("id");
							
						}
						webapp.app.getController("AlertSettingController").changeAllAlertStatus(grid, objId, objType, true);
					}
				},
				
				{
					xtype: 'button',
					text: "Disable all",
					handler: function(button, eOpts){
						var grid = button.up("gridpanel");
						var objType = GlobalData.objTypeTomcatDomain;
						var objId = GlobalData.lastSelectedMenuId;
						if(objId === -1) {//server mnt
							objType = GlobalData.objTypeServer;
							var selectedRecords;
							selectedRecords = Ext.getCmp('serverGrid').getSelectionModel().getSelection();
							objId = selectedRecords[0].get("id");
							
						}
						webapp.app.getController("AlertSettingController").changeAllAlertStatus(grid, objId, objType, false);
					}
				}
			]
		}
	]
});