/*
 * File: app/view/LogViewWindow.js
 */

Ext.define('webapp.view.LogViewWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.logViewWindow',

    requires: [
        'Ext.form.Label',
        'Ext.form.field.TextArea',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
		'Ext.panel.Panel',
		'Ext.Component'
    ],
    height: 600,
    width: 1200,
    layout: 'anchor',
    bodyPadding: '15 0',
    title: 'Log viewer',
	scrollable: false,
	
	listeners: {
		show: 'onWindowShow'
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
			padding: '0 0 0 10',
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
						if(button.up("window").closeCallback && button.up("window").closeCallback !== undefined) {
							button.up("window").closeCallback();
						}
                        button.up("window").close();
                    },
                    text: 'Close'
                }
            ]
        }
    ],
	
	onWindowShow: function(window, eOpts) {
		var emptyEl = window.down('panel').body.insertHtml("beforeEnd", "<div></div>", true);
		var me = this;
		emptyEl.prev().setHeight(0);
		this.loadingStop = false;
		setTimeout(function() {
			me.loadLog();
		}, 600);
		
		this.intervalId = setInterval(function(){
			me.loadLog();
		}, 3000);
	},
	
	loadLog: function(){
		var me = this;
		var panel = this.down('panel');
		var reqUrl = GlobalData.urlPrefix + 'task/getLogs/' + this.taskDetailId;
		if(this.loadEl){
			this.loadEl.destroy();
		}
		Ext.Ajax.request({
			 url: reqUrl,
			 success: function(resp, ops) {
				var logs = Ext.decode(resp.responseText);
				Ext.each(logs, function (line) {
				
					if(line == 'end'){
						clearInterval(me.intervalId);
						console.log('clear interval.');
						me.loadingStop = true;
						return false;
					}
					panel.body.insertHtml("beforeEnd", "<div class='osc-logs'>" + me.highlightLog(line) + "</div>");
				});
				
				if(me.loadingStop){
					Ext.toast({
						 html: '로딩 완료',
						 title: 'Logs',
						 width: 200,
						 align: 'tr'
				    });
				} else {
					me.loadEl = panel.body.insertHtml("beforeEnd", "<div id class='img-loading'><span style='color: yellow; padding-left: 15px;'>Loading...</span></div>", true);
				}
				panel.getScrollable().scrollTo(null, Infinity);
			}
		});
	},
	
	errLogs : ["(BUILD FAILED)", "(\\[sshexec\\] Error)", "(#.+Exception)", "(\\[sshexec\\] Caught exception)"],
	
	highlightLog: function(line){
		var highlighted = line;
		Ext.each(this.errLogs, function (err) {
			var regExp = new RegExp(err);
			highlighted = highlighted.replace(regExp, '<span style="color: red;">$1</span>');
	    });
		
		highlighted = highlighted.replace(/(\[echo\]|BUILD SUCCESSFUL)/g,'<span style="color: yellow;">$1</span>');
		
		return highlighted;
	}
});