/*
 * File: app/view/ConfigurationFileForm.js
 */

Ext.define('webapp.view.ConfigurationFileForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.configurationfileform',

    requires: [
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.toolbar.Separator',
        'Ext.form.field.ComboBox',
        'Ext.form.Label',
        'Ext.form.field.TextArea',
        'Ext.form.field.Hidden'
    ],
	
    itemId: 'configurationFileForm',
    layout: 'fit',
    bodyPadding: 10,
    defaultListenerScope: true,

    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'button',
                    itemId: 'editConfigFileBtn',
                    text: 'Edit',
                    listeners: {
                        click: 'onEditConfigFileBtnClick'
                    }
                },
                {
                    xtype: 'button',
                    hidden: true,
                    itemId: 'saveConfigFileBtn',
                    text: 'Save',
                    listeners: {
                        click: 'onSaveConfigFileBtnClick'
                    }
                },
                {
                    xtype: 'tbseparator'
                },
                {
                    xtype: 'combobox',
                    itemId: 'versionConfigFileCombobox',
					name:'id',
                    width: 378,
                    fieldLabel: 'Version',
                    editable: false,
                    displayField: 'versionAndTimeAndTomcat',
                    store: 'TomcatConfigFileStore',
                    valueField: 'id',
                    listeners: {
                        change: 'onVersionConfigFileComboboxChange'
                    }
                },
                {
                    xtype: 'label',
                    text: 'compare to'
                },
                {
                    xtype: 'combobox',
                    itemId: 'compareVersionConfigFileCombobox',
                    width: 282,
                    editable: false,
                    displayField: 'versionAndTimeAndTomcat',
                    store: 'TomcatConfigFileStore',
                    valueField: 'id'
                },
                {
                    xtype: 'button',
                    text: 'Diff',
                    listeners: {
                        click: 'onButtonClick'
                    }
                }
            ]
        }
    ],
	
    items: [
        {
            xtype: 'textareafield',
            itemId: 'contentConfigFileField',
			name: 'content',
            width: 1112,
            readOnly: true
        },
        {
            xtype: 'hiddenfield',
            itemId: 'objTypeHiddenField',
			name:'objType',
            fieldLabel: 'Label'
        },
        {
            xtype: 'hiddenfield',
            itemId: 'confTypeHiddenField',
			name:'confType',
            fieldLabel: 'Label'
        }
    ],

    onEditConfigFileBtnClick: function(button, e, eOpts) {
        button.setVisible(false);
        this.down("#saveConfigFileBtn").setVisible(true);
		
        //set readonly = false for content
        this.down("#contentConfigFileField").setReadOnly(false);
        this.down("#versionConfigFileCombobox").setDisabled(true);
        this.down("#compareVersionConfigFileCombobox").setDisabled(true);
    },

    onSaveConfigFileBtnClick: function(button, e, eOpts) {
        var me = this;
        var url = GlobalData.urlPrefix + "configfile/save";
		var form = button.up("form");
		var versionCombobox = this.down("#versionConfigFileCombobox");
		form.getForm().submit({
			url: url,
			params:{"objId": GlobalData.lastSelectedMenuId,"id": versionCombobox.getValue()},
			submitEmptyText: false,
            waitMsg: 'Saving Data...',
			success: function(formBasic, action){
				var response = Ext.decode(action.response.responseText);
				button.setVisible(false);		
				me.down("#editConfigFileBtn").setVisible(true);
				me.down("#contentConfigFileField").setReadOnly(true);
				me.down("#compareVersionConfigFileCombobox").setDisabled(false);
				
				versionCombobox.setDisabled(false);
				versionCombobox.getStore().load({
					callback: function(){
						 versionCombobox.setValue(response.data.configFileId);
					}
				});
				/*
				var configFile =  Ext.decode(action.response.responseText).data;
				var msg = {
					configFileId : configFile.id,
					domainId : GlobalData.lastSelectedMenuId
				};
				MUtils.showProvisionLogWindow('updateXmlFile', msg);
				*/
				
				var task = response.data.task;
				if(task.id > 0){
					me.updateXmlFile(response.data.configFileId, task.id);
					MUtils.showTaskWindow(task);
				}
			}
		});
    },
	
	updateXmlFile: function(configFileId, taskHistoryId){
		var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		 Ext.each(tomcats, function (tomcat) {
			Ext.Ajax.request({
				url: GlobalData.urlPrefix + "/provi/tomcat/updateXml/" + tomcat.id ,
				params: {'taskHistoryId': taskHistoryId, 'configFileId': configFileId},
				timeout: 60000,
				success: function(resp, ops) {
                    //var resData = Ext.decode(resp.responseText).data;
					console.log('success update xml : ' + tomcat.get('ipaddress'));
                }
            });
	     });
	},

    onVersionConfigFileComboboxChange: function(field, newValue, oldValue, eOpts) {
        this.loadContent(field.getValue());
    },

    onButtonClick: function(button, e, eOpts) {
        var firstVerId = this.down("#versionConfigFileCombobox").getValue();
        var secondVerId = this.down("#compareVersionConfigFileCombobox").getValue();
        this.showComparingWindow(firstVerId, secondVerId);
    },

    bindingData: function(versionsLoadUrl, latestVersionId, objType, confType, latestContent) {
		var me = this;
        //clear data first
        me.clearData();
        var versionStore = me.down("#versionConfigFileCombobox").getStore();
        versionStore.getProxy().url = versionsLoadUrl;
		versionStore.load({
			callback: function(){
				if(latestVersionId > 0){
					me.down("#versionConfigFileCombobox").setValue(latestVersionId);
				}
			}
		});
		me.down("#contentConfigFileField").setValue(latestContent);
        me.down("#objTypeHiddenField").setValue(objType);
        me.down("#confTypeHiddenField").setValue(confType);
    },

    loadContent: function(id) {
        var me = this;
        var contentUrl =GlobalData.urlPrefix + "configfile/"+ id;
        webapp.app.getController("globalController").ajaxRequest(contentUrl, {}, "GET", function(json){
            me.down("#contentConfigFileField").setValue(json.data);
        }, null);
    },

    showComparingWindow: function(firstVerId, secondVerId) {
        if(firstVerId === null|| secondVerId === null) {
            MUtils.showWarn("Please select the config file version");
            return;
        }
        var window = Ext.create("widget.ConfigFileComparisonWindow");
        var frame = Ext.getDom("compareConfigFileFrame");

        frame.src = GlobalData.urlPrefix + "configfile/diff/" + firstVerId + "/" + secondVerId;
        window.show();
    },

    clearData: function() {
		this.down("#versionConfigFileCombobox").getStore().removeAll();
        this.down("#compareVersionConfigFileCombobox").getStore().removeAll();
		this.down("#compareVersionConfigFileCombobox").reset();
        this.down("#contentConfigFileField").setValue('');
        this.down("#objTypeHiddenField").setValue('');
        this.down("#confTypeHiddenField").setValue('');
    }
});