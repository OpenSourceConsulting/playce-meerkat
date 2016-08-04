/*
 * File: app/controller/ApplicationController.js
 */

Ext.define('webapp.controller.ApplicationController', {
    extend: 'Ext.app.Controller',

    control: {
        "#btnSubmitDeploy": {
            click: 'onBtnSubmitDeployClick'
        },
        "#mybutton37": {
            click: 'onMybutton37Click'
        },
        "#btnApplicationDeploy": {
            click: 'onBtnApplicationDeployClick'
        },
        "#btnApplicationStart": {
            click: 'onBtnApplicationStartClick'
        },
        "#btnApplicationRestart": {
            click: 'onBtnApplicationRestartClick'
        },
        "#btnApplicationStop": {
            click: 'onBtnApplicationStopClick'
        }
    },

    onBtnSubmitDeployClick: function(button, e, eOpts) {
        var form = button.up('form');			// domain form
        var window = button.up('window');
		var me = this;
		var redeploy = false;
        if(form.isValid()){
			var contextPath = form.down("[name='contextPath']").getValue();
			if(!contextPath.startsWith("/")) {
				contextPath = "/" + contextPath;
			}
			//temporarily validate
			var grid = Ext.getCmp("associatedApplicationListView");
			grid.getStore().each(function(record){
				if(record.data.contextPath === contextPath) {
					redeploy = true;
				}
			});
			
			if(redeploy) {
				MUtils.confirm("업데이트 하시겠습니까?", function(btn){
					if(btn === "yes") {
						me.saveApplication(form, {"update":true});
					}
				});
			}
			else {
				me.saveApplication(form, {"update":false});
			}
        }
    },
	
	saveApplication: function(form, params){
		var me = this;
		form.submit({
			url: GlobalData.urlPrefix + "application/deploy",
			params: params,
			waitMsg: "Deploying war file....",
			success: function(formBasic, action){
				var resData = Ext.JSON.decode(action.response.responseText).data;
				var task = resData.task;
				if(task.id > 0){
					me.deployWar(resData.applicationId, task.id);
					MUtils.showTaskWindow(task, function(){
						var appListStore = Ext.getCmp("associatedApplicationListView").getStore();
						appListStore.getProxy().url = GlobalData.urlPrefix + "domain/" + GlobalData.lastSelectedMenuId + "/apps";
						appListStore.load();
					});
				}
				form.up("window").close();
			},
			failure: function(formBasic, action){
				var json = Ext.JSON.decode(action.response.responseText);
				MUtils.showWarn(json.msg==""? "Deploy failed": json.msg);
			}
		});
	
	},

	deployWar: function(applicationId, taskHistoryId){
		
		 var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		 Ext.each(tomcats, function (tomcat) {
			Ext.Ajax.request({
				url: GlobalData.urlPrefix + "/provi/tomcat/deployWar/" + tomcat.id ,
				params: {'taskHistoryId': taskHistoryId, 'applicationId': applicationId},
				timeout: 60000,
				success: function(resp, ops) {
                    //var resData = Ext.decode(resp.responseText).data;
					console.log('success deploy war : ' + tomcat.get('ipaddress'));
                }
            });
	     });
		 
	},
	
	deleteApp: function(applicationId){
		var url = GlobalData.urlPrefix + "/application/undeploy"
		webapp.app.getController("globalController").ajaxRequest(url, {"Id": applicationId}, "POST", function(json){
			Ext.getCmp("associatedApplicationListView").getStore().getProxy().url = GlobalData.urlPrefix + "domain/" + GlobalData.lastSelectedMenuId + "/apps";
			Ext.getCmp("associatedApplicationListView").getStore().load();
		});
	},

    onMybutton37Click: function(button, e, eOpts) {
        var window = Ext.getCmp("deployWindow");
        window.close();
    },

    onBtnApplicationDeployClick: function(button, e, eOpts) {
        this.showDeployWindow();
    },

    onBtnApplicationStartClick: function(button, e, eOpts) {
        var selectedRecords=Ext.getCmp('associatedApplicationListView').getSelectionModel().getSelection();
        var appId = selectedRecords[0].get("id");
        this.changeState(appId,1);
    },

    onBtnApplicationRestartClick: function(button, e, eOpts) {
        var selectedRecords=Ext.getCmp('associatedApplicationListView').getSelectionModel().getSelection();
        var appId = selectedRecords[0].get("id");
        this.changeState(appId,3);
    },

    onBtnApplicationStopClick: function(button, e, eOpts) {
        var selectedRecords=Ext.getCmp('associatedApplicationListView').getSelectionModel().getSelection();
        var appId = selectedRecords[0].get("id");
        this.changeState(appId,2);
    },

    undeployApp: function(appId) { 
		var me = this;	
		var url = GlobalData.urlPrefix + "application/updateTask";
		var params = {"applicationId": appId, "taskCdId":105};
		var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		
		webapp.app.getController("globalController").ajaxRequest(url, params, "POST", function(json){
		
			Ext.each(tomcats, function (tomcat) {
				url = GlobalData.urlPrefix + "provi/tomcat/undeployWar/" + tomcat.id;
				params = {"appId":appId, "taskId": json.data.id};
				webapp.app.getController("globalController").ajaxRequest(url, params, "POST", function(json){
				}, null);
			});

			MUtils.showTaskWindow(json.data, function(){
				me.deleteApp(appId);
			});
		}, null);
		
    },

    showDeployWindow: function() {
        var window = Ext.create("widget.DeployWindow");
        window.down("[name=tomcatDomain]").setValue(GlobalData.lastSelectedMenuId);
        window.show();
    },

    changeState: function(appId, state) {
        var url = GlobalData.urlPrefix;
        if(state === 1) {// start
            url += "application/start";
        }
        else if(state===2) {//stop
            url += "application/stop";
        }
        else if(state === 3) {//restart
            url += "application/restart";
        }

        Ext.Ajax.request({
            url: url,
            params: {"id" : appId},
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                if(response.success === true){
                    Ext.getCmp('associatedApplicationListView').getSelectionModel().getSelection()[0].set("state",response.data);
                    if (response.data === 1) {
                        Ext.getCmp("btnApplicationStart").disable();
                        Ext.getCmp("btnApplicationStop").enable();
                        Ext.getCmp("btnApplicationRestart").enable();
                        Ext.getCmp("btnApplicationUndeploy").disable();
                    }else if (response.data === 2){
                        Ext.getCmp("btnApplicationStart").enable();
                        Ext.getCmp("btnApplicationStop").disable();
                        Ext.getCmp("btnApplicationRestart").disable();
                        Ext.getCmp("btnApplicationUndeploy").enable();

                    }
                }
                else {
                    Ext.Msg.show({
                        title: "Message",
                        msg: response.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
                }

            }
        });
    }

});
