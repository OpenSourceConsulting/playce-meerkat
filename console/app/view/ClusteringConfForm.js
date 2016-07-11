/*
 * File: app/view/ClusteringConfForm.js
 */

Ext.define('webapp.view.ClusteringConfForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.clusteringconfform',

    requires: [
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Paging',
        'Ext.button.Button',
        'Ext.toolbar.Separator',
        'Ext.form.field.ComboBox',
        'Ext.form.Label',
        'Ext.form.field.Hidden'
    ],
	scrollable: true,
	layout: 'fit',
    itemId: 'clusteringConfForm',
    bodyPadding: 10,
    defaultListenerScope: true,
    items: [
        {
            xtype: 'gridpanel',
            itemId: 'clusteringConfigurationGridView',
            forceFit: true,
            store: 'ClusteringConfigurationStore',
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'name',
                    text: 'Name'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'value',
                    text: 'Value'
                },
				{
					xtype: 'actioncolumn',
					sortable: true,
					dataIndex: 'id',
					menuDisabled: true,
					text: 'Actions',
					items: [
						{
							handler: function(view, rowIndex, colIndex, item, e, record, row) {
							},
							iconCls: 'icon-edit',
							tooltip: 'Edit'
						},
						{
							handler: function(view, rowIndex, colIndex, item, e, record, row) {
							},
							iconCls: 'icon-delete',
							tooltip: 'Delete'
						}
					]
				}
            ],
			
            listeners: {
                itemcontextmenu: 'onClusteringConfigurationGridViewItemContextMenu'
            },
			
            dockedItems: [
                {
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    width: 360,
                    displayInfo: true,
                    store: 'ClusteringConfigurationStore'
                }
            ]
        },
		{
            xtype: 'hiddenfield',
            anchor: '100%',
            itemId: 'objectTypeHiddenField',
            fieldLabel: 'Label'
        },
        {
            xtype: 'hiddenfield',
            anchor: '100%',
            itemId: 'objectIdHiddenField',
            fieldLabel: 'Label'
        }
    ],
	
    onClusteringConfigurationGridViewItemContextMenu: function(dataview, record, item, index, e, eOpts) {
		if (GlobalData.lastSelectedMenuId > 0) {//domain level
			return;
		}
    	var mnuContext = Ext.create("Ext.menu.Menu",{
            items: [
				{
					id: 'edit-clustering-config',
					text: 'Edit'
				},
				{
					id: 'delete-clustering-config',
					text: 'Delete'
				}
            ],
			
            listeners: {
                click: function( _menu, _item, _e, _eOpts ) {
                    switch (_item.id) {
                        case 'edit-clustering-config':
                        	var selectedRecords;
							selectedRecords = Ext.getCmp('dataGridServerGroupGrid').getSelectionModel().getSelection();
							var objectId = selectedRecords[0].get("id");
                            webapp.app.getController("ClusteringConfigurationController").showClusteringConfigurationWindow("edit", record.get("id"), objectId);
                            break;
                        case 'delete-clustering-config':
                            webapp.app.getController("ClusteringConfigurationController").deleteConfig(record.get("id"));
                            break;
                        default:
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

        mnuContext.showAt(e.getXY());
        e.stopEvent();
    },

    bindData: function(objectType, objectId, latestVersionId) {
		if(latestVersionId == null) {
			latestVersionId = 0;
		}
		var contentUrl = GlobalData.urlPrefix;
		contentUrl = contentUrl + "clustering/config/" + latestVersionId;
		if(objectType === GlobalData.objTypeTomcatDomain) {
			var grid = this.down("#clusteringConfigurationGridView");
			grid.getStore().getProxy().url = contentUrl;
			grid.getStore().getProxy().extraParams = {"objectType": objectType, "objectId": objectId};
			grid.getStore().load();
		}
		else {
			var clusteringConfCombobox = this.up("tabpanel").down("#clusteringConfVersionCombobox");
			if(clusteringConfCombobox !== null) {
				
				var clusteringConfVersionUrl =  GlobalData.urlPrefix + "clustering/config/versions";
				clusteringConfCombobox.getStore().removeAll();
				clusteringConfCombobox.getStore().getProxy().url = clusteringConfVersionUrl;
				clusteringConfCombobox.getStore().getProxy().extraParams = {"objectId":objectId};
				clusteringConfCombobox.getStore().load();					
				clusteringConfCombobox.reset();
				var grid = this.down("#clusteringConfigurationGridView");
				grid.getStore().getProxy().url = contentUrl;
				grid.getStore().getProxy().extraParams = {"objectType": objectType, "objectId": objectId};
				if(latestVersionId > 0){
					clusteringConfCombobox.setValue(latestVersionId);
				}
				else{
					grid.getStore().load();
				}
			}
			
			this.down("#objectTypeHiddenField").setValue(objectType);
			this.down("#objectIdHiddenField").setValue(objectId);
		}
    }
});