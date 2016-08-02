/*
 * File: app/view/ScouterMngWindow.js
 */

Ext.define('webapp.view.ScouterMngWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.ScouterMngWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],
    id: 'ScouterMngWindow',
	title: 'Scouter Agent 설치',
    width: 480,
	height: 450,
	minWidth: 300,
	minHeight: 300,
    layout: 'fit',
	defaultFocus: 'scouterAgentInstallPath',
    modal: true,
	closeAction: 'destroy',
	icon: 'resources/images/icons/fam/scouter.png',
	buttonAlign: 'center',
	listeners: {
		show: 'onWindowShow',
		beforeclose: 'onWindowBeforeClose'
	},
    items: [
        {
            xtype: 'form',
            bodyPadding: 10,
			layout: {
                    type: 'vbox',
                    align: 'stretch'
            },
            fieldDefaults: {
				labelAlign: 'top',
				labelWidth: 90,
				labelStyle: 'font-weight:bold'
			},
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '설치 경로',
                    name: 'scouterAgentInstallPath',
					itemId: 'scouterAgentInstallPath',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    validateBlank: true
                },
				{
                    xtype: 'textareafield',
					flex: 1,
                    fieldLabel: 'Agent 설정',
                    name: 'scouterAgentConfigs',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    validateBlank: true
                },
				{
					xtype: 'hiddenfield',
					name: 'isDefault'
				},
				{
                    xtype: 'label',
                    cls: 'osc-panel-tip',
                    text: 'obj_name 은 변경할필요가 없습니다. 자동 설정됩니다.'
                }
			]
		}
    ],
	buttons: [
        {
			itemId: 'doInstallScouterAgentBtn',
			text: '설치하기',
			handler: function(button, e) {
				button.up("window").applyAgent();
			}
		},
		{
			handler: function(button, e) {
				button.up("window").close();
			},
			text: '취소'
		}
    ],
	
	onWindowShow: function(window, eOpts) {
	
		var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
		
		if(tomcats.length == 0){
			MUtils.showWarn("설치할 tocmat instance가 없습니다.");
			window.close();
			return;
		}
	
		window.down('form').getForm().load({
			url: GlobalData.urlPrefix + "/provi/scouter/config/" + GlobalData.lastSelectedMenuId,
			method: 'GET',
            waitMsg: 'Loading...',
			success: function(formBasic, action){
				//var response = Ext.decode(action.response.responseText);
				var isDefault = formBasic.findField('isDefault').getValue();
				console.log('isDefault : ' + isDefault);
				
				if(isDefault == 'false'){
					window.down('#scouterAgentInstallPath').setReadOnly(true);
					window.down('#doInstallScouterAgentBtn').setText('적용');
					window.setTitle('Scouter Agent 설정 조회');
				}
			}
		});
	},
	
	applyAgent: function() {
		var form = this.down('form');
		var isDefault = form.getForm().findField('isDefault').getValue();
		var action = (isDefault == 'true')? '설치': '변경';// install
		
		var me = this;
		Ext.MessageBox.confirm('Confirm', '이설정으로 '+ action +'하시겠습니까?', function(btn){

            if(btn == "yes"){
                
				if (form.isValid()){
		
					var scouterAgentInstallPath = form.getForm().findField('scouterAgentInstallPath').getValue();
					var scouterAgentConfigs = form.getForm().findField('scouterAgentConfigs').getValue();
					var taskCdId = (isDefault == 'true')? 113: 115;// install
					
				
					Ext.Ajax.request({
						 url:  GlobalData.urlPrefix + "/task/create",
						 params: {'domainId': GlobalData.lastSelectedMenuId, 'taskCdId': taskCdId},
						 success: function(resp, ops) {
							var task = Ext.decode(resp.responseText).data;
								
							if(task.id > 0){
								var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
								 Ext.each(tomcats, function (tomcat) {
									Ext.Ajax.request({
										url: GlobalData.urlPrefix + "provi/scouter/install/agent/" + tomcat.id ,
										params: {'taskHistoryId': task.id, 'scouterAgentInstallPath': scouterAgentInstallPath, 'scouterAgentConfigs': scouterAgentConfigs, 'isDefault': isDefault},
										timeout: 60000,
										success: function(resp, ops) {
											//var resData = Ext.decode(resp.responseText).data;
											console.log('success install scouter agent : ' + tomcat.get('ipaddress'));
										}
									});
								 });
								 
								 MUtils.showTaskWindow(task, function(){
									me.saveScouterInstallPath(scouterAgentInstallPath);
								 });
							} else {
								MUtils.showWarn("설치할 tomcat instance 가 없습니다.");
							}
						}
					});
				}
				
            }
        });
	},
	
	onWindowBeforeClose: function(window, eOpts) {

	},
	
	saveScouterInstallPath: function(scouterAgentInstallPath){
		var me = this;
		Ext.Ajax.request({
             url: GlobalData.urlPrefix + "domain/saveScouterInstallPath",
             params: {'domainId': GlobalData.lastSelectedMenuId, 'scouterAgentInstallPath': scouterAgentInstallPath},
             success: function(resp, ops) {
                //var resData = Ext.decode(resp.responseText).data;
				console.log('saved scouterAgentInstallPath.');
				me.close();// window close.
				//after installing scouter successfully, refresh domain info
				webapp.app.getController("DomainController").loadDomainInfo(GlobalData.lastSelectedMenuId);
            }
        });
	}

});