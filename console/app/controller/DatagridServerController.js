/*
 * File: app/controller/DatagridServerController.js
 */

Ext.define('webapp.controller.DatagridServerController', {
    extend: 'Ext.app.Controller',
	requires: [
	 'webapp.model.ServerModel'
	 ],
    control: {
        "#dataGridServerGroupGrid": {
            select: 'onDatagridServerGroupGridSelect'
        },
        "#SessionServerGroupContainer": {
            activate: 'onSessionServerGroupContainerActivate'
        },
        "#datagridGroupTab": {
            tabchange: 'onDatagridGroupTabTabChange'
        },
        "#newServerGroupBtn":{
        	click: 'newServerGroupBtnClick'
        },
        "#newServerGroupWindowBtn":{
        	click:'showNewServerWindow'
        },
		"#btnSubmitServerGroup": {
			click: 'submitServerGroupClick'
		},
		"#linkNewServerBtn":{
			click: 'linkNewServerBtnClick'
		}
    },

    onDatagridServerGroupGridSelect: function(rowmodel, record, index, eOpts) {
        var id = record.get("id");
		var tab = Ext.getCmp("datagridGroupTab");
		tab.up("container").down("#messageField").setHidden(true);
		tab.setVisible(true);
		tab.setActiveItem(0);
        this.displayDatagridServers(id);
    },

    onSessionServerGroupContainerActivate: function(container, eOpts) {
        var grid = container.down("#dataGridServerGroupGrid");
        grid.getStore().load();
		grid.getSelectionModel().deselectAll();
		Ext.getCmp("datagridGroupTab").setVisible(false);
    },

    onDatagridGroupTabTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        var activeTab = tabPanel.getActiveTab();
        var activeTabIndex =tabPanel.items.findIndex('id', activeTab.id);
        var selectedRecords;
        selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
        var groupId = selectedRecords[0].get("id");
        switch(activeTabIndex){
            case 0://server list tab
                this.displayDatagridServers(groupId);
                break;
            case 1: //conf tab
                this.displayClusteringConf(groupId);
                break;
            default:
                break;
        }
    },

    displayDatagridServers: function(groupId) {
        var url = GlobalData.urlPrefix + "res/datagrid/list";
        var store = Ext.getCmp("datagirdServerGrid").getStore();
        store.getProxy().url = url;
        store.getProxy().extraParams = {groupId:groupId};
        store.load();
    },

    displayClusteringConf: function(groupId) {
		var form  = Ext.getCmp("clusteringConfigServerGroupTab").down("#clusteringConfForm");
		var selectedRecords;
        selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
        var groupId = selectedRecords[0].get("id");
		//load clustering conf
		var latestVersionUrl = GlobalData.urlPrefix + "clustering/config/latest";
		webapp.app.getController("globalController").ajaxRequest(latestVersionUrl, {"objectId": groupId, "objectType": GlobalData.objTypeSessionServerGroup}, "GET", function(json){
		  form.bindData(GlobalData.objTypeSessionServerGroup, groupId, json.data);
		}, null);
    },
    
    newServerGroupBtnClick: function(button, e, eOpts) {
		this.showServerGroupWindow("add", 0, true);
    },
	
	linkNewServerBtnClick: function(button, e, eOpts) {
		var selectedRecords;
        selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
        var groupId = selectedRecords[0].get("id");
		this.showServerGroupWindow("edit", groupId, false);
	},

	showServerGroupWindow: function(type, id, isEditGroupInfo) {
		var title = "New session server group";
		var window = Ext.create("widget.datagridservergroupwindow");
    	var grid = window.down("#sessionServerGrid");
		var serverListUrl = GlobalData.urlPrefix + "res/server/list";
		var serverStore = Ext.create('Ext.data.JsonStore',{
			storeId: 'serverWindowStore',
			proxy: {
				type: 'ajax',
				url: serverListUrl,
				reader: {
					type: 'json',
					rootProperty: 'list'
				}
			},
			model: Ext.create('webapp.model.ServerModel')
		});
		grid.setStore(serverStore);
		if(type === "edit"){
			title = "Edit session server group";
			var form = window.down("#datagridServerGroupForm");
			//window.down("[name=typeCdId]").getStore().load();            // ------->   don't need this. replace to autoLoadOnValue: true
			form.down("#groupIdHiddenField").setValue(id);
			form.getForm().load({
				url: GlobalData.urlPrefix + "res/datagrid/group/get",
				params:{"id":id},
				method:"GET"
			});
			serverStore.getProxy().url = GlobalData.urlPrefix + "res/datagrid/list/selected";
			serverStore.getProxy().extraParams = {"groupId":id};
			window.down("#btnSubmitServerGroup").setText("Save");
		}
		
		serverStore.load();
		if (isEditGroupInfo) {
			window.down("[name=typeCdId]").setReadOnly(false);
			window.down("[name=name]").setReadOnly(false);
		}
		else {
			window.down("[name=typeCdId]").setReadOnly(true);
			window.down("[name=name]").setReadOnly(true);
		}
		window.setTitle(title);
    	window.show();
	},
	
    showNewServerWindow: function(button, e, eOpts) {
    	webapp.app.getController("ServerManagementController").showServerWindow(0, "add", button.getId());
    },
	
	submitServerGroupClick: function(button, e, eOpts) {
		var window = button.up("window");
		var form = window.down("#datagridServerGroupForm");
		//window.down("[name=typeCdId]").getStore().load();
		
		if(form.isValid()){
			var name = form.down("[name=name]").getValue();
			var typeCdId = form.down("[name=typeCdId]").getValue();
			var id = form.down("#groupIdHiddenField").getValue();
			
			var items = form.down("grid").getStore();
			var sessionServers = [];
			items.each(function(rec){
				if(rec.get("selected") === true){
					sessionServers.push({'datagridServerGroupId':id, 'serverId': rec.get("id"), 'port': rec.get("port")});
				}
			});
			
			var me = this;
			Ext.Ajax.request({
				url: GlobalData.urlPrefix + "res/datagrid/group/save",
				params: {"id": id, "name":name,"typeCdId":typeCdId, "sessionServers": Ext.JSON.encode(sessionServers)},
                submitEmptyText: false,
                waitMsg: 'Saving Data...',
				success:  function(resp, ops) {
					var response = Ext.decode(resp.responseText);
					if(response.success === true){
						var groupGrid = Ext.getCmp("dataGridServerGroupGrid");
						var store = groupGrid.getStore();
						store.reload();
						var groupId = response.data;
						me.displayDatagridServers(groupId);
						button.up("window").close();
					}
				
			}
			});
		}
	},
    
    deleteDatagridServerGroup: function (id){
		 Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this server group?', function(btn){
            if(btn == "yes"){
				var url = GlobalData.urlPrefix + "res/datagrid/group/delete";
				webapp.app.getController("globalController").ajaxRequest(url, {"id":id}, "POST", function(json){
					var groupGrid = Ext.getCmp("dataGridServerGroupGrid");
					var store = groupGrid.getStore();
					store.reload();
				});
			}
		});
	},
	
	removeDatagridServerFromGroup: function(groupId, serverId){
		Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this server out of group?', function(btn){
            if(btn == "yes"){
				var url = GlobalData.urlPrefix + "res/datagrid/group/remove";
				webapp.app.getController("globalController").ajaxRequest(url, {"groupId":groupId, "serverId":serverId}, "POST", function(json){
					var grid = Ext.getCmp("datagirdServerGrid");
					grid.getStore().reload();
					var groupGrid = Ext.getCmp("dataGridServerGroupGrid");
					var store = groupGrid.getStore();
					store.reload();
				});
			}
		});
	}

});
