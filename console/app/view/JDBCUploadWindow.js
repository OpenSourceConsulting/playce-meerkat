/*
 * File: app/view/JDBCUploadWindow.js
 */

Ext.define('webapp.view.JDBCUploadWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.JDBCUploadWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.File',
        'Ext.button.Button',
        'Ext.form.field.Hidden'
    ],
    height: 170,
    id: 'JDBCUploadWindow',
    width: 600,
    layout: 'fit',
    title: 'JDBC 파일 Upload 설치',
	closeAction: 'destroy',
	modal: true,
    items: [
        {
            xtype: 'form',
            id: 'applicationDeployForm',
            bodyPadding: 15,
            method: 'POST',
			buttonAlign: 'center',
			layout: {
				type: 'vbox',
				align: 'stretch'
			},
			fieldDefaults: {
				//labelWidth: 170,
				labelStyle: 'font-weight:bold'
				//labelAlign: 'right'
			},
            items: [
                {
                    xtype: 'filefield',
                    fieldLabel: 'JDBC 파일 선택',
                    name: 'file',
                    allowBlank: false,
                    allowOnlyWhitespace: false,
                    emptyText: 'Select JDBC file',
					listeners: {
                        change: function(fld, value) {
                            var newValue = value.replace(/^.*(\\|\/|\:)/, '');
                            fld.setRawValue(newValue);
                        }
                    }
                },
				{
                    xtype: 'label',
                    cls: 'osc-panel-tip',
                    text: '설치위치 : $CATALINA_BASE/shared/lib'
                }
            ],
			buttons: [
				{
                    itemId: 'jdbcUploadBtn',
                    text: '설치',
					handler: function(button, e) {
						Ext.MessageBox.confirm('Confirm', '모든 인스턴스에 설치됩니다. 설치하시겠습니까?', function(btn){

							if(btn == "yes"){
								button.up("window").uploadInstall();
							}
						});
					}
                },
                {
                    itemId: 'jdbcUploadCancelBtn',
                    text: 'Cancel',
					handler: function(button, e) {
						button.up("window").close();
					}
                }
			]
        }
    ],
	
	uploadInstall: function(){
	
		var domainId = Ext.getCmp('domainViewForm').getForm().findField('id').getValue();
		var form = this.down('form');
		
		form.submit({
			url: GlobalData.urlPrefix + "domain/upload/jdbc",
			params: {'domainId': domainId},
			waitMsg: "Uploading JDBC file....",
			success: function(formBasic, action){
				var resData = Ext.JSON.decode(action.response.responseText).data;
				var task = resData.task;
				
				if(task.id > 0){
					var tomcats = Ext.getCmp("associatedTomcatGridView").getStore().getRange();
					 Ext.each(tomcats, function (tomcat) {
						Ext.Ajax.request({
							url: GlobalData.urlPrefix + "/provi/tomcat/installJDBCDriver/" + tomcat.id ,
							params: {'taskHistoryId': task.id, 'fileName': resData.fileName, 'isUploaded': true},
							timeout: 60000,
							success: function(resp, ops) {
								//var resData = Ext.decode(resp.responseText).data;
								console.log('success install jdbc driver : ' + tomcat.get('ipaddress'));
							}
						});
					 });
					 
					 MUtils.showTaskWindow(task);
				}
				
				form.up("window").close();
			},
			failure: function(formBasic, action){
				var json = Ext.JSON.decode(action.response.responseText);
				MUtils.showWarn(json.msg==""? "Deploy failed": json.msg);
			}
		});
	}

});