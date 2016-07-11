/*
 * File: app/view/SessionsViewWindow.js
 */

Ext.define('webapp.view.SessionsViewWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.sessionsWin',

    requires: [
        'Ext.form.Label',
        'Ext.form.field.TextArea',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
		'Ext.panel.Panel',
		'Ext.Component'
    ],
    height: 350,
    width: 500,
    layout: {
		type: 'vbox',
		align: 'stretch'
	},
    bodyPadding: 15,
    title: '세션정보 상세 조회',
	scrollable: true,
	
	listeners: {
		show: 'onWindowShow',
		beforeclose: 'onWindowBeforeClose'
	},

    items: [
        {
			xtype: 'form',
			layout: {
				type: 'vbox',
                align: 'stretch'
			},
			scrollable: true,
			url: 'dolly/getSessionData',
            items:[
				{
					xtype: 'displayfield',
					fieldLabel: 'Session Key',
					name: 'sessionKey'
				},
				{
					xtype: 'displayfield',
					fieldLabel: 'Session Data :',
					flex: 1,
					cls: 'pretty-json',
					labelAlign : 'top',
					name: 'sessionData'
				}
			]
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'bottom',
            ui: 'footer',
            layout: {
                type: 'hbox',
                pack: 'center'
            },
            items: [
                {
                    xtype: 'button',
                    handler: function(button, e) {
                        button.up("window").close();
                    },
                    text: 'Close'
                }
            ]
        }
    ],
	
	onWindowShow: function(window, eOpts) {
		var me = this;
        window.down('form').getForm().load({
			url : GlobalData.urlPrefix + 'dolly/getSessionData',
			params : {domainId: GlobalData.lastSelectedMenuId, key: me.sessionKey},
			waitMsg : 'loading...',
			success: function(form, action){
			
				console.log(window.prettyPrint(action.result.data.sessionData));
				form.findField('sessionData').setValue("<pre>" + window.prettyPrint(action.result.data.sessionData) + "</pre>");
			}
		});
    },
	onWindowBeforeClose: function(window, eOpts) {
		
	},
	
	replacer: function(match, pIndent, pKey, pVal, pEnd) {
              var key = '<span class=json-key>';
              var val = '<span class=json-value>';
              var str = '<span class=json-string>';
              var r = pIndent || '';
              if (pKey)
                 r = r + key + pKey.replace(/[": ]/g, '') + '</span>: ';
              if (pVal)
                 r = r + (pVal[0] == '"' ? str : val) + pVal + '</span>';
              return r + (pEnd || '');
   },
   prettyPrint: function(obj) {
	  var jsonLine = /^( *)("[\w]+": )?("[^"]*"|[\w.+-]*)?([,[{])?$/mg;
	  return JSON.stringify(obj, null, 5)
		 .replace(/&/g, '&amp;').replace(/\\"/g, '&quot;')
		 .replace(/</g, '&lt;').replace(/>/g, '&gt;')
		 .replace(jsonLine, this.replacer);
   }

});