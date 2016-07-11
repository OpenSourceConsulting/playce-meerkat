/*
 * File: app/view/TaskWorkingWindow.js
 */

Ext.define('webapp.view.TaskWorkingWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.TaskWorkingWindow',

    requires: [
        'Ext.form.Label',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
		'Ext.tree.Panel',
		'Ext.form.Panel',
		'Ext.tree.Column',
		'Ext.Component'
    ],
    height: 400,
    width: 1000,
	layout: {
		type: 'vbox',
		align: 'stretch'
	},
    title: '작업 상태창',
	closeAction: 'destroy',
	listeners: {
		show: 'onWindowShow',
		beforeclose: 'onWindowBeforeClose'
	},

    items: [
        {
            xtype: 'form',
            bodyPadding: 10,
			//bodyCls: ['osc-vform'],
			height: 40,
            items: [
                {
					xtype: 'displayfield',
					name:'taskName',
					fieldLabel: '작업명',
					fieldStyle: 'font-size: 14px;font-weight: bold;color: blue;'
				}
			]
		},
		{
            xtype: 'treepanel',
			flex: 1,
			emptyText: 'Loading...',
            useArrows: true,
			rootVisible: false,
            store: 'TaskStatusTreeStore',
			columns: [{
				xtype: 'treecolumn', //this is so we know which column will show the tree
				text: 'Tomcat Instance 명',
				flex: 2,
				dataIndex: 'name'
			},
			{
				text: 'Host Name',
				flex: 1,
				dataIndex: 'hostName'
			},
			{
				text: 'IP주소',
				flex: 1,
				dataIndex: 'ipaddress'
			},
			{
				text: '작업상태',
				flex: 1,
				dataIndex: 'status',
				renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
					
					if (Ext.isEmpty(record.get('ipaddress') )) {
						return '';
					}else if (value == 0) {
						return '작업 대기중';
					} else if (value == 1) {
						metaData.tdCls = 'img-loading';
						return '<span style="color: blue;">작업중...</span>';
					} else if (value == 2) {
						metaData.tdCls = 'img-success';
						return '<span style="color: green;">작업 완료</span>';
					} else if (value == 3) {
						metaData.tdCls = 'img-fail';
						return '<span style="color: red;">작업 실패</span>';
					}
					return value;
				}
			},
			{
				xtype: 'actioncolumn',
				text: 'Actions',
				items: [
					{
						getClass: function(v, meta, rec) {
							if (rec.get('status') > 0) {
								return 'icon-search';
							} else {
								return '';
							}
						},
						getTip: function(v, meta, rec) {
							if (rec.get('status') > 0) {
								return 'View logs';
							} else {
								return '';
							}
						},
						handler: function(grid, rowIndex, colIndex) {
							var rec = grid.getStore().getAt(rowIndex);
							if (rec.get('status') > 0) {
								/*
								var msg = {domainId : 0, taskDetailId: rec.get('taskDetailId')};
								MUtils.showProvisionLogWindow('viewLog', msg);
								*/
								Ext.create("widget.logViewWindow", {'taskDetailId' : rec.get('taskDetailId')}).show();
							}
						}
					},
					{
						getClass: function(v, meta, rec) {
							if (rec.get('status') == 3) {
								return 'icon-refresh';
							} else {
								return '';
							}
						},
						getTip: function(v, meta, rec) {
							if (rec.get('status')  == 3) {
								return '재실행';
							} else {
								return '';
							}
						},
						handler: function(grid, rowIndex, colIndex) {
							var rec = grid.getStore().getAt(rowIndex);
							if (rec.get('status')  == 3) {
								grid.up('window').rework(rec.get('taskDetailId'));
							}
						}
					}
				]
			}],
			listeners: {
				load: 'onTaskTreeload'
			},
			onTaskTreeload: function( treeStore, records, successful, operation, node, eOpts ) {
			
				console.log('node.childNodes.length = ' + node.childNodes.length);
				
				var domains = node.childNodes;
				
				var allSuccess = true;
				var allTaskStop = true;
				var hasTomcat = false;
				Ext.each(domains, function (domain) {
					Ext.each(domain.childNodes, function (tomcat) {
					
						hasTomcat = true;
						var status = tomcat.get('status');
						console.log(tomcat.get('name') + ' status is ' + status);
						
						if(status < 2){
							allTaskStop = false;
							console.log('allTaskStop = false');
							return false;
						}
						if(status != 2){
							allSuccess = false;
						}
					});
				});
				
				if(hasTomcat && allTaskStop) {
					this.up('window').clearInterval();
					
					if(allSuccess && this.up('window').callback){
						this.up('window').callback();
					}
				}
			},
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top'
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
		if(Ext.isEmpty(this.task) && Ext.isEmpty(this.taskHistoryId) || this.taskHistoryId == 0){
			MUtils.showWarn("작업정보가 없어 조회 할수 없습니다.");
			window.close();
			return;
		}
		
		if(Ext.isEmpty(this.task) == false){
			this.taskHistoryId = this.task.id;
			window.down('form').getForm().findField('taskName').setValue(this.task.taskName);
		} else {
			window.down('form').getForm().load({
				url: GlobalData.urlPrefix + "task/get",
				params: {"taskId": this.taskHistoryId},
				waitMsg: 'Loading...',
				method: "GET"
			});
		}

		var me = this;
		
		setTimeout(function() {
			me.loadTask(window.down('treepanel'));
		}, 600);
		
		this.intervalId = setInterval(function(){
			me.loadTask(window.down('treepanel'));
		}, 6000);
    },
	
	onWindowBeforeClose: function(window, eOpts) {
	
		window.down('treepanel').getRootNode().removeAll();
		if(this.intervalId){
			clearInterval(this.intervalId);
			console.log('clear interval.');
		}
	},
	
	loadTask: function(tree) {
	
		tree.view.loadMask.maskOnDisable = false;
		var emptyLoadMask = tree.view.loadMask.disable();
		
		tree.getStore().getProxy().url = GlobalData.urlPrefix + "task/list/" + this.taskHistoryId;
		//tree.getStore().getProxy().url = "resources/task-list.json";
		//tree.getStore().getProxy().url = 'resources/treegrid.json';
        tree.getStore().load({
			callback: function(){
				tree.expandAll();
				emptyLoadMask.enable();
			}
		});
		
	},
	
	clearInterval: function(){
		var me = this;
		
		setTimeout(function(){ 
			clearInterval(me.intervalId);
			console.log('clear interval.');
		}, 2000);// execute after grid reloaded.
	},
	
	rework: function(taskDetailId) {
	
		var me = this;
		Ext.MessageBox.confirm('Confirm', '이 작업을 재실행합니다. 하시겠습니까?', function(btn){
            if(btn == "yes"){
                Ext.Ajax.request({
					 url: GlobalData.urlPrefix + "provi/tomcat/rework/" + taskDetailId,
					 method: 'POST',
					 timeout: 60000,
					 success: function(resp, opts) {
						//var res = Ext.decode(resp.responseText);
						console.log('success : provi/tomcat/rework/' + taskDetailId);
						
						me.clearInterval();
					}
				});
				console.log("request provi/tomcat/rework/" + taskDetailId);
				
				setTimeout(function() {
					console.log("setTimeout me.loadTask()");
					me.loadTask(me.down('treepanel'));
				}, 1000);// 짧을수록 아래 로직의 수행안될수 있음.
				
				me.intervalId = setInterval(function(){
					console.log("setInterval me.loadTask()");
					me.loadTask(me.down('treepanel'));
				}, 6000);
            }
        });
	}
});