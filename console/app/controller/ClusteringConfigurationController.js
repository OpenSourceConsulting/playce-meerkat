/*
 * File: app/controller/ClusteringConfigurationController.js
 */

Ext.define('webapp.controller.ClusteringConfigurationController', {
    extend: 'Ext.app.Controller',

    id: 'ClusteringConfigurationController',

    control: {
      
        "#btnClusteringConfigurationSubmit": {
            click: 'onSubmitClusteringConfigurationClick'
        },
        "#btnNewClutersingConfiguration": {
            click: 'onBtnNewClutersingConfigurationClick'
        },
        "#clusteringConfVersionCombobox": {
            change: 'onClusteringConfVersionComboboxChange'
        },
        "#cluseringConfComparisonBtn": {
            click: 'onClusteringConfComparisonBtnClick'
        },
        "#clusteringConfKeywordField": {
            specialkey: 'onClusteringConfKeywordFieldSpecialkey'
        }
    },

    onSubmitClusteringConfigurationClick: function(button, e, eOpts) {
        var form = Ext.getCmp("clusteringConfigurationForm");			// user form
		var gridUrl = GlobalData.urlPrefix;
        var versionUrl = GlobalData.urlPrefix;
		versionUrl = versionUrl + "clustering/config/versions";
        clusteringConfTab = Ext.getCmp("clusteringConfigServerGroupTab");
		if (form.getForm().isValid()){
			form.getForm().submit({
				url: GlobalData.urlPrefix + "clustering/config/save",
				method: "POST", 
				submitEmptyText: false,
				waitMsg: 'Saving Data...',
				success: function(formBasic, action){
				   var response = Ext.decode(action.response.responseText);
				   var versionCombobox = clusteringConfTab.down("#clusteringConfVersionCombobox");
				   versionCombobox.getStore().getProxy().url = versionUrl;
				   versionCombobox.getStore().load();
				   versionCombobox.setValue(response.data);
				   button.up("window").close();
				}
			});
		}
    },

    onBtnNewClutersingConfigurationClick: function(button, e, eOpts) {
    	var selectedRecords;
		selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
		var objectId = selectedRecords[0].get("id");
        this.showClusteringConfigurationWindow("add", 0, objectId);
    },

    onClusteringConfVersionComboboxChange: function(field, newValue, oldValue, eOpts) {
        var form = field.up("tabpanel").down("#clusteringConfForm");
        var objectId = form.down("#objectIdHiddenField").getValue();
		var objectType = form.down("#objectTypeHiddenField").getValue();
        var version = field.getValue();
        var grid = form.down("#clusteringConfigurationGridView");
		var contentUrl = GlobalData.urlPrefix;
		if(version === null) {
			version = 0;
		}
		contentUrl = contentUrl + "clustering/config/" + version;
		grid.getStore().getProxy().url = contentUrl;
		grid.getStore().getProxy().extraParams = {"objectType": objectType, "objectId": objectId};
		grid.getStore().load();
    },
	
    onClusteringConfComparisonBtnClick: function(button, e, eOpts) {
        var tab = button.up("tabpanel");
        var firstVersion = tab.down("#clusteringConfVersionCombobox").getValue();
        var secondVersion = tab.down("#compareClusteringConfVersionCombobox").getValue();
        var objectId = tab.down("form").down("#objectIdHiddenField").getValue();
        if(firstVersion === null || secondVersion === null){
            MUtils.showWarn("Please select the config version");
            return;
        }
        var window = Ext.create("widget.ClusteringConfigurationComparingWindow");
        var grid =Ext.getCmp("clusteringConfComparisonGrid");
        var compareUrl = GlobalData.urlPrefix;
        compareUrl = compareUrl + "clustering/config/compare/" +firstVersion +"/to/" + secondVersion;
        if(grid){
            grid.getStore().getProxy().url = compareUrl;
            grid.getStore().getProxy().extraParams = {"objectId": objectId};
            grid.getStore().load();
        }
        window.show();
    },

    onClusteringConfKeywordFieldSpecialkey: function(field, e, eOpts) {
        if(e.getKey() === e.ENTER){
            var tab = field.up("tabpanel");
            var objectId = tab.down("#objectIdHiddenField").getValue();

            var grid =tab.down("#clusteringConfigurationGridView");
            var version = tab.down("#clusteringConfVersionCombobox").getValue();
            if(version === null){
                MUtils.showWarn("Please select version before searching.");
                return;
            }
            var url = GlobalData.urlPrefix;
            if(field.getValue().trim() === ""){
            	url = url + "clustering/config/"+version;
            }
            else{
            	url = url + "clustering/config/"+version+"/search/"+field.getValue();
            }
            grid.getStore().getProxy().url = url;
            grid.getStore.extraParams = {"objectId": objectId};
            grid.getStore().load();
        }
    },

    showClusteringConfigurationWindow: function(type, id, objectId) {
        var window = Ext.create("widget.ClusteringConfigurationWindow");
        var submitButton = Ext.getCmp("btnClusteringConfigurationSubmit");
        var form = Ext.getCmp("clusteringConfigurationForm");			// clustering configuration form.
        var editUrl = GlobalData.urlPrefix;
        editUrl = editUrl + "clustering/config/edit";

        if (type === "edit"){
            window.setTitle("Edit Clustering Configuration");
            submitButton.setText("Save");
			
			form.load({
				url:editUrl,
				params: {"id": id},
				waitMsg: 'Saving Data...'
			});
        }
		var _objectIdField = form.getForm().findField("objIdClusteringWindowHiddenField");
		_objectIdField.setValue(objectId);
        window.show();
    },

    deleteConfig: function(id) {
          Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this configuration?', function(btn){
              if(btn == "yes"){
            	var versionUrl = GlobalData.urlPrefix;
                var url = GlobalData.urlPrefix + "clustering/config/delete";
                versionUrl = versionUrl + "clustering/config/versions";
                var clusteringConfTab = Ext.getCmp("clusteringConfigServerGroupTab");

                webapp.app.getController("globalController").ajaxRequest(url,{"id":id}, "POST", function(json){
                	var versionCombobox = clusteringConfTab.down("#clusteringConfVersionCombobox");
                    versionCombobox.getStore().getProxy().url = versionUrl;
                    versionCombobox.getStore().load();
                    versionCombobox.setValue(json.data);
                     window.close();
                }, null);
            }
          });
    }

});
