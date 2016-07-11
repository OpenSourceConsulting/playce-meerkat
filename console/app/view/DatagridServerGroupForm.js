/*
 * File: app/view/ClusteringConfForm.js
 */

Ext.define('webapp.view.DatagridServerGroupForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.datagridservergroupform',

    requires: [
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Paging',
        'Ext.button.Button',
        'Ext.toolbar.Separator',
        'Ext.form.field.ComboBox',
        'Ext.form.Label',
        'Ext.form.field.Hidden',
		'Ext.grid.plugin.CellEditing'
    ],

    itemId: 'datagridServerGroupForm',
    bodyPadding: 10,
    defaultListenerScope: true,

    items: [
		{
			xtype: 'fieldset',
            flex: 1,
            height: 485,
            layout: 'column',
			items: [
				{
                    xtype: 'hiddenfield',
					name: 'groupIdHiddenField',
					itemId: 'groupIdHiddenField'
                },
                {
                    xtype: 'textfield',
                    margin: '30 0 10 50',
                    width: 414,
                    fieldLabel: 'Group name',
					name: 'name',
					allowBlank: false,
                    validateBlank: true
                },
				{
                    xtype: 'combobox',
                    margin: '0 0 10 50',
                    width: 414,
                    fieldLabel: 'Type',
					store: 'SessionServerGroupTypeStore',
					valueField: 'id',
					editable: false,
					autoLoadOnValue: true,
					displayField: 'codeNm',
					name: 'typeCdId',
					allowBlank: false
                },
				{
                    xtype: 'fieldset',
                    height: 330,
                    width: 517,
                    title: 'Session Servers',
					name :'servers',
                    
					layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
					
					items: [
						{
							xtype: 'gridpanel',
							flex: 4,
							forceFit: true,
							itemId: 'sessionServerGrid',
							name:'servers',
							
							columns: [
								{
									xtype: 'checkcolumn',
									align: 'center',
									dataIndex: 'selected',
									text: 'Select'
								},
								{
									xtype: 'gridcolumn',
									align: 'center',
									dataIndex: 'name',
									text: 'Name'
								},
								{
									xtype: 'gridcolumn',
									align: 'center',
									dataIndex: 'sshIPAddr',
									text: 'IP Address'
								},
								{
									xtype: 'gridcolumn',
									align: 'center',
									dataIndex: 'port',
									text: 'Port',
									emptyCellText: '11222',
									editor: {
										xtype: 'textfield',
										allowBlank: false,
										selectOnFocus: true
									}
								}
							],
							plugins: [Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 1})]
						}
					]
				}
			]
		}
	],
 
    onClusteringConfigurationGridViewItemContextMenu: function(dataview, record, item, index, e, eOpts) {
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
                	var objType = dataview.up("#clusteringConfForm").down("#objectTypeHiddenField").getValue();
                    switch (_item.id) {
                        case 'edit-clustering-config':
                        	
                            webapp.app.getController("ClusteringConfigurationController").showClusteringConfigurationWindow("edit", record.get("id"), objType, GlobalData.lastSelectedMenuId);
                            break;
                        case 'delete-clustering-config':
                            webapp.app.getController("ClusteringConfigurationController").deleteConfig(objType, record.get("id")	);
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
        var contentUrl = GlobalData.urlPrefix;
        var clusteringConfCombobox = this.down("#clusteringConfVersionCombobox");
        var clusteringConfVersionUrl =  GlobalData.urlPrefix + "clustering/config/versions"
        
		clusteringConfCombobox.getStore().getProxy().url = clusteringConfVersionUrl;
        clusteringConfCombobox.getStore().getProxy().extraParams = {"objectId":objectId, "objectType": objectType};
        clusteringConfCombobox.getStore().load();
        
		if(latestVersionId > 0){
            clusteringConfCombobox.setValue(latestVersionId);
        }
        
		this.down("#objectTypeHiddenField").setValue(objectType);
        this.down("#objectIdHiddenField").setValue(objectId);
    }
});