/*
 * File: app/controller/DatasourceController.js
 */

Ext.define('webapp.controller.DatasourceController', {
    extend: 'Ext.app.Controller',

    control: {
        "#btnLinkNewDatasource": {
            click: 'onShowDatasourceWindow'
        },
        "#mybutton38": {
            click: 'onMybutton38Click'
        },
        "#datasourceList": {
            select: 'onDatasourceListSelect'
        },
        "#dsConnectionTestBtn": {
            click: 'onDsConnectionTestBtnClick'
        },
        "#dsModifiedBtn": {
            click: 'onDsModifiedBtnClick'
        },
        "#formDSTestBtn": {
            click: 'onFormDSTestBtnClick'
        },
        "#formDSSubmitBtn": {
            click: 'onFormDSSubmitBtnClick'
        },
        "#dsKeywordField": {
            specialkey: 'onDsKeywordFieldSpecicalKey'
        },
		"#ResDataSourceContainer": {
			activate: 'containerActivate'
		},
		"#dbTypeCombobox": {
			change: 'onDBTypeComboboxChange'
		}
    },
	
	containerActivate: function(container, opts){
		this.initDatasourceContainerView();
	},
	
    onClickLinkNewDatasource: function(button, e, eOpts) {
        this.showLinkDatasourceWindow(GlobalData.lastSelectedMenuId, "new");
    },

    onMybutton38Click: function(button, e, eOpts) {
        var selectedDsIds = "";
        var items = Ext.getCmp("allDatasourceGrid").getStore();
        items.each(function(rec){
            if(rec.get("selected") === true){
                selectedDsIds += "#" + rec.get("id");
            }
        });

        var restartTomcat = Ext.getCmp("restartTomcatCheckbox").getValue();
        var window = Ext.getCmp("linkNewDataSourceWindow");
        var url = GlobalData.urlPrefix + "datasource/tomcat/link/save";
        Ext.Ajax.request({
            url: url,
            params: {"ids":selectedDsIds, "tomcatId" : GlobalData.lastSelectedMenuId,"isRestart":restartTomcat},
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                if(response.success === true){
                    Ext.getCmp("tomcatDatasourcesGrid").getStore().loadData(response.data);
                    window.close();
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
    },

    onDatasourceListSelect: function(rowmodel, record, index, eOpts) {
        var id = record.get("id");
        var name = record.get("name");
        var dbType = record.get("dbType");
        var jdbcUrl = record.get("jdbcUrl");
        var username = record.get("userName");
        var password = record.get("password");
        var maxConnection = record.get("maxConnection");
        var maxConnectionPool = record.get("maxConnectionPool");
        var minConnectionPool = record.get("minConnectionPool");
        var timeout = record.get("timeout");


        var nameField = Ext.getCmp("dsNameField");
        var dbTypeCombobox = Ext.getCmp("dsDBTypeCombobox");
        var jdbcUrlField = Ext.getCmp("dsJDBCUrlField");
        var usernameField = Ext.getCmp("dsUsernameField");
        var passwordField = Ext.getCmp("dsPasswordField");
        var _idField = Ext.getCmp("dsIDField");
        var maxConnectionField = Ext.getCmp("dsMaxConnectionField");
        var maxConnectionPoolField = Ext.getCmp("dsMaxConnectionPoolField");
        var minConnectionPoolField = Ext.getCmp("dsMinConnectionPoolField");
        var timeoutField = Ext.getCmp("dsTimeoutField");
		/*
        var detailTab = Ext.getCmp("dsDetailTab");
        detailTab.setVisible(true);
		detailTab.up("container").down("#messageField").setHidden(true);
		*/
		var serverDetail = Ext.getCmp('datasourceDetailPanel');
		serverDetail.layout.setActiveItem(1);
		
        _idField.setValue(id);
        nameField.setValue(name);
        dbTypeCombobox.getStore().load();
        dbTypeCombobox.setValue(dbType);
        jdbcUrlField.setValue(jdbcUrl);
        usernameField.setValue(username);
        passwordField.setValue(password);
        _idField.setValue(id);
        maxConnectionField.setValue(maxConnection);
        minConnectionPoolField.setValue(minConnectionPool);
        maxConnectionPoolField.setValue(maxConnectionPool);
        timeoutField.setValue(timeout);

        //load tomcat instances
        var url = GlobalData.urlPrefix + "res/ds/"+id+"/tomcatlist";
        var tiGrid = Ext.getCmp("dsTomcatInstanceGrid");
        tiGrid.getStore().getProxy().url = url;
        tiGrid.getStore().load();
    },

    onDsConnectionTestBtnClick: function(button, e, eOpts) {
        var dbTypeCombobox = Ext.getCmp("dsDBTypeCombobox");
        var jdbcUrlField = Ext.getCmp("dsJDBCUrlField");
        var usernameField = Ext.getCmp("dsUsernameField");
        var passwordField = Ext.getCmp("dsPasswordField");

        var params = {"jdbcUrl":jdbcUrlField.getValue(), "username":usernameField.getValue(),
                      "password":passwordField.getValue(), "dbType":dbTypeCombobox.getRawValue()};
        this.testDSConnection(params, function(data){
            Ext.Msg.show({
                title: "Message",
                msg: data.msg,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.INFO
            });
        }, function(data){
            Ext.Msg.show({
                title: "Message",
                msg: data.msg,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.WARNING
            });
        });
    },

    onDsModifiedBtnClick: function(button, e, eOpts) {
        var nameField = Ext.getCmp("dsNameField");
        var dbTypeCombobox = Ext.getCmp("dsDBTypeCombobox");
        var jdbcUrlField = Ext.getCmp("dsJDBCUrlField");
        var usernameField = Ext.getCmp("dsUsernameField");
        var passwordField = Ext.getCmp("dsPasswordField");
        var _idField = Ext.getCmp("dsIDField");
        var maxConnectionField = Ext.getCmp("dsMaxConnectionField");
        var maxConnectionPoolField = Ext.getCmp("dsMaxConnectionPoolField");
        var minConnectionPoolField = Ext.getCmp("dsMinConnectionPoolField");
        var timeoutField = Ext.getCmp("dsTimeoutField");

        var name = nameField.getValue();
        var dbType = dbTypeCombobox.getValue();
        var jdbcUrl = jdbcUrlField.getValue();
        var username = usernameField.getValue();
        var password = passwordField.getValue();
        var id = _idField.getValue();
        var maxConn = maxConnectionField.getValue();
        var maxConnPool = maxConnectionPoolField.getValue();
        var minConnPool = minConnectionPoolField.getValue();
        var timeout = timeoutField.getValue();

        if(this.validateDS(name, username, password, jdbcUrl, dbType, maxConn, maxConnPool, minConnPool, timeout)){
            var params = {"id": id, "name":name, "userName":username, "password":password, "maxConnection":maxConn,
                          "maxConnectionPool":maxConnPool, "minConnectionPool":minConnPool, "timeout":timeout,
                          "dbType":dbType, "jdbcUrl":jdbcUrl};
            this.saveDS(params, function(data){
                Ext.getCmp("datasourceList").getStore().reload();
                Ext.Msg.show({
                    title: "Message",
                    msg: data.msg,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.INFO
                });
            });
        }
    },

    onShowDatasourceWindow: function(button, e, eOpts) {
        this.showDSWindow();
    },

    onFormDSTestBtnClick: function(button, e, eOpts) {
        var dbTypeCombobox = Ext.getCmp("formDSDBTypeCombobox");
        var jdbcUrlField = Ext.getCmp("formDSJDBCUrlField");
        var usernameField = Ext.getCmp("formDSUsernameField");
        var passwordField = Ext.getCmp("formDSPasswordField");

        var dbType = dbTypeCombobox.getRawValue();
        var jdbcUrl = jdbcUrlField.getValue();
        var username = usernameField.getValue();
        var password = passwordField.getValue();

        if(dbType === "" || jdbcUrl === "" || username === "" || password === ""){
            return;
        }
        this.testDSConnection({"username":username,"password":password, "jdbcUrl":jdbcUrl, "dbType":dbType},
                              function(data){
                                  Ext.Msg.show({
                                      title: "Message",
                                      msg: data.msg,
                                      buttons: Ext.Msg.OK,
                                      icon: Ext.Msg.INFO
                                  });
                              }, function(data){
                                  Ext.Msg.show({
                                      title: "Message",
                                      msg: data.msg,
                                      buttons: Ext.Msg.OK,
                                      icon: Ext.Msg.WARNING
                                  });
                              });
    },

    onFormDSSubmitBtnClick: function(button, e, eOpts) {
        var dbTypeCombobox = Ext.getCmp("formDSDBTypeCombobox");
        var jdbcUrlField = Ext.getCmp("formDSJDBCUrlField");
        var usernameField = Ext.getCmp("formDSUsernameField");
        var passwordField = Ext.getCmp("formDSPasswordField");

        var nameField = Ext.getCmp("formDSNameField");
        var maxConnField = Ext.getCmp("formDSMaxConnField");
        var maxConnPoolField = Ext.getCmp("formDSMaxConnPoolField");
        var minConnPoolField = Ext.getCmp("formDSMinConnPoolField");
        var timeoutField = Ext.getCmp("formDSTimeoutField");

        var dbType = dbTypeCombobox.getValue();
        var jdbcUrl = jdbcUrlField.getValue();
        var username = usernameField.getValue();
        var password = passwordField.getValue();
        var name = nameField.getValue();
        var maxConn = maxConnField.getValue();
        var minConnPool = minConnPoolField.getValue();
        var maxConnPool = maxConnPoolField.getValue();
        var timeout = timeoutField.getValue();

        if(this.validateDS(name, username, password, jdbcUrl, dbType, maxConn, maxConnPool, minConnPool, timeout)){
             var params = {"id": 0, "name":name, "userName":username, "password":password, "maxConnection":maxConn,
                              "maxConnectionPool":maxConnPool, "minConnectionPool":minConnPool, "timeout":timeout,
                              "dbType":dbType, "jdbcUrl":jdbcUrl};
                this.saveDS(params, function(data){
                    Ext.getCmp("datasourceList").getStore().reload();
                    Ext.Msg.show({
                        title: "Message",
                        msg: data.msg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.INFO
                    });
                    button.up("window").close();
                });
        }
    },

    deleteDS: function(id) { 
        var url = GlobalData.urlPrefix + "res/ds/delete";
        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this datasource?', function(btn){

             if(btn == "yes"){
                 webapp.app.getController("globalController").ajaxRequest(url, {"id":id}, "POST", function(data){
                        Ext.getCmp("datasourceList").getStore().reload();
                    }, function(data){
                        Ext.Msg.show({
                            title: "Message",
                            msg: data.msg,
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.WARNING
                        });
                    });

             }
         });

    },

    onDsKeywordFieldSpecicalKey: function(field, e, eOpts) {

        if(e.getKey() === e.ENTER){
            var store = Ext.getStore("DatasourceStore");
            store.getProxy().url = GlobalData.urlPrefix + "res/ds/search?keyword=" + field.getValue();
            store.reload();
        }
    },

    showLinkDatasourceWindow: function(tomcatId, type) {
        var window = Ext.create("widget.LinkNewDataSourceWindow");
        var url = GlobalData.urlPrefix + "datasource/tomcat/link/list";
        Ext.Ajax.request({
            url: url,
            params: {"tomcatId" : GlobalData.lastSelectedMenuId},
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                if(response.success === true){
                    var gridStore = Ext.getCmp("allDatasourceGrid").getStore();
                    gridStore.loadData(response.data);
                    //load name, state of tomcat
                    Ext.getCmp("linkDatasourceTomcatNameField").setValue(Ext.getCmp("tomcatNameField").getValue());
                    Ext.getCmp("linkDatasourceTomcatStatusField").setValue(Ext.getCmp("tomcatStateField").getValue());

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
        window.show();
    },

    removeDs: function(tomcatId, dsId) {
           Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this datasource?', function(btn){

               if(btn == "yes"){
                   var url = GlobalData.urlPrefix + "datasource/tomcat/link/remove";
                   Ext.Ajax.request({
                       url: url,
                       params: {"tomcatId" : tomcatId, "dsId":dsId},
                       success: function(resp, ops) {
                           var response = Ext.decode(resp.responseText);
                           if(response.success === true){
                               Ext.getCmp("tomcatDatasourcesGrid").getStore().loadData(response.data);
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
               }});
    },

    testDSConnection: function(params, successCallback, failCallback) {
        var url = GlobalData.urlPrefix + "res/ds/testConnection";
        webapp.app.getController("globalController").ajaxRequest(url, params, "GET", function(data){
            successCallback(data);
        }, function(data){
            failCallback(data);
        });
    },

    saveDS: function(params, successCallback, failCallback) {
        var url = GlobalData.urlPrefix + "res/ds/save";
        webapp.app.getController("globalController").ajaxRequest(url, params, "POST", function(data){
            successCallback(data);
        }, function(data){
            failCallback(data);
        });
    },

    validateDS: function(name, username, password, jdbcUrl, dbType, maxConn, maxConnPool, minConnPool, timeout) {
        if(name === "" || username === "" || password === "" || jdbcUrl === "" || maxConn < 0 || maxConnPool < 0 || minConnPool < 0 || timeout < 0 || dbType < 0){
            return false;
        }
        return true;
    },

    showDSWindow: function() {
        var window = Ext.create("widget.DatasourceWindow");
        var submitButton = Ext.getCmp("dsSubmitBtn");
        window.show();
    },
	
	initDatasourceContainerView: function(){
		var container = Ext.getCmp("ResDataSourceContainer");
		var store = container.down("#datasourceList").getStore();
		store.getProxy().url = GlobalData.urlPrefix + "res/ds/list";
        store.load();
	},
	
	onDBTypeComboboxChange: function(combo, newValue, oldValue, eOpts ){
		/*
		 * insert jdbc url template.
		 */
		console.log('select ' + combo.getRawValue());
		var jdbcUrl = combo.up('datasourceform').getForm().findField('jdbcUrl');
		
		//console.log(Ext.getClassName(jdbcUrl));
		
		if (jdbcUrl && combo.getRawValue() == 'MySQL'){
			jdbcUrl.setValue('jdbc:mysql://HOST_NAME:3306/DATABASE?useUnicode=true&characterEncoding=UTF-8');
		} else {
			jdbcUrl.setValue('');
		}
	}

});
