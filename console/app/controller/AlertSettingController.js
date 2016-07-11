/*
 * File: app/controller/AlertSettingController.js
 */

Ext.define('webapp.controller.AlertSettingController', {
    extend: 'Ext.app.Controller',
    alias: 'view.AlertSettingWindow',
	requires:[
		'webapp.view.MonitoringRealTimeTomcatChartWindow',
		'webapp.store.DomainAlertSettingStore'
	],
    control: {
		"#saveAlertBtn": {
			click: "saveAlertSettingBtnClick"
		}
    },

	showAlertSettingWindow: function(alertId) {
		var window = Ext.create("widget.AlertSettingWindow");
		var form = window.down("form");
		var url = GlobalData.urlPrefix + "monitor/alert/setting";
		
		form.load({
			url: url,
			params:{
				alertId: alertId
			},
			method:"GET",
			waitMsg: 'Loading...',
			success: function(formBasic, action){
				var response = Ext.JSON.decode(action.response.responseText);
				var combobox  = form.down("[name='thresholdOpCdId']");
				combobox.clearValue();
				combobox.getStore().load(function(){
					combobox.setValue(response.data.thresholdOpCdId);
				});
				window.show();
			}
		});
		
	},
	
	
	changeAlertStatus: function(gridpanel, alertId, currentStatus){
		var me = this;
		var action = "enable";
		if(currentStatus) {
			action = "disable";
		}
		
		Ext.MessageBox.confirm('Confirm', 'Are you sure you want to ' + action + ' this alert?', function(btn){
            if(btn == "yes"){
				var url = GlobalData.urlPrefix + "monitor/alert/setting/changeStatus";
				webapp.app.getController("globalController").ajaxRequest(url, {alertId: alertId, status: !currentStatus}, "POST", function(data){
					gridpanel.getStore().reload();
				});
			}
		});
	},
	
	changeAllAlertStatus: function(grid, objId, objType, status) {
		var me = this;
		var action = "enable";
		if(!status) {
			action = "disable";
		}
		var params = {
			"objId": objId, 
			"objType": objType,
			"status": status
		};
		Ext.MessageBox.confirm('Confirm', 'Are you sure you want to ' + action + ' all alert?', function(btn){
            if(btn == "yes"){
				var url = GlobalData.urlPrefix + "monitor/alert/setting/changeAllStatus";
				webapp.app.getController("globalController").ajaxRequest(url, params, "POST", function(data){
					grid.getStore().reload();
				});
			}
		});
	},

	saveAlertSettingBtnClick: function(button, eOpts) {
		var form = button.up("window").down("form");
		var me = this;
		var alertId = form.down("[name=id]");
		form.submit({
			url: GlobalData.urlPrefix + "monitor/alert/setting/save",
			submitEmptyText: false,
            waitMsg: 'Saving Data...',
			success: function(formBasic, action) {
				webapp.app.getStore("DomainAlertSettingStore").reload();
				Ext.Msg.show({
					title: "Message",
					msg: "Alert setting is changed. Please enable to affect this setting.",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.INFO
				});
				button.up("window").close();
			},
			failure: function(formBasic, action) {
				var response = Ext.JSON.decode(action.response.responseText);
				Ext.Msg.show({
					title: "Message",
					msg: response.msg,
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING
				});
			}
			
		});
	},
});
