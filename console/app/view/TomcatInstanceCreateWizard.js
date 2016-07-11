/*
 * File: app/view/TomcatInstanceCreateWizard.js
 */

var MSG_DOMAN_AND_TOMCAT_CONFIG='도메인 및 톰캣 구성';

Ext.define('webapp.view.TomcatInstanceCreateWizard', {
    extend: 'Ext.window.Window',
    alias: 'widget.ticWizard',

    requires: [
        'webapp.view.DomainForm',
        'webapp.view.TomcatForm',
        'webapp.view.AddingDatasourceGrid',
        'webapp.view.CreateTomcatInstanceGrid',
        'Ext.form.Label',
        'Ext.form.Panel',
        'Ext.form.field.Display',
        'Ext.grid.Panel',
        'Ext.form.FieldSet',
        'Ext.form.field.File',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.toolbar.Fill'
    ],

    height: 504,
    width: 750,
    layout: 'card',
    title: MSG_DOMAN_AND_TOMCAT_CONFIG,
    modal: true,
	defaultFocus: 'domainNameTextField',
	defaults: {
		cls: 'osc-body',
		bodyCls: 'osc-body'
	},
    items: [
        {
            xtype: 'container',
            itemId: 'step1',
            scrollable: true,
            layout: {
                type: 'vbox',
                align: 'stretch',
                padding: 10
            },
            items: [
                {
                    xtype: 'label',
                    style: 'font-weight: bold',
                    text: '1/3 Step : 도메인 생성'
                },
                {
                    xtype: 'label',
                    margin: '10px 0 10px 20px',
                    cls: 'osc-panel-tip',
                    text: '도메인을 생성합니다.'
                },
                {
                    xtype: 'domainform'
                },
                {
                    xtype: 'tomcatform'
                }
            ]
        },
        {
            xtype: 'container',
            itemId: 'step2',
            scrollable: true,
            layout: {
                type: 'vbox',
                align: 'stretch',
                padding: 10
            },
            items: [
                {
                    xtype: 'label',
                    cls: 'osc-bold',
                    itemId: 'stepTitle',
                    text: '2/3 Step : 데이타소스 선택'
                },
                {
                    xtype: 'label',
                    margin: '10px 0 10px 20px',
                    cls: 'osc-panel-tip',
                    itemId: 'stepLabel1',
                    text: '데이타소스를 선택 또는 생성합니다.'
                },
                {
                    xtype: 'displayfield',
                    itemId: 'domainLabel',
                    fieldLabel: '도메인명',
                    labelAlign: 'right',
                    value: 'API Product Domain',
                    fieldCls: 'x-form-display-field osc-bold'
                },
                {
                    xtype: 'label',
                    cls: 'osc-h3',
                    text: '데이타소스 :'
                },
                {
                    xtype: 'AddingDatasourceGrid',
                    flex: 1
                }
            ]
        },
        {
            xtype: 'container',
            itemId: 'step3',
            scrollable: true,
            layout: {
                type: 'vbox',
                align: 'stretch',
                padding: 10
            },
            items: [
                {
                    xtype: 'label',
                    itemId: 'stepTitle3',
                    style: 'font-weight: bold',
                    text: '3/3 Step : 설치 머신(Server) 선택'
                },
                {
                    xtype: 'label',
                    margin: '10px 0 10px 20px',
                    cls: 'osc-panel-tip',
                    itemId: 'stepLabel3',
                    text: 'Tomcat Instance를 생성할 Server를 선택합니다.'
                },
                {
                    xtype: 'displayfield',
                    itemId: 'domainLabel',
                    fieldLabel: '도메인명',
                    labelAlign: 'right',
                    value: 'API Product Domain',
                    fieldCls: 'x-form-display-field osc-bold'
                },
                {
                    xtype: 'label',
                    cls: 'osc-h3',
                    text: 'Servers :'
                },
                {
                    xtype: 'CreateTomcatInstanceGrid'
                },
                {
                    xtype: 'fieldset',
                    itemId: 'deployFSet',
					hidden: true,
                    collapsed: true,
                    collapsible: true,
                    title: 'Deploy War',
                    items: [
                        {
                            xtype: 'form',
                            bodyPadding: 10,
                            header: false,
                            title: 'My Form',
                            items: [
                                {
                                    xtype: 'filefield',
                                    anchor: '70%',
                                    fieldLabel: 'WAR File'
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '70%',
                                    fieldLabel: 'Context Path'
                                }
                            ]
                        }
                    ]
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
                    disabled: true,
                    hidden: true,
                    id: 'prev-btn',
                    text: '&laquo; Pre'
                },
                {
                    xtype: 'tbfill'
                },
                {
                    xtype: 'button',
                    id: 'next-btn',
                    text: '생성 & 다음 &raquo;'
                }
            ]
        }
    ]
});