/*
 * File: app/controller/TomcatInstWizardController.js
 */

Ext.define('webapp.controller.TomcatInstWizardController', {
    extend: 'Ext.app.Controller',

    control: {
		"ticWizard #step1": {
            activate: 'onStep1Activate'
        },
        "ticWizard #step2": {
            activate: 'onStep2Activate'
        },
        "ticWizard #step3": {
            activate: 'onStep3Activate'
        },
        "ticWizard #btnWCreateDs": {
            click: 'onDSCreateButtonClick'
        },
        "ticWizard #next-btn": {
            click: 'onNextButtonClick'
        },
        "ticWizard #prev-btn": {
            click: 'onPrevButtonClick'
        },
        "ticWizard": {
            changewindow: 'onWindowChangeWindow'
        },
		"ticWizard #keywordField": {
			specialkey: 'onFilteringServer'
		}
    },
	
	onStep1Activate: function(component, eOpts) {
        component.down("[name=catalinaHome]").setWidth(400);
		var form = component.down("#domainForm");
		//reload session server group
		form.down("[name='serverGroup']").getStore().getProxy().url = GlobalData.urlPrefix + "res/datagrid/group/list/notempty";
		form.down("[name='serverGroup']").getStore().load();
    },

    onStep2Activate: function(component, eOpts) {
        Ext.getCmp('next-btn').setText('저장 & 다음 &raquo;');
        var store = component.down('gridpanel').getStore();//.load();
        store.proxy.url = GlobalData.urlPrefix + 'res/ds/listna?domainId=' + Domain.id;
        store.reload();

        component.down('#domainLabel').setValue(Domain.name);
    },

    onStep3Activate: function(component, eOpts) {
        Ext.getCmp('next-btn').setText('설치하기');
		
		var serverStore = component.down('gridpanel').getStore();
		var url = GlobalData.urlPrefix + "domain/server/availist";
        serverStore.getProxy().url = url;
		serverStore.getProxy().extraParams = {"domainId": Domain.id};
		serverStore.load();
		
        component.down('#domainLabel').setValue(Domain.name);
    },

    onDSCreateButtonClick: function(button, e, eOpts) {
        Ext.create("widget.DatasourceWindow").show();
    },

    onNextButtonClick: function(button, e, eOpts) {

        var layout = button.up("panel").getLayout();//must be CardLayout


        var activeId = layout.getActiveItem().getItemId();

        if (activeId == 'step1') {

            this.saveDomainWithConfig(button, layout);

        } else if (activeId == 'step2') {

            this.saveDatasources(button, layout);

        } else if (activeId == 'step3') {


            var grid = button.up('window').down('#targetServerGrid');
            var s = grid.getSelectionModel().getSelection();

            if(s.length === 0) {
                MUtils.showWarn('Select servers!');
                return;
            }

			var verify = true;
            selected = [];
            Ext.each(s, function (item) {
			
				console.log('instName :' + item.data.instName );
				
                if(Ext.isEmpty(item.data.instName)) {
                    verify = false;
                    return false;
                } 
                selected.push({'domainId':Domain.id, 'serverId':item.data.id, 'name': item.data.instName});
            });
            //alert(Ext.JSON.encode(selected));
			
			if(verify === false){
				MUtils.showWarn('Instance Name을 입력해주세요.');
                return;
			}

            var ctr = this;
            MUtils.confirm('설치하시겠습니까?', function(btn, text){
                if (btn == 'yes'){
                    ctr.saveServersWithInstall(button, layout, selected);
                }

            });



        } else {
            alert("?? activeId = " + activeId);
        }
    },

    onPrevButtonClick: function(button, e, eOpts) {

        var layout = button.up("panel").getLayout();
        layout.prev();
        button.setDisabled(!layout.getPrev());
    },

    onWindowChangeWindow: function(window, activeItemId, domainModel, eventOptions) {
        this.setDomainModel(domainModel);
        window.getLayout().setActiveItem(activeItemId);

        if(activeItemId === 'step2'){
            window.setTitle('Add Datasources');
			window.down('#stepTitle').hide();
			window.down('#stepLabel1').hide();
        }else if(activeItemId === 'step3'){
            window.setTitle('Add TomcatInstances');
			window.down('#stepTitle3').hide();
			window.down('#stepLabel3').hide();
        }
        
        window.down('#deployFSet').hide();
        window.down('#prev-btn').hide();
        window.down('#next-btn').setText('save');
    },

    saveDomainWithConfig: function(button, layout) {
        var form = button.up('window').down('#domainForm');			// domain form
        var tform = button.up('window').down('#tomcatForm');

        var contr = this;

        if (form.isValid() && tform.isValid()) {
            var url = GlobalData.urlPrefix + "domain/saveWithConfig";

            Ext.Ajax.request({
                 url: url,
                 params: Ext.apply(form.getValues(), tform.getValues()),
                 success: function(resp, ops) {

					 webapp.app.getController("MenuController").loadDomainMenu();
                     Ext.mk.msg('작업결과','저장완료', button);
                     contr.setDomainModel(Ext.JSON.decode(resp.responseText).data);

                     contr.doNext(button, layout);
                 }
            });
        }
    },

    saveDatasources: function(button, layout) {
        var grid = button.up('window').down('#dsGrid');
        var s = grid.getSelectionModel().getSelection();

        var me = this;
		var isWizardSave = button.up('window').down('#stepTitle').isVisible();

        if(s.length === 0) {
			var amsg = "";
			if(isWizardSave){
				amsg = "다음에 설정하시겠습니까?";
				MUtils.confirm(amsg, function(btn, text){
					if (btn == 'yes'){
						me.doNext(button, layout);
					}
				});
			}
			else {
				amsg = "Datasource를 선택해주세요.";
				MUtils.showWarn(amsg);
			}
            return;
        }

        selected = [];
        Ext.each(s, function (item) {
            selected.push({'tomcatDomainId':Domain.id, 'datasourceId':item.data.id});
        });
        //alert(Ext.JSON.encode(selected));
		
		var saveUrl = GlobalData.urlPrefix + "domain/addDatasources";
		
		if(isWizardSave) {
			saveUrl = GlobalData.urlPrefix + "domain/saveFirstDatasources";
		}

        Ext.Ajax.request({
            url: saveUrl,
            headers: { 'Content-Type': 'application/json' },
            params: Ext.JSON.encode(selected),
            success: function(resp, ops) {

                if(isWizardSave){
                    me.doNext(button, layout);
                }else {

                    Ext.getCmp('domainDatasourceGrid').getStore().load({
						params: {	domainId: Domain.id}
					});
                    
                    button.up('window').close();

					var resData = Ext.JSON.decode(resp.responseText).data;
					var task = resData.task;
					if(task.id > 0){
						me.updateAllContextXml(resData.updatedXml.id, task.id);
						MUtils.showTaskWindow(task);
					}
                }
            }
        });
    },
	
	updateAllContextXml: function(configFileId, taskHistoryId) {
	
		var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		Ext.each(tomcats, function (tomcat) {
		
			Ext.Ajax.request({
				url: GlobalData.urlPrefix + "/provi/tomcat/updateXml/" + tomcat.id ,
				params: {'configFileId': configFileId, 'taskHistoryId': taskHistoryId},
				 timeout: 60000,
				success: function(resp, ops) {
                    //var resData = Ext.decode(resp.responseText).data;
					console.log('success add datasource ' + configFileId);
                }
            });
	    });
	},
	
    saveServersWithInstall: function(button, layout, selected) {

        Ext.MessageBox.show({
            msg: '설치 작업 등록중, please wait...',
            progressText: 'Saving...',
            width:300,
            wait:true,
            waitConfig: {interval:200}

        });
		var me = this;
        Ext.Ajax.request({
            url: GlobalData.urlPrefix + "tomcat/instance/saveList",
            headers: { 'Content-Type': 'application/json' },
            params: Ext.JSON.encode(selected),
            success: function(resp, ops) {
				var response = Ext.decode(resp.responseText);
				if(response.success) {
					var hidden = button.up('window').down('#stepTitle3').isHidden();
					button.up('window').close();
					
					Ext.MessageBox.hide();

					var task = response.data;
					if(task.id > 0){
						me.installTomcats(selected, task.id);
						MUtils.showTaskWindow(task, function(){
							if(hidden){
								webapp.app.getController("MenuController").loadTomcatMenu(GlobalData.lastSelectedMenuId);
								var url = GlobalData.urlPrefix + "domain/tomcatlist";
								var store = Ext.getCmp('associatedTomcatGridView').getStore();
								store.getProxy().url = url;
								store.getProxy().extraParams = {"domainId":GlobalData.lastSelectedMenuId};
								store.load();
							}
						});
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
            },
			failure: function(resp, ops) {
				var response = Ext.decode(resp.responseText);
				Ext.Msg.show({
					title: "Message",
					msg: response.msg,
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING
				});
			}
        });
    },

    init: function(application) {
        var Domain = {};// saved Domain Model Object.
    },

    doNext: function(button, layout) {
        if (layout.getNext()) {
            layout.next();
        }
        button.up("panel").down('#prev-btn').setDisabled(!layout.getPrev());
    },

    setDomainModel: function(domainModel) {
        Domain = domainModel;
    },
	
	installTomcats: function(tomcats, taskHistoryId) {
		Ext.each(tomcats, function (tomcat) {
		
			Ext.Ajax.request({
				url: GlobalData.urlPrefix + "/provi/tomcat/install" ,
				params: {'domainId': tomcat.domainId, 'serverId': tomcat.serverId, 'taskHistoryId': taskHistoryId},
				 timeout: 60000,
				success: function(resp, ops) {
                    //var resData = Ext.decode(resp.responseText).data;
					console.log('success tomcat install ' + tomcat.serverId);
                }
            });
	    });
	},
	
	onFilteringServer: function(field, e, eOpts) {
        if(e.getKey() === e.ENTER){
            var url = GlobalData.urlPrefix + "user/search";
			var store = field.up("gridpanel").getStore();
			var url = GlobalData.urlPrefix + "res/server/list";
			store.getProxy().url = url;
			store.load(
			{
				url: url,
				params: {
					"keyword": field.getValue()	
				}
			});
        }
    }

});
