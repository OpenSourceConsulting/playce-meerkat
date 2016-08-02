/*
 * File: app/controller/MenuController.js
 */

var MSG_CREATE_TOMCAT_INSTANCE='톰캣 인스턴스 추가';
var MSG_START_TOMCAT='톰캣 시작';
var MSG_STOP_TOMCAT='톰캣 중지';
var MSG_MODIFY_TOMCAT='톰캣 수정';
var MSG_DELETE_TOMCAT='톰캣 삭제';
var MSG_CREATE_DOMAIN='도메인 생성';
var MSG_MODIFY_DOMAIN='도메인 수정';
var MSG_DELETE_DOMAIN='도메인 삭제';
var MSG_EXPAND='확장';
var MSG_REFRESH='새로 고침';

Ext.define('webapp.controller.MenuController', {
    extend: 'Ext.app.Controller',
	
    control: {
        "leftMenuPanel treepanel": {
            itemclick: 'onTreepanelItemClick',
			beforeload: 'onBeforeLoad',
			itemcontextmenu: 'onTreepanelItemContextMenu'
        },
		 "header": {
            click: 'onMenuPanelHeaderClick'
        }
    },

	onTreepanelItemClick: function(dataview, record, item, index, e, eOpts) {
        var menuId = record.get("id");
        var menuText = record.get("text");
        var is_leaf = record.get("leaf");

        if(menuId !== undefined){
            /*if (!is_leaf){
                this.loadChildMenus(menuId, dataview.up('treepanel'));
            }*/

            if (menuId != 'create-wizard') {
                this.showMenu(menuId, menuText);
            } else {
                Ext.create('widget.ticWizard').show();
            }
        }

    },

    onTreepanelItemContextMenu: function(dataview, record, item, index, e, eOpts) {
		var me = this;
        var menuId = record.get("id");
        var mnuContext = null;

        //Tomcat management level
        if (menuId==="tomcatMng"){
            mnuContext = Ext.create("Ext.menu.Menu",{

            items: [
				{
					id: 'new-domain',
					text: MSG_CREATE_DOMAIN
				},
				{
					id: 'refresh',
					text: MSG_REFRESH
				}
            ],
            listeners: {

                click: function( _menu, _item, _e, _eOpts ) {
                   switch (_item.id) {
                        case 'create-wizard':
                            Ext.create('widget.ticWizard').show();
                            break;
                        case 'new-domain':
                            webapp.app.getController("DomainController").showDomainWindow("new", 0);
                            break;
                        case 'refresh':
							var node = dataview.getStore().getNodeById(menuId);
							node.expand();
                            me.loadDomainMenu();
                            break;
                        default:
                            me.loadDomainMenu();
                            break;
                   }
                },
                hide:function(menu){
                    menu.destroy();
                }
            },
            defaults: {
               clickHideDelay: 1
            }
        });
        } else if (menuId.indexOf("tomcatMng_domain_") >= 0 && menuId.indexOf("_tomcat_") < 0) { //domain level
            mnuContext =  Ext.create("Ext.menu.Menu",{

				items: [
					{
						id: 'new-tomcat',
						text: MSG_CREATE_TOMCAT_INSTANCE
					},
					{
						id: 'edit-domain',
						text: MSG_MODIFY_DOMAIN
					},
					{
						id: 'delete-domain',
						text: MSG_DELETE_DOMAIN
					},
					{
						id:'refresh',
						text: MSG_REFRESH
					}
				],
				listeners: {
				   click: function( _menu, _item, _e, _eOpts ) {
					   var domainId = menuId.substr(menuId.indexOf("_domain_") + "_domain_".length);
					   var domainName = record.get("text");
					   switch (_item.id) {
							case 'new-tomcat':
								//init domain model
								GlobalData.lastSelectedMenuId = domainId;
								//var domainModel = {"id": domainId};
								//webapp.app.getController("TomcatInstWizardController").setDomainModel(domainModel);
								if(me.checkAddableInstance(domainId)){
									webapp.app.getController("DomainController").showTomcatInstanceWizardWindow('step3');
								} else {
									MUtils.showInfo('Tomcat 인스턴스 설정 정보가 필요합니다.<br/>Tomcat 인스턴스 설정 정보 입력후 다시 시도하세요. ');
								}
								break;
							case 'edit-domain':
								webapp.app.getController("DomainController").showDomainWindow("edit", domainId);
								break;
							case 'delete-domain':
								webapp.app.getController("DomainController").deleteDomain(domainId);
								break;
							case 'refresh':
								var node = dataview.getStore().getNodeById(menuId);
								node.expand();
								me.loadTomcatMenu(domainId);
								break;
						}
					},
					hide:function(menu){
						menu.destroy();
					}
			   },
			   defaults: {
				 clickHideDelay: 1
			   }
			});
        }
        else if (menuId.indexOf("tomcatMng_domain_") >= 0 && menuId.indexOf("_tomcat_") >= 0) { //tomcat level
			var tomcatId =  menuId.substr(menuId.indexOf("_tomcat_") + "_tomcat_".length);
			var domainId = menuId.substring(menuId.indexOf("_domain_") + "_domain_".length, menuId.indexOf("_tomcat_"));
            mnuContext =  Ext.create("Ext.menu.Menu",{
                items: [
					{
						id: 'start-tomcat',
						text: MSG_START_TOMCAT,
						disabled:(record.get("state") !== 8 && record.get("state") !== 23)?false:true
					},
					{
						id: 'stop-tomcat',
						text: MSG_STOP_TOMCAT,
						disabled:(record.get("state") !== 7 && record.get("state") !== 22)?false:true

					},
					{
						id: 'edit-tomcat',
						text: MSG_MODIFY_TOMCAT
					},
					{
						id: 'delete-tomcat',
						text: MSG_DELETE_TOMCAT,
						disabled:record.get("state") === 8? true: false
					}
                ],
                listeners: {
                    click: function( _menu, _item, _e, _eOpts ) {
						if(typeof _item === 'undefined'){
							return;
						}
                        switch (_item.id) {
                            case 'start-tomcat':
                                webapp.app.getController("TomcatController").changeState(tomcatId, 1);
                                break;
                            case 'stop-tomcat':
                                webapp.app.getController("TomcatController").changeState(tomcatId, 2);
                                break;
                            case 'edit-tomcat':
                                webapp.app.getController("TomcatController").editTomcat(tomcatId);
                                break;
                            case 'delete-tomcat':
								webapp.app.getController("TomcatController").uninstallTomcat(tomcatId, domainId);
                                break;
                        }
                    },
                    hide:function(menu){
                        menu.destroy();
                    }
           },
           defaults: {
             clickHideDelay: 1
           }
        });
        }

        if (mnuContext !== null){
            mnuContext.showAt(e.getXY());

        }
        e.stopEvent();

    },

	onMenuPanelHeaderClick: function(header, e, eOpts) {
		var panelId = header.up('panel').getId();
		//alert(panelId);
		
		if(panelId === 'dashMenuPanel'){
			Ext.getCmp("subCenterContainer").layout.setActiveItem(0);
		}
	},
	
    showMenu: function(menuId, menuText) {
        var activeItem = -1;
        var objectId = -1;
		var reactive = false;
        if(menuId === "dashboard"){
            activeItem = 0;
			reactive = true;
        }else if (menuId.indexOf("tomcatMng_domain_") >=0 && menuId.indexOf("_tomcat_") < 0) {
            objectId = menuId.substr(menuId.indexOf("tomcatMng_domain_")+ "tomcatMng_domain_".length );
            activeItem = 1;
			if(objectId !== GlobalData.lastSelectedMenuId) {
				reactive = true;
			}
        }else if (menuId.indexOf("tomcatMng_domain_") >=0 && menuId.indexOf("_tomcat_") >= 0) {
            objectId = menuId.substr(menuId.indexOf("_tomcat_") + "_tomcat_".length);
			if(objectId !== GlobalData.lastSelectedMenuId) {
				reactive = true;
			}
            activeItem = 2;
        }else if (menuId === "mon_servers" && menuId.indexOf("_server_") < 0) {
            activeItem = 3;
        }else if (menuId.indexOf("mon_tomcats") >= 0 && menuId.indexOf("_tomcat_") < 0) {
            activeItem = 4;
			return; // for demo
        }else if (menuId.indexOf("mon_servers") >= 0 && menuId.indexOf("_server_") >= 0) {
			objectId = menuId.substr(menuId.indexOf("_server_") + "_server_".length);
			if(objectId !== GlobalData.lastSelectedMenuId) {
				reactive = true;
			}
            activeItem = 5;
        }else if (menuId.indexOf("mon_tomcats") >= 0 && menuId.indexOf("_tomcat_") >= 0) {
            objectId = menuId.substr(menuId.indexOf("_tomcat_")+"_tomcat_".length);
			if(objectId !== GlobalData.lastSelectedMenuId) {
				reactive = true;
			}
            activeItem = 6;
        }else if (menuId === "resm_servers") {
            activeItem = 7;
        }else if (menuId === "resm_sessions") {
            activeItem = 8;
        }else if (menuId === "resm_ds") {
            activeItem = 9;
		}else if (menuId === "usermnt"){
            activeItem = 10;
            is_child = false;
		
        }else if (menuId ==="logmnt"){
            alert("Under construction.\n Reused from other project");
            is_child = false;
        } else if (menuId === "tomcatMng") {
			activeItem = 11; //TomcatDomainContainer
		}
		
		GlobalData.lastSelectedMenuId = objectId;
		if(activeItem > -1){
            var centerContainer = Ext.getCmp("subCenterContainer");
			centerContainer.items.get(activeItem).fireEvent("deactivate"); //reset view. 
			centerContainer.layout.setActiveItem(activeItem);
			if(reactive){
				centerContainer.items.get(activeItem).fireEvent("activate", centerContainer.items.get(activeItem) ); 
			}
        }

    },
	
	onBeforeLoad: function( store, operation, eOpts ){
		store.getProxy().url = GlobalData.urlPrefix + "menu/getNodes";
	},
	
	loadDomainMenu: function(){
		var tree = Ext.getCmp('menuTreePanel');
		this.reloadChildMenu(tree, 'tomcatMng');
		tree = Ext.getCmp('monitoringTreePanel');
		this.reloadChildMenu(tree, 'mon_tomcats');
	},
	
	loadTomcatMenu: function(domainId) {
		var tree = Ext.getCmp('menuTreePanel');
		this.reloadChildMenu(tree, 'tomcatMng_domain_' + domainId);
	},
	
	reloadChildMenu: function(treePanel, parentId) {
		var store = treePanel.getStore();
		var node = store.getNodeById(parentId);
		store.load({node: node});
	},
	
	checkAddableInstance: function(domainId) {
	
		var addable = false;
		console.log('pre addable is ' + addable);
		Ext.Ajax.request({
             url: GlobalData.urlPrefix + "domain/get",
			 method: 'GET',
             params: {id: domainId},
			 async: false,
             success: function(resp, ops) {
                    var domain = Ext.decode(resp.responseText).data;
                    
					addable = Ext.isEmpty(domain.domainTomcatConfig) == false;
			}
		});
		
		console.log('return addable is ' + addable);
		
		return addable;
	},
	
	selectNode: function(nodeId){
		var tree = Ext.getCmp('menuTreePanel');
		var node = tree.getStore().getNodeById(nodeId);
		node.expand();
		webapp.app.getController("MenuController").reloadChildMenu(tree, nodeId);
		tree.getSelectionModel().select(node);
	}
});
