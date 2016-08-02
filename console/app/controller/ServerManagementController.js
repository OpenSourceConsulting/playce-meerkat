/*
 * File: app/controller/ServerManagementController.js
 */

Ext.define('webapp.controller.ServerManagementController', {
    extend: 'Ext.app.Controller',

		control: {
        "#serverGrid": {
            select: 'onServerGridSelect',
			beforecellclick: function(view , td , cellIndex , record , tr , rowIndex , e , eOpts) {
                
				if(cellIndex >= 7) {
					console.log('cancel select event.');
					return false;
				}
				return true;
            }
        },
        "#showPasswordCheckbox": {
            change: 'onShowPasswordCheckboxChange'
        },
        "#serverEditBtn": {
            click: 'onServerEditBtnClick'
        },
        "#serverCancelBtn": {
            click: 'onServerCancelBtnClick'
        },
        "#serverSaveBtn": {
            click: 'onServerSaveBtnClick'
        },
        "#serverAddSSHAccountBtn": {
            click: 'onServerAddSSHAccountBtnClick'
        },
        "#serverEditSSHAccountBtn": {
            click: 'onServerEditSSHAccountBtnClick'
        },
        "#serverSubmitSSHBtn": {
            click: 'onServerSubmitSSHBtnClick'
        },
        "#sshAccountGrid": {
            select: 'onSshAccountGridSelect'
        },
        "#serverTestSSHAccountBtn": {
            click: 'onServerTestSSHAccountBtnClick'
        },
        "#newServerBtn": {
            click: 'onNewServerBtnClick'
        },
        "#testServerBtn": {
            click: 'onTestServerBtnClick'
        },
		"#serverTestSSHConnectionBtn": {
			click: 'onTestServerBtnClick'
		},
        "#submitServerBtn": {
            click: 'onSubmitServerBtnClick'
        },
        "#serverDeleteSSHAccountBtn": {
            click: 'onServerDeleteSSHAccountBtnClick'
        },
		"#ServerManagementContainer": {
			activate: 'containerActivate'
		},
		"#serverSSHPasswordTextField": {
			change: 'sshTextboxChange'
		},
		"#serverSSHUserIDTextField": {
			change: 'sshTextboxChange'
		},
		"#keywordTextField": {
			specialkey: 'onNameFiltering'
		}
    },

	containerActivate: function(container, opts){
		this.initServerManagementContainerView();
	},

    onServerGridSelect: function(rowmodel, record, index, eOpts) {
	
		var detailTab = Ext.getCmp("detailServerTab");
		var id = record.get("id");
		/*
		detailTab.up("container").down("#messageField").setHidden(true);
		detailTab.setVisible(true);
		*/
		var serverDetail = Ext.getCmp('serverDetailPanel');
		serverDetail.layout.setActiveItem(1);
		
		this.loadServerInfo(detailTab, id);
		this.loadSSHAccounts(detailTab, id);
		this.loadAlertSettings(detailTab, id);
    },
	
	loadServerInfo: function(view, id) {
		var infoForm = view.down("#infoForm");
		var sshIPField = infoForm.down("[name='sshNi']");
		var ipaddrUrl = GlobalData.urlPrefix + "res/server/"+id+"/nis";
		sshIPField.clearValue();
		sshIPField.getStore().getProxy().url = ipaddrUrl;
		sshIPField.getStore().load();
		infoForm.load({
			url: GlobalData.urlPrefix + "res/server/get",
			params:{id:id},
			method: "GET",
			waitMsg: 'Loading...',
			success: function(formBasic, action) {
				var response = Ext.JSON.decode(action.response.responseText);
				infoForm.down("[name=sshNi]").setValue(response.data.sshNiId);
			}
		});
	},
	
	loadSSHAccounts: function(view, id) {
		var sshGrid = view.down("[name='sshGrid']");
		var sshUrl = GlobalData.urlPrefix + "res/server/"+id+"/sshAccounts";
		sshGrid.getStore().getProxy().url = sshUrl;
        sshGrid.getStore().load();
	},
	
	loadAlertSettings: function(view, id) {
		var grid = view.down("#alertTab").down("gridpanel");
		var url = GlobalData.urlPrefix + "res/server/"+id+"/alertsettings";
		grid.getStore().getProxy().url = url;
        grid.getStore().load();
	},

    onShowPasswordCheckboxChange: function(field, newValue, oldValue, eOpts) {
        var passwordField = Ext.getCmp("serverSSHPasswordTextField");
        if(!newValue){
            passwordField.inputEl.dom.setAttribute('type', 'password');
            passwordField.inputEl.addCls('x-form-password');
        }
        else{
            passwordField.inputEl.dom.setAttribute('type', 'text');
            passwordField.inputEl.addCls('x-form-text');
            passwordField.inputEl.removeCls('x-form-password');
        }
    },

    onServerEditBtnClick: function(button, e, eOpts) {
		var selectedRecords=Ext.getCmp('serverGrid').getSelectionModel().getSelection();
		var tomcatNo = selectedRecords[0].get("tomcatInstanceNo");
		var sessionServerNo = selectedRecords[0].get("sessionServerNo");
		if(tomcatNo > 0 || sessionServerNo > 0) {
			MUtils.showWarn("The used server could not be edited.");
			return;
		}
		var form = button.up("tabpanel").down("#infoForm");
		this.setStateInfoForm(form, false);
    },

    onServerCancelBtnClick: function(button, e, eOpts) {
        var form = button.up("tabpanel").down("#infoForm");
		this.setStateInfoForm(form, true);
		//reset value
		this.loadServerInfo(button.up("tabpanel"), form.down("[name='id']").getValue());
    },

    onServerSaveBtnClick: function(button, e, eOpts) {
		var me = this;
		var url = GlobalData.urlPrefix + "res/server/save"
		var form = button.up("tabpanel").down("#infoForm");
		
		form.submit({
			url: url,
			submitEmptyText: false,
            waitMsg: 'Saving Data...',
			success: function(formBasic, action){
				var response = Ext.JSON.decode(action.response.responseText);
				webapp.app.getStore("ServerStore").reload();
				me.setStateInfoForm(form, true);
				Ext.Msg.show({
					title: "Message",
					msg: "Update successfully.",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.INFO
				});
				
			},
			failure: function(formBasic, action){
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
	
	setStateInfoForm: function(form, isDefault){
		var tab = form.up("tabpanel").down("#infoTab");
		var editBtn = tab.down("#serverEditBtn");
		var saveBtn = tab.down("#serverSaveBtn");
		var cancelBtn = tab.down("#serverCancelBtn");
		if(isDefault){
			editBtn.setVisible(true);
			saveBtn.hide();
			cancelBtn.hide();
		}
		else {
			editBtn.hide()
			saveBtn.setVisible(true);
			cancelBtn.setVisible(true);
		}
		
		var fields = form.getForm().getFields();
		fields.each(function(item){
			item.setReadOnly(isDefault);
		});
	},
	
    onServerAddSSHAccountBtnClick: function(button, e, eOpts) {
         //reset value
        var selectedRecords=Ext.getCmp('serverGrid').getSelectionModel().getSelection();
        var sshIPAddr = selectedRecords[0].get("sshIPAddr");
        var sshPort = selectedRecords[0].get("sshPort");
        var serverId = selectedRecords[0].get("id");
        var name = selectedRecords[0].get("name");
        this.showSSHAccountWindow(serverId, 0, name, sshIPAddr, sshPort, "", "",false, "add");
    },

    onServerEditSSHAccountBtnClick: function(button, e, eOpts) {
        var selectedServerRecords = Ext.getCmp('serverGrid').getSelectionModel().getSelection();
        var sshIPAddr = selectedServerRecords[0].get("sshIPAddr");
        var sshPort = selectedServerRecords[0].get("sshPort");
        var serverId = selectedServerRecords[0].get("id");
        var name = selectedServerRecords[0].get("name");

        var selectedSSHRecords = Ext.getCmp('sshAccountGrid').getSelectionModel().getSelection();
		if(selectedSSHRecords.length <= 0) {
			Ext.Msg.show({
                title: "Message",
                msg: "Please choose ssh account.",
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.WARNING
            });
			return;
		}
        var sshAccountId = selectedSSHRecords[0].get("id");
        var sshUserID = selectedSSHRecords[0].get("username");
        var sshPassword = selectedSSHRecords[0].get("password");
        var isRoot = selectedSSHRecords[0].get("root");
        this.showSSHAccountWindow(serverId, sshAccountId, name, sshIPAddr, sshPort, sshUserID, sshPassword, isRoot, "edit");

    },

    onServerSubmitSSHBtnClick: function(button, e, eOpts) {
        var userIDField = Ext.getCmp("serverSSHUserIDTextField");
        var passwordField = Ext.getCmp("serverSSHPasswordTextField");
        var sshAccountIdField = Ext.getCmp("sshAccountHiddenField");
        var serverIdField = Ext.getCmp("serverIDHiddenField");
        this.saveSSHAccount(sshAccountIdField.getValue(), userIDField.getValue(), passwordField.getValue(), serverIdField.getValue());

    },

    onSshAccountGridSelect: function(rowmodel, record, index, eOpts) {
        Ext.getCmp("serverEditSSHAccountBtn").setDisabled(false);
        Ext.getCmp("serverDeleteSSHAccountBtn").setDisabled(false);
        Ext.getCmp("serverTestSSHAccountBtn").setDisabled(false);
    },

    onServerTestSSHAccountBtnClick: function(button, e, eOpts) {

        var selectedServerRecords=Ext.getCmp('serverGrid').getSelectionModel().getSelection();
        var sshIPAddr = selectedServerRecords[0].get("sshIPAddr");
        var sshPort = selectedServerRecords[0].get("sshPort");

        var selectedSSHRecords = Ext.getCmp('sshAccountGrid').getSelectionModel().getSelection();
        var sshUserID = selectedSSHRecords[0].get("username");
        var sshPassword = selectedSSHRecords[0].get("password");

        this.testSSHConnection(sshUserID, sshPassword, sshIPAddr, sshPort, function(data){
		
            Ext.Msg.show({
                title: "Message",
                msg: data.msg,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.INFO
            });
        });
    },

    onNewServerBtnClick: function(button, e, eOpts) {
        this.showServerWindow(0, "add", button.getId());
    },

    onTestServerBtnClick: function(button, e, eOpts) {
	
		var form = button.up('window').down('form');
		var submitBtn = button.up('window').down("[name='submitBtn']")
		var loadMask = new Ext.LoadMask({
			msg    : 'Please wait...',
			target  : form
		});

		loadMask.show();
	
        var sshIPAddrField = form.down("[name='sshIPAddr']");
        var sshPortField = form.down("[name='sshPort']");
        var userIDField = form.down("[name='sshUserName']");
        var passwordField = form.down("[name='sshPassword']")

        this.testSSHConnection(userIDField.getValue(), passwordField.getValue(),
                               sshIPAddrField.getValue(), sshPortField.getValue(),
                               function(data){
									loadMask.hide();
                                   Ext.Msg.show({
                                       title: "Message",
                                       msg: data.msg,
                                       buttons: Ext.Msg.OK,
                                       icon: Ext.Msg.INFO
                                   });
								   submitBtn.setDisabled(false);
                               },
                              function(data){
							      loadMask.hide();
                                  submitBtn.setDisabled(true);
                              });
    },

    onSubmitServerBtnClick: function(button, e, eOpts) {
		var form = button.up("window").down("form");
		var url = GlobalData.urlPrefix + "res/server/save";
        if(form.isValid()) {
			form.submit({
				url: url,
				method: "POST",
				success: function(formBasic, action){
					var clickedButtonId = Ext.getCmp("parentButtonHiddenId").getValue();
					var clickedButton = Ext.getCmp(clickedButtonId);
					var window = clickedButton.up("window");
					if(window !== undefined) {
						window.down("#targetServerGrid").getStore().load();
					}
					else {
						clickedButton.up("gridpanel").getStore().reload();
					}
					button.up("window").close();
				},
				failure: function(formBasic, action){
					var response = Ext.JSON.decode(action.response.responseText);
					Ext.Msg.show({
						title: "Message",
						msg: response.msg,
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					});
				}
			});
		}
    },

    onServerDeleteSSHAccountBtnClick: function(button, e, eOpts) {
         Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this ssh account?', function(btn){

            if(btn == "yes"){
                var selectedSSHRecords = Ext.getCmp('sshAccountGrid').getSelectionModel().getSelection();
                var accountId = selectedSSHRecords[0].get("id");
                webapp.app.getController("ServerManagementController").deleteSSHAccount(accountId, function(){
					 var selectedServerRecords=Ext.getCmp('serverGrid').getSelectionModel().getSelection();
					 var serverId = selectedSSHRecords[0].get("id");
					 var sshGrid = Ext.getCmp("sshAccountGrid");
					 var sshUrl = GlobalData.urlPrefix + "res/server/"+serverId+"/sshAccounts";
					 sshGrid.getStore().getProxy().url = sshUrl;
					 sshGrid.getStore().clearData();
					 sshGrid.getStore().load();
                 });
             }
         });

    },

    loadEnvironmentVariables: function(machineId, callBack) {
        var url = GlobalData.urlPrefix + "res/machine/evlist";
        Ext.Ajax.request({
            url: url,
            params:{"machineId":machineId},
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                if(response.success){
                    callBack(response.data);
                }
                else{
                    Ext.Msg.show({
                        title: "Message",
                        msg: response.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
                }
            },
            method:"GET"
        });
    },

    loadEVRevisions: function(machineId, callback) {
        var url = GlobalData.urlPrefix + "res/machine/envrevisions";
        Ext.Ajax.request({
            url: url,
            params:{"machineId":machineId},
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                if(response.success){
                    callback(response.data);
                }
                else{
                    Ext.Msg.show({
                        title: "Message",
                        msg: response.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
                }
            },
            method:"GET"
        });
    },

    getServer: function(id, callback) {
        var url = GlobalData.urlPrefix + "res/machine/get";
        Ext.Ajax.request({
            url: url,
            params: {"id" : id},
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                if(response.success === true){
                    callback(response.data);
                }
                else {
                    Ext.Msg.show({
                        title: "Message",
                        msg: response.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
                }
            },
            method:"GET"
        });
    },

    showSSHAccountWindow: function(serverId, accountId, serverName, sshIPAddr, sshPort, sshUserID, sshPassword, isRoot, type) {
        var window = Ext.create("widget.SSHAccountWindow");
        var submitButton = Ext.getCmp("serverSubmitSSHBtn");

        var sshIPAddrField = Ext.getCmp("serverSSHIPAddressDisplayField");
        var sshPortField = Ext.getCmp("serverSSHPortDisplayField");
        var nameField = Ext.getCmp("serverNameDisplayField");
        var sshUserIDField = Ext.getCmp("serverSSHUserIDTextField");
        var sshPasswordField = Ext.getCmp("serverSSHPasswordTextField");
        var sshAccountIdField = Ext.getCmp("sshAccountHiddenField");
        var serverIdField = Ext.getCmp("serverIDHiddenField");

        sshIPAddrField.setValue(sshIPAddr);
        sshPortField.setValue(sshPort);
        nameField.setValue(serverName);
        sshAccountIdField.setValue(accountId);
        serverIdField.setValue(serverId);

        if (type === "edit"){
            window.setTitle("Edit SSH Account");
            submitButton.setText("Save");
            sshUserIDField.setValue(sshUserID);
            sshPasswordField.setValue(sshPassword);
        }
        window.show();

    },

    testSSHConnection: function(userID, password, ipAddr, port, successCallback, failCallback) {
        var url = GlobalData.urlPrefix + "res/server/testssh";
        var params = {"userID":userID, "password":password,"ipAddr":ipAddr,"port":port };
        webapp.app.getController("globalController").ajaxRequest(url,params,"GET", function(data){
           successCallback(data);
        }, function(data){
            failCallback(data);
        });
    },

    saveSSHAccount: function(id, username, password, serverId) {
		var url = GlobalData.urlPrefix + "res/server/updatessh";
		var params = {"Id":id, "username":username, "password":password, "server":serverId, "root":false};
		webapp.app.getController("globalController").ajaxRequest(url, params, "POST", function(data){
			 Ext.Msg.show({
					title: "Message",
					msg: data.msg,
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.INFO
			 });
			var window =Ext.getCmp("sshAccountWindow");
			window.close();
		});
    },

    deleteSSHAccount: function(accountId, callback) {
        var url = GlobalData.urlPrefix + "res/server/delssh";
        webapp.app.getController("globalController").ajaxRequest(url, {"id":accountId}, "POST", function(data){
            callback();
        });
    },

    showServerWindow: function(id, type, parentButtonId) {
        var window = Ext.create("widget.ServerWindow");
		Ext.getCmp("parentButtonHiddenId").setValue(parentButtonId);
        window.show();
    },
	
	initServerManagementContainerView: function(){
		var container = Ext.getCmp("ServerManagementContainer");
		container.down("#serverGrid").getStore().getProxy().url = GlobalData.urlPrefix + "res/server/list";
        container.down("#serverGrid").getStore().reload();
    },
	
	deleteServer: function(id) {
		Ext.MessageBox.confirm('Confirm', '삭제하시겠습니까?', function(btn){
			if(btn == "yes"){
				var url = GlobalData.urlPrefix + "/res/server/delete";
				webapp.app.getController("globalController").ajaxRequest(url, {"serverId":id}, "POST", function(data){
					var detailTab = Ext.getCmp("detailServerTab");
					detailTab.setVisible(false);
					Ext.getCmp("serverGrid").getStore().reload();
				});
			}
		});
	},
	
	sshTextboxChange: function( textfield, newValue, oldValue, eOpts ){
		textfield.up("window").down("#serverTestSSHConnectionBtn").setDisabled(false);
		textfield.up("window").down("#serverSubmitSSHBtn").setDisabled(true);
	},
	
	onNameFiltering: function(field, e, eOpts) {
        if(e.getKey() === e.ENTER){
            var store = Ext.getStore("ServerStore");
            var url = GlobalData.urlPrefix + "res/server/list";
			store.getProxy().url = url;
			/*store.getProxy.extraPrams = {
				"keyword": field.getValue()
			}*/
			store.load(
			{
				url: url,
				params: {
					"keyword": field.getValue()	
				}
			});
        }
    },
	
	installAgent: function(serverId, agentInstalled) {
	
		var installUrl = (agentInstalled)? 'provi/agent/reinstall/': 'provi/agent/install/';
		var msg = (agentInstalled)? 'agent를 재설치 하시겠습니까?': '설치하시겠습니까?';
		var taskCdId = (agentInstalled)? 112: 110; // reinstall or install
		
		Ext.MessageBox.confirm('Confirm', msg, function(btn){
            if(btn == "yes"){
			
				Ext.Ajax.request({
					 url: GlobalData.urlPrefix + 'task/create/agent',
					 params: {'serverId': serverId, 'taskCdId': taskCdId},
					 success: function(resp, ops) {
						var task = Ext.decode(resp.responseText).data;
						
						if(task.id > 0){
							Ext.Ajax.request({
								 url: GlobalData.urlPrefix + installUrl + serverId,
								 params: {'taskHistoryId': task.id},
								 success: function(resp, ops) {
									console.log('success ' + installUrl + serverId);
								}
							});
							
							MUtils.showTaskWindow(task, function(){
								Ext.getCmp("serverGrid").getStore().reload();
							});
						}
					}
				});
			
            }
        });
	}

});
