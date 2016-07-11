/*
 * File: app/controller/globalController.js
 */

MSG_PROCESS_COMPLETED="처리 완료";
MSG_SUCCESSFUL='성공';

Ext.define('webapp.controller.globalController', {
    extend: 'Ext.app.Controller',

    init: function(application) {
	
		Ext.get('loading').remove();
	
        /**
         * Global Variables를 정의
         */

        var urlPrefix = '/controller/';
        //var urlPrefix='';

        // Global variables를 정의하는 구문으로 GlobalData.urlPrefix, GlobalData.serverSize 등으로 어디에서든 접근이 가능하다.
        Ext.define('GlobalData', {
            singleton: true,
            lastSelectedMenuId:-1,
            urlPrefix: urlPrefix,
            isLogined: false,
            realTimeInterval:-1,
			monServerIntervalArray:[],
			detailMonTomcatIntervalArray:[],
            confFileServerXml:"server.xml",
            confFileContextXml:"context.xml",
			objTypeServer:"server",
            objTypeTomcatDomain:"tomcat_domain",
            objTypeTomcatInst:"tomcat_inst",
            objTypeSessionServerGroup:"server_group",
			tomcatStateStop: 7,
			tomcatStateRunning: 8,
			tomcatStateStarting: 14,
			tomcatStateStopping: 15,
			viewAjaxProgress: false,
			monValueThreshold: 100,
			netMonValueThreshold: 1000,
			heapMemoryValueThreshold:50,
			activeThreadChartThreshold:50,
			JMX_TOMCAT_CPU_USAGE_CHART: "jmx_cpu",
			JMX_TOMCAT_HEAP_MEMORY_CHART: "jmx_memory",
			JMX_TOMCAT_ACTIVE_THREADS_CHART: "jmx_threads",
			JMX_TOMCAT_JDBC_CONNECTION_CHART: "jmx_jdbc",
			SERVER_CPU_CHART : "server_cpu",
			SERVER_MEMORY_CHART : "server_memory",
			SERVER_NETIN_CHART : "server_netin",
			SERVER_NETOUT_CHART : "server_netout",
			seriesColors : ['#8ca640', '#974144', '#4091ba', '#8e658e', '#3b8d8b', '#b86465', '#d2af69',	'#6e8852', '#3dcc7e', '#a6bed1', '#cbaa4b',	'#998baa'	],
			ALERT_AGENT_CD_ID : 21

        });

        this.initExtAjax();
        this.initVType();

        //store url에 GLOBAL.urlPrefix 추가
        var stores = webapp.getApplication().stores;

        for(var i = 0; i < stores.length ; i++ ) {

			//console.log('store : ' + stores[i]);
            var store = Ext.getStore(stores[i]);

            if(store.getProxy().url !== null) {
                store.getProxy().url = urlPrefix + store.getProxy().url;
            }
			//console.log('store.getProxy().url = ' + store.getProxy().url);
        }

        Ext.define('MUtils', {
            singleton: true,
            showInfo : function(message) {
                Ext.MessageBox.show({
                    title: 'Status',
                    msg: message,
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
            },
            showWarn : function(message) {
                Ext.MessageBox.show({
                    title: 'Warnning',
                    msg: message,
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            },
            confirm : function(msg, fn) {
                Ext.MessageBox.confirm('Confirm', msg, fn);
            },
			showProvisionLogWindow: function(action, params) {
				var pOpts = { 
					"action": action, 
					msg : params 
				};
				Ext.create('widget.proviLogWin', pOpts).show();
			},
			showTaskWindow: function(taskObj, callback) {
				
				if(Ext.isNumeric(taskObj)){
					pOpts = {taskHistoryId: taskObj};
				} else {
					pOpts = {task: taskObj};
				}
				
				if(typeof callback === 'function'){
					pOpts.callback = callback;
				}
				
				Ext.create('widget.TaskWorkingWindow', pOpts).show();
			}
        });
    },
	
	startsWith: function(thisString, searchString, position){
      position = position || 0;
      return thisString.substr(position, searchString.length) === searchString;
    },

    initExtAjax: function() {
        /*
         * Global Ajax Config
         */

        Ext.Ajax.timeout = 15000;// default is 15000.
        /*
		Ext.Ajax.on("beforerequest", function(conn, options, eOpts){	
			
			if(GlobalData.viewAjaxProgress) {
				Ext.MessageBox.show({
					msg: '처리중.., please wait...',
					progressText: 'Working...',
					width:300,
					wait:true,
					waitConfig: {interval:300}
				});
			}
			
		});
		*/
        Ext.Ajax.on("requestexception", function(conn, response, options, eOpts){

			if(GlobalData.viewAjaxProgress) {
				Ext.MessageBox.hide();
				GlobalData.viewAjaxProgress = !GlobalData.viewAjaxProgress;
			}
            if(response.timedout){

                Ext.Msg.show({
                    title:'Request Timeout',
                    msg: options.url +' request is timeout.',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });

            }else if(response.status == 403){

                //if(options.url.indexOf("auth/onAfterLogin") > -1){
                if (GlobalData.isLogined === false) {
					console.log('skip 403 on ajax requestexception event');
                    return;
                }

                var resJson = Ext.JSON.decode(response.responseText);

                Ext.Msg.show({
                    title:'Access Deny',
                    msg:  resJson.msg + '<br/>request url : ' + options.url ,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR,
                    fn : function(btn) {
                        if (resJson.data == "notLogin") {
                            window.location.reload();
                        }
                    }
                });

            }else{
                var message = '';

                if(response.responseText === '') {
                    message = 'Server is not running!!';
                } else if(response.status == 404){
                    message = options.url + ' not found. 404 error.';
                } else if(response.status == 500){
                    message = options.url + ' error. 500 error.';
                } else {
                    message = Ext.JSON.decode(response.responseText).msg;
                }


                Ext.Msg.show({
                    title:'Server Error',
                    //msg: 'server-side failure with status code ' + response.status,
                    msg: message,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
		
		 Ext.Ajax.on("requestcomplete", function(conn, response, options, eOpts){
		 
			if(GlobalData.viewAjaxProgress) {
				Ext.MessageBox.hide();
				GlobalData.viewAjaxProgress = !GlobalData.viewAjaxProgress;
			}
			if(options.url.indexOf('save') > 0 || options.url.indexOf('create') > 0 || options.url.indexOf('delete') > 0 || options.url.indexOf('remove') > 0) {
				Ext.toast({
					 html: MSG_PROCESS_COMPLETED,
					 title: MSG_SUCCESSFUL,
					 width: 200,
					 align: 'tr'
				 });
			 }
		 });
    },

    initVType: function() {
        /*
         * Global Validation(VTypes) Config
         */
        Ext.apply(Ext.form.field.VTypes, {
            daterange: function(val, field) {
                var date = field.parseDate(val);

                if (!date) {
                    return false;
                }
                if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
                    var start = field.up('form').down('#' + field.startDateField);
                    start.setMaxValue(date);
                    start.validate();
                    this.dateRangeMax = date;
                }
                else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
                    var end = field.up('form').down('#' + field.endDateField);
                    end.setMinValue(date);
                    end.validate();
                    this.dateRangeMin = date;
                }
                /*
                 * Always return true since we're only using this vtype to set the
                 * min/max allowed values (these are tested for after the vtype test)
                 */
                return true;
            },

            daterangeText: 'Start date must be less than end date',

            password: function(val, field) {
                //var pwd = field.up('form').down('#passwd');
                pwd = field.previousNode('textfield');
                return (val == pwd.getValue());
            },

            passwordText: 'Passwords do not match',

            numeric: function(val, field) {
                var numericRe = /(^-?\d\d*\.\d*$)|(^-?\d\d*$)|(^-?\.\d\d*$)/;
                return numericRe.test(val);
            },
            numericText : 'Not a valid numeric number. Must be numbers',
            numericMask : /[.0-9]/,

            template: function(val, field) {
                var templateRe = /^[a-zA-Z0-9_\.\-]*$/;
                return templateRe.test(val);
            },
            templateText : "영문 대소문자, 숫자, '_', '-', '.' 만 가능합니다."
        });
    },

    realtimeAjaxRequest: function(seconds, url, params, method, successCallbackFunction, intervalCallbackFunction) {
		
		GlobalData.viewAjaxProgress = false;
        var me = this;
        me.ajaxRequest(url, params, method, null, null);
        interval = setInterval(function(){
		
			GlobalData.viewAjaxProgress = false;
            me.ajaxRequest(url, params, method, function(json){
			    successCallbackFunction(json);
			}, null);
        }, seconds * 1000);
		intervalCallbackFunction(interval);
		
    },

    stopRealtimeAjaxRequest: function(timerId) {
        clearInterval(timerId);
    },

    ajaxRequest: function(url, params, method, successCallback, failCallback) {
            Ext.Ajax.request({
                url: url,
                params: params,
                method: method,
				waitMsg: 'Waiting...',
                success: function(resp, ops) {
                    var response = Ext.decode(resp.responseText);
                    if(response.success === true){
                        if(successCallback !== null){
                            successCallback(response);
                        }
                    }
                    else {
                        Ext.Msg.show({
                            title: "Message",
                            msg: response.msg,
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.WARNING
                        });
                        if(failCallback !== null){
                            failCallback(response);
                        }
                    }
                }
            });
    },
	
	getMonitoringMaxValue: function(list, fields){
		var max = -1;
		Ext.Array.each(list, function(name, index, record) {
			Ext.Array.each(fields, function(_name, _index, _record){
				if(max < record[index][fields[_index]]) {
					max = record[index][fields[_index]];
				}
			});
		});
		return max;
	}
	

});
