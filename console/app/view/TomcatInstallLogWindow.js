/*
 * File: app/view/TomcatInstallLogWindow.js
 */

Ext.define('webapp.view.TomcatInstallLogWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.proviLogWin',

    requires: [
        'Ext.form.Label',
        'Ext.form.field.TextArea',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
		'Ext.panel.Panel',
		'Ext.Component'
    ],
    height: 592,
    width: 1200,
    layout: 'anchor',
    bodyPadding: '15 0',
    title: '작업 로그창',
	
	listeners: {
		show: 'onWindowShow',
		beforeclose: 'onWindowBeforeClose'
	},

    items: [
        {
            xtype: 'label',
            cls: 'osc-h3',
            text: 'Logs: '
        },
        {
			xtype: 'panel',
            anchor: '100% 100%',
			bodyPadding: '0 0 0 10',
			header: false,
			scrollable: true,
            bodyStyle:{"background-color":"black"}
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
		var wsUrl = 'ws://'+ location.hostname +':'+ location.port + GlobalData.urlPrefix + 'provi/taillog' ;
		
		console.log('connect ' + wsUrl);
		
		window.ws = Ext.create ('Ext.ux.WebSocket', {
			url: wsUrl ,
			autoReconnect: false,
			listeners: {
				open: function (ws) {
					console.log ('The websocket is ready to use');
					
					if(Ext.isEmpty(window.msg)){
						MUtils.showWarn("message is empty.");
					} else {
						ws.send (window.action, window.msg);
						console.log('send '+ window.action +', domanId:' + window.msg.domainId);
					}
				} ,
				close: function (ws) {
					console.log ('The websocket is closed!');
					MUtils.showInfo("Loading 완료.");
				} ,
				error: function (ws, error) {
					Ext.Error.raise (error);
				} ,
				message: function (ws, message) {
					//console.log (message);
					
					if(Ext.isEmpty(window.logPanel)){
						window.logPanel = window.down('panel');
						console.log ('create logPanel.');
					}
					
					window.logPanel.add(new Ext.Component( {
						/*autoEl: {
							tag: 'div',
							html: message,
							cls: 'osc-logs'
						}*/
						cls : 'osc-logs',
						html : message
					} )); 
					
					
					window.logPanel.getScrollable().scrollTo(null, Infinity);
					
				}
			}
		});
        
    },
	onWindowBeforeClose: function(window, eOpts) {
		window.ws.close();
	}

});