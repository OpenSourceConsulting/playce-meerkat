/*
 * File: app/view/TomcatForm.js
 */

var MSG_TOMCAT_VERSION='톰캣 버전';
var MSG_HTTP_PORT='HTTP 포트';
var MSG_SESSION_TIMEOUT='세션 타임아웃';
var MSG_ENCODING='인코딩';
var MSG_SERVER_PORT='서버 포트';
var MSG_AJP_PORT='AJP 포트';
var MSG_ENABLE_JMX='JMX 활성화';
var MSG_RMI_REGISTRY_PORT='RMI 레지스트리 포트';
var MSG_RMI_SERVER_PORT='RMI 서버 포트';
var MSG_ADVANCED_CONFIG='추가 구성';

Ext.define('webapp.view.TomcatForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.tomcatform',

    requires: [
        'Ext.form.field.Hidden',
        'Ext.form.FieldSet',
        'Ext.form.FieldContainer',
        'Ext.form.field.Number',
        'Ext.form.field.Checkbox',
        'Ext.form.field.TextArea'
    ],
    itemId: 'tomcatForm',
	bodyCls: 'osc-body',
    bodyPadding: 10,
    fieldDefaults: {
        labelWidth: 150,
        labelAlign: 'right'
    },

    items: [
        {
            xtype: 'hiddenfield',
            anchor: '100%',
            itemId: 'tomcatHiddenField',
            fieldLabel: 'Label',
            name: 'id'
        },
        {
            xtype: 'fieldset',
            itemId: 'tomcatConfigGroupField',
            title: 'Tomcat Config',
            items: [
				{
                    xtype: 'combobox',
                    width: 399,
                    fieldLabel: MSG_TOMCAT_VERSION,
                    editable: false,
					autoLoadOnValue: true,
					name: 'tomcatVersionCd',
                    displayField: 'codeNm',
                    store: 'TomcatVersionStore',
                    valueField: 'id',
					itemId :'tomcatVersionCombonbox'
                },
                {
                    xtype: 'textfield',
                    width: 600,
                    fieldLabel: 'JAVA_HOME',
                    name: 'javaHome',
                    allowBlank: false,
                    validateBlank: true
                },
                {
					xtype:'fieldcontainer',
					layout: {
                        type: 'hbox'
                    },
					items:[
						{
						   xtype: 'textfield',
							width: 600,
							fieldLabel: 'CATALINA_HOME',
							name: 'catalinaHome',
							allowBlank: false,
							validateBlank: true
						},
						{
							xtype: 'displayfield',
							width: 10,
							value: '&nbsp;/&nbsp;',
							name:'separator'
						},
						{
							xtype: 'displayfield',
							width: 200,
							name:'tomcatVersionNm'
						}
					]
                },
                {
                    xtype: 'textfield',
                    width: 600,
                    fieldLabel: 'CATALINA_BASE',
                    name: 'catalinaBase',
                    allowBlank: false,
                    validateBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    layout: {
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'numberfield',
                            fieldLabel: MSG_HTTP_PORT,
                            name: 'httpPort',
							value: 8080,
                            allowBlank: false,
                            allowOnlyWhitespace: false
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: MSG_SESSION_TIMEOUT,
                            name: 'sessionTimeout',
							value: 30
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'fieldset',
            itemId: 'advancedTomcatConfigGroupField',
            collapsed: true,
            collapsible: true,
            title: MSG_ADVANCED_CONFIG,
            items: [
				{
                    xtype: 'fieldcontainer',
                    layout: {
                        type: 'hbox'
                    },
                    items: [
						{
							xtype: 'textfield',
							fieldLabel: MSG_ENCODING,
							name: 'encoding',
							value:'UTF-8',
							allowBlank: false,
							validateBlank: true
						},
						{
                            xtype: 'numberfield',
                            fieldLabel: MSG_SERVER_PORT,
                            name: 'serverPort',
							value:8005,
                            allowBlank: false,
                            allowOnlyWhitespace: false
                        },
					]
				},
                {
                    xtype: 'fieldcontainer',
                    layout: {
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'numberfield',
                            fieldLabel: 'Redirect 포트',
                            name: 'redirectPort',
							value: 8443,
                            allowBlank: false,
                            allowOnlyWhitespace: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: MSG_AJP_PORT,
                            name: 'ajpPort',
							value:8009,
                            allowBlank: false,
                            allowOnlyWhitespace: false
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'checkboxfield',
                            fieldLabel: MSG_ENABLE_JMX,
							name: 'jmxEnable',
                            boxLabel: 'Enable'
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: MSG_RMI_REGISTRY_PORT,
                            labelWidth: 170,
							width: 240,
                            name: 'rmiRegistryPort',
							value: 8225,
                            allowBlank: false,
                            allowOnlyWhitespace: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: MSG_RMI_SERVER_PORT,
                            labelWidth: 116,
							width: 196,
                            name: 'rmiServerPort',
							value: 8226,	
                            allowBlank: false,
                            allowOnlyWhitespace: false
                        }
                    ]
                },
                {
                    xtype: 'textareafield',
                    anchor: '100%',
                    fieldLabel: 'CATALINA_OPTS',
                    name: 'catalinaOpts',
					value:'export CATALINA_OPTS="-server -Xms512m -Xmx512m"'
                }
            ]
        }
    ]

});