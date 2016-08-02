Ext.define('webapp.controller.TomcatController', {
    extend: 'Ext.app.Controller',

    control: {
        "#btnTomcatStart": {
            click: 'onBtnTomcatStartClick'
        },
        "#btnTomcatStop": {
            click: 'onBtnTomcatStopClick'
        },
        "#btnTomcatRestart": {
            click: 'onBtnTomcatRestartClick'
        },
        "#btnTestConnection": {
            click: 'onTestServerClick'
        },
        "#btnTomcatConfEdit": {
            click: 'onBtnTomcatConfEditClick'
        },
		"#btnTomcatConfSave": {
            click: 'onBtnTomcatConfSaveClick'
        },
        "#serverComboBox": {
            select: 'onServerComboBoxSelect'
        },
        "#tomcatTabs": {
            tabchange: 'onTomcatTabsTabChange'
        },
		"#tomcatVersionCombonbox":{
			select: 'onTomcatVersionComboboxChange'
		},
		"#TomcatInstanceContainerView": {
			activate: 'containerActivate'
		},		
		"#btnEditTomcatSubmit": {
			click: 'onEditTomcatSubmitClick'
		}
    },
	
	containerActivate: function(container, opts){
		this.initTomcatInstanceContainerView(GlobalData.lastSelectedMenuId);
	},

    onBtnTomcatStartClick: function(button, e, eOpts) {
         this.changeState(GlobalData.lastSelectedMenuId, 1);
    },

    onBtnTomcatStopClick: function(button, e, eOpts) {
         this.changeState(GlobalData.lastSelectedMenuId, 2);
    },

    onBtnTomcatRestartClick: function(button, e, eOpts) {
        this.changeState(GlobalData.lastSelectedMenuId, 3);
    },

    onTestServerClick: function(button, e, eOpts) {
        var form = Ext.getCmp('tomcatForm');
        var server = form.getForm().findField("serverComboBox");
        var serverId = server.getValue();
        var url = GlobalData.urlPrefix + "res/machine/testConnection";
        var serverStatus = Ext.getCmp("serverStatusDisplayField");
        var btnSubmit = Ext.getCmp("btnTomcatSubmit");
		webapp.app.getController("globalController").ajaxRequest(url, {"id": serverId}, "GET", function(json){
			var response = json.data;
             if (response === true){
                     serverStatus.setValue("Connection success!");
                     serverStatus.setFieldStyle("color:blue");
                     btnSubmit.enable();
			 }
			 else {
				 serverStatus.setValue("Connection fail!");
				 serverStatus.setFieldStyle("color:red");
				 btnSubmit.disable();
			 }
        });
    },

    onServerComboBoxSelect: function(combo, record, eOpts) {
        var testConnectionButton = Ext.getCmp("btnTestConnection");
        var sshUserDisplayField = Ext.getCmp("sshUserDisplayField");
        var bindIPAddressDisplayField = Ext.getCmp("bindIPAddressDisplayField");
        var otherBindIPAddressDisplayField = Ext.getCmp("otherBindIPAddressDisplayField");
        var id = combo.getValue();

        webapp.app.getController("ServerManagementController").getServer(id,function(data){
            testConnectionButton.setDisabled(false);
            sshUserDisplayField.setValue(data.sshUsername);
            bindIPAddressDisplayField.setValue(data.sshipaddr);
            otherBindIPAddressDisplayField.setValue(data.sshipaddr);
        });

    },

    onTomcatTabsTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        var activeTab = tabPanel.getActiveTab();
        var activeTabIndex =tabPanel.items.findIndex('id', activeTab.id);
        var tomcatId = GlobalData.lastSelectedMenuId;
        switch(activeTabIndex){
            case 0: //tomcat config tab
                this.displayTomcatConfig(tomcatId);
                break;
            case 1: //datasource tab
                this.displayDatasource(tomcatId);
                break;
            case 2: //server.xml tab
                this.displayServerXml(tomcatId);
                break;
            case 3: //context.xml tab
                this.displayContextXml(tomcatId);
                break;
            case 4: // application tab
                this.displayApps(tomcatId);
                break;
            case 5: //session tab
                this.displaySessions(tomcatId);
                break;
            default:
                this.displayTomcatConfig(tomcatId);
                break;
        }
    },
	
	onBtnTomcatConfEditClick: function(button, e, eOpts) {
		var tabs = Ext.getCmp('tomcatTabs');
		var form  = tabs.down("#tomcatForm");
		form.getForm().getFields().each (function (field) {
			field.setReadOnly(false); 
		});
		form.down("[name=catalinaHome]").setReadOnly(true);
		form.down("[name=catalinaBase]").setReadOnly(true);
		//hide current  button and show save button
		button.setHidden(true);
		tabs.down("#btnTomcatConfSave").setHidden(false);
	},
	
	onBtnTomcatConfSaveClick: function(button, e, eOpts) {
		var tabs = Ext.getCmp('tomcatTabs');
		var form  = tabs.down("#tomcatForm");
		var me = this;
		var tomcatId =  GlobalData.lastSelectedMenuId;
		form.getForm().baseParams = {"tomcatInstanceId":tomcatId};
        form.getForm().submit({
			url: GlobalData.urlPrefix + "tomcat/instance/conf/save",
            submitEmptyText: false,
            waitMsg: 'Saving Data...',
			success: function(formBasic, action){
				var response = Ext.decode(action.response.responseText);
				button.setVisible(false);
                tabs.down("#btnTomcatConfEdit").setVisible(true);
				formBasic.getFields().each (function (field) {
					field.setReadOnly (true); 
				});
				
				//reload menutree
				var domainId = response.data;
				var tree = Ext.getCmp('menuTreePanel');
				var domainNode = tree.getStore().getNodeById('tomcatMng_domain_' + domainId);
				var tomcatNode = domainNode.findChild('id','tomcatMng_domain_' + domainId + "_tomcat_" + tomcatId, true);
				webapp.app.getController("MenuController").reloadChildMenu(tree, 'tomcatMng_domain_' + domainId);
				tree.getSelectionModel().select(tomcatNode);
				//reload tomcat info
				var tomcatInfoForm = Ext.getCmp("tomcatInstanceContainer").down("#tomcatForm");
				me.displayTomcatInstance(tomcatInfoForm, tomcatId, function(data){
					if (data.state === 8 || data.state === 23) {
						Ext.getCmp("btnTomcatStart").disable();
						Ext.getCmp("btnTomcatStop").enable();
						//Ext.getCmp("btnTomcatRestart").enable();
					}else if(data.state === 7 || data.state === 22) {
						Ext.getCmp("btnTomcatStart").enable();
						Ext.getCmp("btnTomcatStop").disable();
						//Ext.getCmp("btnTomcatRestart").disable();
					}
				});
			}			
		});	
	},
	
	onTomcatVersionComboboxChange:  function(combo, record, eOpts) {
		combo.up("form").down("[name=tomcatVersionNm]").setValue(combo.getRawValue());
	},
	
    changeState: function(id, state) {
		var me = this;
        var url = GlobalData.urlPrefix;
        var newState = "";
        var action = "";
		var taskCdId = 116;
        if(state === 1) {// start
            url += "tomcat/instance/start";
            newState = "Started";
            action = "시작";
			taskCdId = 116;
        }
        else if(state===2) {//stop
            url += "tomcat/instance/stop";
            newState = "Stopped";
            action = "정지";
			taskCdId = 117;
        }
        else if(state === 3) {//restart
            url += "tomcat/instance/restart";
            newState = "Restarted";
            action = "restart";
        }
		console.log(id + " is " + action);
        Ext.MessageBox.confirm('Confirm', action +'하시겠습니까?', function(btn){
            if(btn == "yes"){

				Ext.Ajax.request({
					 url: GlobalData.urlPrefix + "task/create/tomcat/" + id,
					 params: {'taskCdId': taskCdId},
					 success: function(resp, ops) {
						var taskDetail = Ext.decode(resp.responseText).data;
						
						Ext.Ajax.request({
							url: url,
							params: {"id" : id, 'taskHistoryId': taskDetail.taskHistoryId},
							success: function(resp, ops) {
								/*
								var response = Ext.decode(resp.responseText);
								if(response.success === true){
									//MUtils.showInfo(response.msg);
									MUtils.showInfo(action + "되었습니다.");
								}
								else {
									MUtils.showWarn(response.msg);
								}
								*/
							}
						});
						Ext.create("widget.logViewWindow", {'taskDetailId' : taskDetail.id, closeCallback: function(){
							var form = Ext.getCmp('tomcatInstanceContainer').down("#tomcatForm");						
							me.displayTomcatInstance(form, id, function(data){
								if (data.state === 8 || data.state === 23) {
									Ext.getCmp("btnTomcatStart").disable();
									Ext.getCmp("btnTomcatStop").enable();
									//Ext.getCmp("btnTomcatRestart").enable();
								}else if(data.state === 7 || data.state === 22) {
									Ext.getCmp("btnTomcatStart").enable();
									Ext.getCmp("btnTomcatStop").disable();
									//Ext.getCmp("btnTomcatRestart").disable();
								}
							});
						}}).show();
					}
				});
			
                
            }
        });
    },

    displayTomcatInstance: function(form, id, callback) {
        var url = GlobalData.urlPrefix + "tomcat/instance/get";
		form.getForm().load({
			url: url,
			params:{"id":id},
			method: 'GET',
            waitMsg: 'Loading...',
			success: function(formBasic, action){
				var response = Ext.JSON.decode(action.response.responseText);
				callback(response.data);
			}
		});
    },

    displayTomcatConfig: function(tomcatId) {
        var url = GlobalData.urlPrefix + "tomcat/instance/"+tomcatId+"/conf";
		var tabs = Ext.getCmp('tomcatTabs');
		var form  = tabs.down("#tomcatForm");
		form.reset(true);
		form.getForm().getFields().each (function (field) {
			field.setReadOnly (true); 
		});
		form.getForm().load({
			url: url,
			params:{"id":id},
			method: 'GET',
            waitMsg: 'Loading...',
			success: function(formBasic, action){
				form.down("[name=tomcatVersionCd]").hide();
				form.down("[name=separator]").hide();
				form.down("[name=tomcatVersionNm]").hide();
			}
		});
    },

    displayDatasource: function(tomcatId) {
        var url = GlobalData.urlPrefix + "tomcat/instance/"+tomcatId+"/ds";
        var store = Ext.getCmp("tomcatDatasourcesGrid").getStore();
        store.getProxy().url = url;
        store.load();
    },

    displayServerXml: function(tomcatId) {
        var form  = Ext.getCmp("tomcatServerXmlTab").down("#configurationFileForm");
        webapp.app.getController("TomcatConfigFileController").displayConfigFile(tomcatId, GlobalData.objTypeTomcatInst, GlobalData.confFileServerXml, form);
    },

    displayContextXml: function(tomcatId) {
        var form  = Ext.getCmp("tomcatContextXmlTab").down("#configurationFileForm");
        webapp.app.getController("TomcatConfigFileController").displayConfigFile(tomcatId, GlobalData.objTypeTomcatInst, GlobalData.confFileContextXml, form);
    },

    displayApps: function(tomcatId) {
        var reqUrl = GlobalData.urlPrefix + "tomcat/instance/"+tomcatId+"/apps";
		
        var store = Ext.getCmp("tomcatApplicationGrid").getStore();
        store.getProxy().url = reqUrl;
        store.load();
    },

    displaySessions: function(tomcatId) {
		var url = GlobalData.urlPrefix + "tomcat/instance/"+tomcatId+"/sessions";
		var store = Ext.getCmp("tomcatSessionGrid").getStore();
		store.getProxy().url = url;
		store.load();
    },
	
	uninstallTomcat: function (tomcatId, domainId) {
		var me = this;
		Ext.MessageBox.confirm('Confirm', '설치된 Tomcat Instance를 삭제하시겠습니까? <br/><br/><label><input type="checkbox" id="deleteOnlyDB" /> DB 데이타만 삭제</label>', function(btn){
            if(btn == "yes"){
			
				if (document.getElementById('deleteOnlyDB').checked){
					me.deleteTomcat(tomcatId, domainId);
					return;
				}
			
				Ext.Ajax.request({
					url: GlobalData.urlPrefix + "/task/create/uninstall/" + tomcatId,
					method: 'POST',
					success: function(resp, ops) {
						var task = Ext.decode(resp.responseText).data;
						
						if(task.id > 0){
							Ext.Ajax.request({
								url: GlobalData.urlPrefix + "/provi/tomcat/uninstall/" +tomcatId ,
								params: {'taskHistoryId': task.id},
								timeout: 60000,
								success: function(resp, ops) {
									//var resData = Ext.decode(resp.responseText).data;
									console.log('success uninstall tomcat instance : ' + tomcatId);
								}
							});
							 
							 MUtils.showTaskWindow(task, function(){
								webapp.app.getController("MenuController").loadTomcatMenu(domainId);
								Ext.getCmp('associatedTomcatGridView').getStore().getProxy().extraParams = {domainId: domainId};
								Ext.getCmp('associatedTomcatGridView').getStore().load();
								//jump to parent domain
								var domainNodeId = 'tomcatMng_domain_'  + domainId;
								webapp.app.getController("MenuController").selectNode(domainNodeId);
								
								var centerContainer = Ext.getCmp("subCenterContainer");
								centerContainer.layout.setActiveItem("DomainContainer");
							 });
						 }
					}
				});
			}
		});

	},
	
	deleteTomcat: function(tomcatId, domainId) {
		var url = GlobalData.urlPrefix + "tomcat/instance/delete";
		webapp.app.getController("globalController").ajaxRequest(url, {"id": tomcatId}, "POST", function(json){		
			webapp.app.getController("MenuController").loadTomcatMenu(domainId);
			var store = Ext.getCmp('associatedTomcatGridView').getStore();
			store.getProxy().url = GlobalData.urlPrefix + "domain/tomcatlist";
			store.getProxy().extraParams = {
				domainId: domainId
			}
			store.load();
		},null);
	},
	
	initTomcatInstanceContainerView: function (tomcatId) {
		//reset tab active index 
		var container = Ext.getCmp("TomcatInstanceContainerView");
		container.down("tabpanel").setActiveItem(0);
		var form = Ext.getCmp('tomcatInstanceContainer').down("#tomcatForm");
		this.displayTomcatInstance(form, tomcatId, function(data){
			if (data.state === 8 || data.state === 23) {
						Ext.getCmp("btnTomcatStart").disable();
						Ext.getCmp("btnTomcatStop").enable();
						//Ext.getCmp("btnTomcatRestart").enable();
			}else if(data.state === 7 || data.state === 22) {
				Ext.getCmp("btnTomcatStart").enable();
				Ext.getCmp("btnTomcatStop").disable();
				//Ext.getCmp("btnTomcatRestart").disable();
			}
		});
		this.displayTomcatConfig(tomcatId);
	},

	editTomcat: function(tomcatId){
		var window = Ext.create("widget.EditTomcatInstanceWindow");
		var form = window.down("form");
		form.load({
			url: GlobalData.urlPrefix + "tomcat/instance/get",
			params: {"id": tomcatId},
			waitMsg: 'Loading...',
			method: "GET"
		});
		window.show();
	},

	onEditTomcatSubmitClick: function(button, eOpts) {
		var form = button.up("window").down("form");
		form.submit({
			url: GlobalData.urlPrefix + "tomcat/instance/saveEdit",
			waitMsg: 'Saving...',
			method: "POST",
			success: function(formBasic, action){
				var response = Ext.decode(action.response.responseText);
				if(response.success === true) {
					//reload grid
					Ext.getCmp("associatedTomcatGridView").getStore().reload();
					//reload tree menu
					var domainId = GlobalData.lastSelectedMenuId;
					var tree = Ext.getCmp('menuTreePanel');
					var domainNode = tree.getStore().getNodeById('tomcatMng_domain_' + domainId);
					webapp.app.getController("MenuController").reloadChildMenu(tree, 'tomcatMng_domain_' + domainId);
					tree.getSelectionModel().select(domainNode);
					button.up("window").close();
				}
			},
			failure: function(formBasic, action) {
				var response = Ext.decode(action.response.responseText);
				Ext.Msg.show({
					title: "Message",
					msg: response.msg,
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING
				});
			}
		});
	}
});