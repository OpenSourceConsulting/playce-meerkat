/*
 * File: app/controller/DomainController.js
 */

Ext.define('webapp.controller.DomainController', {
    extend: 'Ext.app.Controller',

    control: {
        "#domainTabs": {
            tabchange: 'onDomainTabsTabChange'
        },
        "#btnSubmitNewDomain": {
            click: 'onSubmitNewDomainClick'
        },
        "#mybutton68": {
            click: 'onEditButtonClick'
        },
        "#editTomcatConfigBtn": {
            click: 'onEditTomcatConfigBtnClick'
        },
        "#saveTomcatConfigBtn": {
            click: 'onSaveTomcatCOnfigBtnClick'
        },
		
		"#cancelTomcatConfigBtn": {
			click: 'onCancelTomcatCOnfigBtnClick'
		},
        "#tomcatIntKeywordField": {
            specialkey: 'onTomcatIntKeywordFieldFiltering'
        },
		"#DomainContainer": {
			activate: 'containerActivate'
		},
		"#btnNewTomcat": {
            click: 'onAddTomcatButtonClick'
        },
		"#tomcatAddDSBtn": {
            click: 'onAddDatasourceButtonClick'
        },
		"#domainTomcatTab gridpanel" : {
			selectionchange: 'onTomcatInstancesGridSelect'
		},
		"#dTomcatStart" : {
			click: 'onTomcatStartMenuItemClick'
		},
		"#dTomcatStop" : {
			click: 'onTomcatStopMenuItemClick'
		},
		"#TomcatDomainContainer": {
			activate: "tomcatDomainContainerActivate"
		},
		"#associatedTomcatGridView": {
			render: 'onDomainTomcatGridRender'
		}
    },
	
	tomcatDomainContainerActivate: function(container, opts){
		container.down("gridpanel").getStore().reload();
	},
	
	// fire on domain menu click.
	containerActivate: function(container, opts){
		console.log('Domain containerActivate');
		
		this.checkLatestFailedTask(container);
		this.loadDomainInfo(GlobalData.lastSelectedMenuId);
	},
	
	checkLatestFailedTask: function(container){
		var warnLabel = container.down('#domainWarnMsg').hide();
		Ext.Ajax.request({
             url: GlobalData.urlPrefix + "task/latest/failed/" + GlobalData.lastSelectedMenuId,
             params: {},
             success: function(resp, ops) {
                    var failedTask = Ext.decode(resp.responseText).data;
                    
					if(Ext.isEmpty(failedTask) == false){
						warnLabel.setText('최근에 실패한 작업('+ failedTask.taskName +')이 존재합니다.').show();
					} 
                }
        });
	},
	
    onDomainTabsTabChange: function(tabPanel, newCard, oldCard, eOpts) {
		
		//var activeTabIndex =tabPanel.items.findIndex('id', activeTab.id);
		var activeTab = tabPanel.getActiveTab();
		var activeTabIndex = tabPanel.items.indexOf(activeTab);
		
		console.log('fire onDomainTabsTabChang : ' + activeTabIndex);
		
        var domainId = GlobalData.lastSelectedMenuId;
        switch(activeTabIndex){
            case 0: //tomcat list tab
                this.displayTomcatList(domainId);
                break;
            case 1: //tomcat conf tab
                this.displayTomcatConfig(newCard, domainId);
                break;
            case 2: //datasource tab
                this.displayDatasources(domainId);
                break;
            case 3: //server.xml tab
                this.displayServerXml(domainId);
                break;
            case 4: //context.xml tab
                this.displayContextXml(domainId);
                break;
            case 5: //applications tab
                this.displayApplications(domainId);
                break;
            case 6: //sessions tab
                this.displaySessions(domainId);
                break;
            case 7: //clustering conf tab
                this.displayClusteringConf(domainId);
                break;
			case 8: //log tab
                this.displayLogs(domainId);
                break;
			case 9: //display alert setting
				this.loadDomainAlertSettings(domainId);
				break;
            default:
                break;
        }

    },

    onSubmitNewDomainClick: function(button, e, eOpts) {
		
		var me = this;
        var form = button.up('window').down('form');			// domain form

        if (form.isValid()) {
			var url = GlobalData.urlPrefix + "domain/save";
			form.getForm().submit({
				url:url,
				submitEmptyText: false,
				waitMsg: 'Saving Data...',
				success: function(formBasic, action){
					var response = Ext.decode(action.response.responseText);
					
                    if(response.success === true){
                        webapp.app.getController("MenuController").loadDomainMenu();
                        Ext.getStore("DomainStore").reload();
                        webapp.app.getController("DomainController").loadDomainInfo(response.data.id);
						if(response.data.tomcatInstancesCount > 0){
							me.handleSessionClustering(response.data, form, function(){
								form.up("window").close();
							});
						} else {
							
							form.up("window").close();
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
				failure: function(formBasic, action){
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

    },

    onEditButtonClick: function(button, e, eOpts) {
        this.showDomainWindow("edit",GlobalData.lastSelectedMenuId);
    },
	
    onEditTomcatConfigBtnClick: function(button, e, eOpts) {
        var tabs = Ext.getCmp('domainTabs');
		
		//show/hide btn
        tabs.down("#saveTomcatConfigBtn").setVisible(true);
		tabs.down("#cancelTomcatConfigBtn").setVisible(true);
        button.setVisible(false);
		
		var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		var form = tabs.down('form');
		var idValue = form.down("[name=id]").getValue();
		form.getForm().getFields().each (function (field) {
		
			if(idValue === ''){
				field.setReadOnly (false); 
			} else	if(field.getName() !== 'catalinaHome' && (tomcats.length == 0 || field.getName() !== 'catalinaBase')){
				// catalinaBase 는 tomcats.length == 0 때만 수정 가능.
				field.setReadOnly (false); 
			}
			
		});
    },
	onCancelTomcatCOnfigBtnClick: function(button, e, eOpts) {
		var tabs = Ext.getCmp('domainTabs');
		
		//show/hide btn
        tabs.down("#saveTomcatConfigBtn").setVisible(false);
		tabs.down("#editTomcatConfigBtn").setVisible(true);
        button.setVisible(false);
		
		var form = tabs.down('form');
		var idValue = form.down("[name=id]").getValue();
		form.getForm().getFields().each (function (field) {
		
			if(idValue === ''){
				field.setReadOnly (true); 
			} else	if(field.getName() !== 'catalinaHome' && field.getName() !== 'catalinaBase'){
				field.setReadOnly (true); 
			}
			
		});
		
	},
    onSaveTomcatCOnfigBtnClick: function(button, e, eOpts) {
		var me = this;
		var form = button.up('panel').down('form');
		var jmxEnable = form.getForm().findField('jmxEnable').getValue();
		var changeRMI = false;
		
		if(Ext.isEmpty(form.jmxEnable) == false){
			changeRMI = form.jmxEnable != jmxEnable;
			console.log('changeRMI = ' + changeRMI);
		}
		
		form.getForm().baseParams = {'tomcatDomain': GlobalData.lastSelectedMenuId, 'changeRMI' : changeRMI};
        form.getForm().submit({
			url: GlobalData.urlPrefix + "domain/conf/save",
            submitEmptyText: false,
            waitMsg: 'Saving Data...',
			success: function(formBasic, action){
				var response = Ext.JSON.decode(action.response.responseText);
				if(response.success) {
					var catalinaHome = form.down("[name='catalinaHome']");
					var tomcatVersionName = form.down("[name='tomcatVersionNm']").getValue();
					if(tomcatVersionName !== "") {
						catalinaHome.setValue(catalinaHome.getValue() + "/" + tomcatVersionName);
					}
					
					button.setVisible(false);
					button.up('panel').down("#editTomcatConfigBtn").setVisible(true);
					button.up('panel').down("#cancelTomcatConfigBtn").setVisible(false);
					form.down("[name='id']").setValue(response.data.configId);
					form.down("[name='tomcatVersionNm']").setValue("");
					form.down("[name='separator']").setVisible(false);
					formBasic.getFields().each (function (field) {
						field.setReadOnly (true); 
					});
				
					var task = response.data.task;
					if(task.id > 0){
						me.updateTomcatConfig(changeRMI, task.id);
						MUtils.showTaskWindow(task);
					}
					me.tabDisable(false);
				}
				else{
					Ext.Msg.show({
						title: "Message",
						msg: response.msg,
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING
					});
				}
			}
        });

    },
	
	updateTomcatConfig: function(changeRMI, taskHistoryId){
		
		 var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		 Ext.each(tomcats, function (tomcat) {
			Ext.Ajax.request({
				url: GlobalData.urlPrefix + "/provi/tomcat/updateConfig/" + tomcat.id ,
				params: {'taskHistoryId': taskHistoryId, 'changeRMI': changeRMI},
				timeout: 60000,
				success: function(resp, ops) {
                    //var resData = Ext.decode(resp.responseText).data;
					console.log('success update config : ' + tomcat.get('ipaddress'));
                }
            });
	     });
		 
	},
	
	onAddTomcatButtonClick: function(button, e, eOpts) {
	   
	   this.showTomcatInstanceWizardWindow('step3');
    },
	
	onAddDatasourceButtonClick: function(button, e, eOpts) {
	
		this.showTomcatInstanceWizardWindow('step2'); 
    },
	
	showTomcatInstanceWizardWindow: function(stepItemId){
		Ext.create('widget.ticWizard').show(undefined, function(){
            var domain =  {'id': GlobalData.lastSelectedMenuId, 'name': Ext.getCmp('domainNameField').getValue()};
            this.fireEvent('changewindow', this, stepItemId, domain);
        });
	},
	
	deleteTomcatDatasource: function(domainId, dsId){
		
		var me = this;
		
		Ext.Ajax.request({
			url: GlobalData.urlPrefix + "/domain/datasource/rmupdate",
			params: {'domainId': GlobalData.lastSelectedMenuId, 'dsId': dsId},
			success: function(resp, ops) {
				var resData = Ext.decode(resp.responseText).data;
				var task = resData.task;
				if(task.id > 0){
					var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
					 Ext.each(tomcats, function (tomcat) {
						Ext.Ajax.request({
							url: GlobalData.urlPrefix + "/provi/tomcat/updateXml/" + tomcat.id ,
							params: {'configFileId': resData.updatedXml.id, 'taskHistoryId': task.id},
							timeout: 60000,
							success: function(resp, ops) {
								//var resData = Ext.decode(resp.responseText).data;
								console.log('success update context.xml : ' + tomcat.get('ipaddress'));
							}
						});
					 });
					 
					 MUtils.showTaskWindow(task, function(){
						me.deleteDatasource(domainId, dsId);
					 });
				 }
				 
			}
		});
		
	},
	
	deleteDatasource: function(domainId, dsId){

		Ext.Ajax.request({
             url: GlobalData.urlPrefix + "domain/datasource/delete",
             params: {'domainId': domainId, 'dsId': dsId},
             success: function(resp, ops) {
               Ext.getCmp('domainDatasourceGrid').getStore().load();
            }
        });
		
	},

    onTomcatIntKeywordFieldFiltering: function(field, e, eOpts) {

        if(e.getKey() === e.ENTER){
            var store = Ext.getCmp("associatedTomcatListView").getStore();
            store.getProxy().url = GlobalData.urlPrefix + "domain/" + GlobalData.lastSelectedMenuId + "/tomcat/search/" + field.getValue();
            store.reload();
        }
    },
	
	installJDBCDriverButtonClick: function(fileType, taskCdId) {
		
		Ext.MessageBox.confirm('Confirm', '모든 인스턴스에 설치됩니다. 설치하시겠습니까?', function(btn){

            if(btn == "yes"){
			
                Ext.Ajax.request({
					url: GlobalData.urlPrefix + "/task/create",
					params: {'domainId': GlobalData.lastSelectedMenuId, 'taskCdId': taskCdId},
					success: function(resp, ops) {
						var task = Ext.decode(resp.responseText).data;
						
						if(task.id > 0){
							var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
							 Ext.each(tomcats, function (tomcat) {
								Ext.Ajax.request({
									url: GlobalData.urlPrefix + "/provi/tomcat/installJDBCDriver/" + tomcat.id ,
									params: {'taskHistoryId': task.id, 'fileName': fileType},
									timeout: 60000,
									success: function(resp, ops) {
										//var resData = Ext.decode(resp.responseText).data;
										console.log('success install jdbc driver : ' + tomcat.get('ipaddress'));
									}
								});
							 });
							 
							 MUtils.showTaskWindow(task);
						 }
						 
					}
				});
            }
        });
	},
	
	onTomcatInstancesGridSelect: function( selectionModel, selected, eOpts ) {
		//alert('change.');
		var startMenu = Ext.getCmp("dTomcatStart");// Ext.menu.Item
		var stopMenu = Ext.getCmp("dTomcatStop");
		
		if(selected.length === 0) {
			startMenu.disable();
			stopMenu.disable();
			return;
        }
		
		var runningCount = 0;
		var stopCount = 0;
		
		Ext.each(selected, function (item) {
			if (item.get('state') == GlobalData.tomcatStateRunning) {
				runningCount++;
			//} else if (item.get('state') == GlobalData.tomcatStateStop) {
			} else {
				stopCount++;
			} 
	    });
		console.log("runningCount : " + runningCount + ", stopCount : " + stopCount);
		if(runningCount > 0 && stopCount == 0){
			startMenu.disable();
			stopMenu.enable();
			console.log("stop menu enable");
		} else if(runningCount == 0 && stopCount > 0){
			startMenu.enable();
			stopMenu.disable();
			console.log("start menu enable");
		} else {
			startMenu.disable();
			stopMenu.disable();
			console.log("all menu disable");
		}
	},
	
	onTomcatStartMenuItemClick: function(item, e, eOpts) {
		this.startStopTomcatInstances(116, GlobalData.urlPrefix + 'tomcat/instance/start', '시작', item);
	},
	
	onTomcatStopMenuItemClick: function(item, e, eOpts) {
		this.startStopTomcatInstances(117, GlobalData.urlPrefix + 'tomcat/instance/stop', '정지', item);
	},
	
	onDomainTomcatGridRender: function(grid , eOpts){
		console.log('onDomainTomcatGridRender');
		var me = this;
		grid.getStore().on("load", function(store , records , successful , operation , eOpts){
			console.log('domain tomcat grid load!!');
			var tomcats = store.getRange();
			var allDone = true;
			 Ext.each(tomcats, function (tomcat) {
				var state = tomcat.get('state');
				
				if(state > 8 && state < 22){
					allDone = false;
					return false; //break each.
				}
			 });
			 console.log("tomcat allDone = " + allDone);
			 if (allDone === false) {
				setTimeout(function(){
					me.reloadGridQuietly(grid);
				}, 5000);
			} else {
				console.log('domain tomcat grid all done!!');
			}
		});
	},
	
	startStopTomcatInstances: function(taskCdId, actionUrl, action, clickItem) {
	
		var grid = Ext.getCmp('associatedTomcatGridView');
		//console.log(Ext.getClassName(grid));
		var selections = grid.getSelectionModel().getSelection();

		if(selections.length === 0) {
			MUtils.showWarn(action +'할 톰캣 인스턴스를 선택해주세요.');
			return;
		}
		
		var selected = [];
		Ext.each(selections, function (item) {
			selected.push({'id': item.data.id});// tomcat instance id.
		});
	
		var me = this;
		Ext.MessageBox.confirm('Confirm', action +'하시겠습니까?', function(btn){
            if(btn == "yes"){
			
				Ext.Ajax.request({
					 url: GlobalData.urlPrefix + "task/create/tomcats/" + taskCdId,
					 headers: { 'Content-Type': 'application/json' },
					 params: Ext.JSON.encode(selected),
					 success: function(resp, ops) {
						var task = Ext.decode(resp.responseText).data;
						
						if(task.id > 0){
							Ext.each(selected, function (record) {
			
								Ext.Ajax.request({
									url: actionUrl,
									params: {'id' : record.id, 'taskHistoryId': task.id}
								});
								console.log(record.id + ' ' + action);
							});
							
							setTimeout(function(){
								me.reloadGridQuietly(grid);
							}, 600);
						
						}
					}
				});

            }
        });
	},
	
	reloadGridQuietly: function(gridObj, callback) {
		gridObj.view.loadMask.maskOnDisable = false;
		var emptyLoadMask = gridObj.view.loadMask.disable();
		gridObj.store.load(function(){
			emptyLoadMask.enable();
			gridObj.getSelectionModel().deselectAll();
		});
	},

    showDomainWindow: function(type, id) {

		var me = this;
        var domainWindow = Ext.create("widget.DomainWindow");
        var submitButton = Ext.getCmp("btnSubmitNewDomain");
        //load server group list
        Ext.getStore("DatagridServerGroupStore").load();

        if (type === "edit"){
            domainWindow.setTitle("Edit Domain");
            submitButton.setText("Save");
            var form = domainWindow.down("#domainForm");			// domain form
			form.getForm().load({
				url: GlobalData.urlPrefix + "domain/get",
				method : 'GET',
				params: {
					id: id
				},
				waitMsg: 'Loading...',
				success: function(formBasic, action){
					var response = Ext.JSON.decode(action.response.responseText);
					form.down("[name=serverGroup]").setValue(response.data.dataGridServerGroupId);
					
					me.setOldDomainInfo(response.data);// save original data. see 'handleSessionClustering' function.
				}
			})
        } else {
			me.setOldDomainInfo( {'id': 0, name: '', 'dataGridServerGroupId': 0});
		}

        domainWindow.show();
    },
	
	/*
	 *  save original data. see 'handleSessionClustering' function.
	 */
	setOldDomainInfo: function(domainObj){
		this.oDomain = domainObj;
		console.log('save original domain data (DomainController.oDomain)');
		console.log(this.oDomain);
	},
	
	/*
	 * serverGroup is dataGridServerGroupId.
	 */
	handleSessionClustering: function(savedDomain, loadingTarget, callback){
		if( !(this.oDomain.dataGridServerGroupId > 0) && savedDomain.dataGridServerGroupId > 0){
			this.configureSessionClustering(savedDomain.id, loadingTarget, callback);
			
		} else if(this.oDomain.dataGridServerGroupId > 0 && savedDomain.dataGridServerGroupId === 0){
			this.unConfigureSessinClustering(savedDomain.id, loadingTarget, callback);
		} else {
			console.log('this.oDomain.dataGridServerGroupId : '+ this.oDomain.dataGridServerGroupId +',domainId: '+ savedDomain.id+', serverGroup : ' + savedDomain.dataGridServerGroupId);
		}
	},
	
	configureSessionClustering: function(domainId, loadingTarget, callback){
		
		var loadMask = new Ext.LoadMask({
			msg    : '세션 클러스터링 설정중입니다. 잠시만 기다려주세요...',
			target  : loadingTarget
		});

		loadMask.show();
		
		Ext.Ajax.request({
			url: GlobalData.urlPrefix + "/provi/session/cluster/configure",
			params: {'domainId': domainId},
			success: function(resp, ops) {
					var resData = Ext.decode(resp.responseText).data;
					
					loadMask.hide();
					MUtils.showInfo('세션 클러스터링 설정완료.');
					if (typeof callback === "function") {
						callback();
					}
			},
			failure: function(resp, opts) {
				loadMask.hide();
			}
		});
	},
	
	unConfigureSessinClustering: function(domainId,  loadingTarget, callback){
		var loadMask = new Ext.LoadMask({
			msg    : '세션 클러스터링 설정 제거중입니다. 잠시만 기다려주세요...',
			target  : loadingTarget
		});

		loadMask.show();
		
		Ext.Ajax.request({
			url: GlobalData.urlPrefix + "/provi/session/cluster/unconfigure",
			params: {'domainId': domainId},
			success: function(resp, ops) {
					var resData = Ext.decode(resp.responseText).data;
					
					loadMask.hide();
					MUtils.showInfo('세션 클러스터링 제거완료.');
					if (typeof callback === "function") {
						callback();
					}
			},
			failure: function(resp, opts) {
				loadMask.hide();
			}
		});
	},
	
    loadDomainInfo: function(domainId) {
		var me = this;
        var domainForm = Ext.getCmp('domainViewForm');
		this.tabDisable(false);

		GlobalData.lastSelectedMenuId = domainId;
		
        domainForm.getForm().load({
			url: GlobalData.urlPrefix + "domain/get",
			params: {
				id: domainId
			},
            waitMsg: 'Loading...',
			success: function(formBasic, action){

				var response = Ext.decode(action.response.responseText);
				me.setOldDomainInfo(response.data);
				
				var tabpanel = Ext.getCmp("domainTabs");
				
				var activeTab = tabpanel.getActiveTab();
				var activeTabIndex = tabpanel.items.indexOf(activeTab);
				
				if(Ext.isEmpty(response.data.domainTomcatConfig)){
					me.tabDisable(true);
					
				}else if(activeTabIndex > 0) {
					tabpanel.layout.setActiveItem(0);
					
				} else {
					me.displayTomcatList(domainId);
				}

				//hide/show clustering config tab
				if (response.data.clustering) {
					tabpanel.child("#sessionTab").tab.show();
				}
				else {
					tabpanel.child("#sessionTab").tab.hide();
				}
				
				domainForm.down('#scouterInstallBtn').setDisabled(Ext.isEmpty(response.data.scouterAgentInstallPath) == false);
				domainForm.down('#scouterConfigViewBtn').setDisabled(Ext.isEmpty(response.data.scouterAgentInstallPath));
			}
        });
		
    },
	
	tabDisable: function(disable){
		var me = this;
		var tabpanel = Ext.getCmp("domainTabs");
		tabpanel.items.each(function(tab){
		
			//console.log('tab id = ' + tab.getId());
			if(disable && tab.getId() == 'domainTomcatConfigTab'){
				tabpanel.setActiveTab(tab);
				me.resetTomcatForm(tab.down('form'));
				
				return true;
			}
			tab.setDisabled(disable);
		});
	},
	
	resetTomcatForm: function(tomcatForm){
	
		tomcatForm.reset(true);
		tomcatForm.down("[name=tomcatVersionCd]").setVisible(true);
		tomcatForm.down("[name='separator']").setVisible(true);
		tomcatForm.down("[name=tomcatVersionNm]").setVisible(true);
	},
	
    deleteDomain: function(domainId) {
        Ext.MessageBox.confirm('Confirm', '삭제하시겠습니까?', function(btn){
             if(btn == "yes"){
                Ext.Ajax.request({
                    url: GlobalData.urlPrefix + "domain/delete",
                    params: {"domainId":domainId},
                    success: function(resp, ops) {

                        var response = Ext.decode(resp.responseText);
                        if(response.success){
							webapp.app.getController("MenuController").loadDomainMenu();
							//redirect to domain grid container
							var centerContainer = Ext.getCmp("subCenterContainer");
							centerContainer.layout.setActiveItem("TomcatDomainContainer");
							webapp.app.getStore("DomainStore").reload();
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
    },

	/*
    saveTomcatConfig: function(params, successCallback, failCallback) {
		
		// TODO 사용중인 method 인지 확인 필요.
		
        var url = GlobalData.urlPrefix + "domain/conf/save";
        webapp.app.getController("globalController").ajaxRequest(url, params, "POST", function (data){
            successCallback(data);
         }, function(data){
            failCallback(data);
         });
    },
	*/
    displayServerXml: function(domainId) {
        var form  = Ext.getCmp("domainTomcatServerXmlTab").down("#configurationFileForm");
        webapp.app.getController("TomcatConfigFileController").displayConfigFile(domainId, GlobalData.objTypeTomcatDomain, GlobalData.confFileServerXml, form);
    },

    displayContextXml: function(domainId) {
        var form  = Ext.getCmp("domainTomcatContextXmlTab").down("#configurationFileForm");
        webapp.app.getController("TomcatConfigFileController").displayConfigFile(domainId, GlobalData.objTypeTomcatDomain, GlobalData.confFileContextXml, form);
    },

    displayTomcatConfig: function(panel, domainId) {
		panel.down("#cancelTomcatConfigBtn").setVisible(false);
		panel.down("#saveTomcatConfigBtn").setVisible(false);
		panel.down("#editTomcatConfigBtn").setVisible(true);
		
		var form = panel.down('form');
		this.resetTomcatForm(form);
		
		form.getForm().getFields().each (function (field) {
			field.setReadOnly (true); 
		});
		form.getForm().baseParams = {tomcatDomain: domainId};
        form.getForm().load({
			url: GlobalData.urlPrefix + "domain/" + domainId + "/tomcatconfig",
			method: 'GET',
            waitMsg: 'Loading...',
			success: function(formBasic, action){				
				if(action.result.data.id){
					form.down("[name=tomcatVersionCd]").hide();
					form.down("[name=separator]").hide();
					form.down("[name=tomcatVersionNm]").hide();
					form.jmxEnable = action.result.data.jmxEnable;
					console.log('jmxEnable = ' + form.jmxEnable);
				}
			},
			failure: function(){
				form.down("[name=tomcatVersionCd]").setVisible(true);
				form.down("[name=separator]").setVisible(true);
				form.down("[name=tomcatVersionNm]").setVisible(true);
			}
        });
    },

    displaySessions: function(domainId) {
         //load session list
        var sessionGrid = Ext.getCmp("domainSessionGridView");
        sessionGrid.getStore().removeAll();

		sessionGrid.getStore().getProxy().extraParams = { 'domainId' : domainId};
		//sessionGrid.getStore().getProxy().url = GlobalData.urlPrefix + "resources/sessions.json";//for demo.
		sessionGrid.getStore().getProxy().url = GlobalData.urlPrefix + "dolly/getSessionKeyList";
        sessionGrid.getStore().load({
			method: 'GET'
		});
    },

    displayApplications: function(domainId) {
        //load application list
        var appGrid = Ext.getCmp("associatedApplicationListView");
        appGrid.getStore().removeAll();
        appGrid.getStore().getProxy().url = GlobalData.urlPrefix + "domain/" + domainId+"/apps";
        appGrid.getStore().load();
    },

    displayClusteringConf: function(domainId) {
         var form  = Ext.getCmp("clusteringConfigTab").down("#clusteringConfForm");
        //load clustering conf
	   var latestVersionUrl = GlobalData.urlPrefix + "clustering/config/latest";
	   webapp.app.getController("globalController").ajaxRequest(latestVersionUrl, {"objectType": GlobalData.objTypeTomcatDomain, "objectId": GlobalData.lastSelectedMenuId}, "GET", function(json){
	       form.bindData(GlobalData.objTypeTomcatDomain, domainId, json.data);
	    }, null);

    },
	
    displayTomcatList: function(domainId){
    	var url = GlobalData.urlPrefix + "domain/tomcatlist";
    	var store = Ext.getCmp("associatedTomcatGridView").getStore();
    	store.getProxy().url = url;
    	store.getProxy().extraParams = {"domainId":domainId};
    	store.load();
    },
	
	displayDatasources: function(domainId) {
		var url = GlobalData.urlPrefix + "/domain/datasource/list";
    	var store = Ext.getCmp("domainDatasourceGrid").getStore();
    	store.getProxy().url = url;
    	store.getProxy().extraParams = {"domainId":domainId};
    	store.load();
	},
	
	showViewLogWindow: function(taskDetailId) {
	
		Ext.create("widget.logViewWindow", {'taskDetailId' : taskDetailId}).show();
	
	},
	
	loadDomainAlertSettings: function(domainId) {
		var url = GlobalData.urlPrefix + "domain/" + domainId + "/alertsettings";
		var container = Ext.getCmp("DomainContainer");
		var store = container.down("tabpanel #alertTab gridpanel").getStore();
		store.getProxy().url = url;
		store.reload();
	},
	
	displayLogs: function(domainId) {
		var url = GlobalData.urlPrefix + "task/list/domain/" +domainId;
		var container = Ext.getCmp("DomainContainer");
		var store = container.down("tabpanel #logTab gridpanel").getStore();
		store.getProxy().url = url;
		store.reload();
	}
});
