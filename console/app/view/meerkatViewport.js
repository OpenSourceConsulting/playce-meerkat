/*
 * File: app/view/meerkatViewport.js
 */

Ext.define('webapp.view.meerkatViewport', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.dollyViewport',

    requires: [
        'webapp.view.dashboardPanel',
        'webapp.view.DomainContainer',
        'webapp.view.TomcatInstanceContainer',
        'webapp.view.MonitoringMachineContainer',
        'webapp.view.MonitoringTomcatInstance',
        'webapp.view.DetailMonitoringMachineContainer',
        'webapp.view.DetailMonitoringTomcatInstance',
        'webapp.view.ServerManagementContainer',
        'webapp.view.SessionServerGroupContainer',
        'webapp.view.DatasourceManagementContainer',
        'webapp.view.UserMntContainer',
		'webapp.view.TomcatDomainContainer',
        'webapp.view.NewMenuPanel',
        'Ext.toolbar.Toolbar',
        'Ext.form.Label',
        'Ext.panel.Panel',
        'Ext.Img',
        'Ext.toolbar.Spacer',
        'Ext.toolbar.Separator',
        'Ext.button.Button',
        'Ext.layout.container.Border',
        'Ext.layout.container.Card',
        'Ext.layout.container.Accordion',
        'Ext.util.Point',
        'Ext.chart.*',
        'Ext.data.*'
    ],
    id: 'dollyViewport',
    itemId: 'dollyViewport',
    layout: 'card',

    items: [
        {
            xtype: 'container',
            height: 250,
            id: 'introContainer',
            itemId: 'introContainer',
            width: 400
        },
        {
            xtype: 'container',
            id: 'mainContainer',
            itemId: 'mainContainer',
            width: 150,
            layout: 'border',
            items: [
              {
                  xtype: 'container',
                  region: 'center',
				  minWidth: 1200,
				  manageHeight: false,
				  collapsible: false,
                  id: 'subCenterContainer',
                  layout: 'card',
                  items: [
                      {
                          xtype: 'dashboardpanel'
                      },
                      {
                          xtype: 'domaincontainer'
                      },
                      {
                          xtype: 'tomcatinstancecontainer'
                      },
                      {
                          xtype: 'monitoringmachinecontainer'
                      },
                      {
                          xtype: 'monitoringtomcatinstance'
                      },
                      {
                          xtype: 'detailmonitoringmachinecontainer'
                      },
                      {
                          xtype: 'detailmonitoringtomcatinstance'
                      },
                      {
                          xtype: 'servermanagementcontainer'
                      },
                      {
                          xtype: 'sessionservergroupcontainer'
                      },
                      {
                          xtype: 'datasourcemanagementcontainer'
                      },
                      {
                          xtype: 'usermntcontainer'
                      },
					  {
						  xtype: 'tomcatdomaincontainer'
					  }
                  ]
                },
                {
                    xtype: 'panel',
                    margin: '0 0 5 0',
                    region: 'north',
                    height: 50,
                    itemId: 'northPanel',
                    split: true,
					collapsible: false,
					splitterResize: false,
                    layout: {
                        type: 'hbox',
                        align: 'middle'
                    },
                    items: [
                        {
                            xtype: 'image',
                            margin: '3 3 3 8',
                            height: 45,
                            itemId: 'logoImg',
                            width: 120,
                            src: 'resources/images/logo/osc-logo.png',
                            title: 'Open Source Consulting Co. ltd.'
                        },
                        /*{
                            xtype: 'image',
                            margin: '3 3 3 8',
                            height: 45,
                            itemId: 'dollyImg',
                            src: 'resources/images/logo/athena_dolly.png',
                            title: 'Athena Dolly'
                        },*/
						{
							xtype: 'label',
							margin: '0 0 0 15',
							cls: 'stats-label1',
							text: 'Athena Meerkat'
						},
                        {
                            xtype: 'tbspacer',
                            flex: 2,
                            width: 500
                        },
                        {
                            xtype: 'panel',
                            flex: 1,
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    items: [
                                        {
                                            xtype: 'tbspacer',
                                            flex: 1
                                        },
                                        {
                                            xtype: 'label',
                                            id: 'topLastLogonLabel'
                                        },
                                        {
                                            xtype: 'tbseparator'
                                        },
                                        {
                                            xtype: 'button',
                                            id: 'topUsername',
											iconCls: 'fa fa-user',
											menu: [
											{
												text:'로그아웃',
												id: 'logoutBtn',
												iconCls: 'fa fa-sign-out'
											},
											{
												text:'내정보',
												handler: function(){
													Ext.create("widget.UserMyWindow").show();
												}
											}
										]
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    region: 'south',
                    split: true,
                    height: 30,
                    itemId: 'southPanel',
                    layout: {
                        type: 'vbox',
                        align: 'stretch',
                        pack: 'center'
                    },
                    items: [
                        {
                            xtype: 'label',
                            itemId: 'footerLabel1',
                            style: '{text-align: center;}',
                            text: '© 2016 , Open Source Consulting, Inc. All rights reserved.'
                        },
                        {
                            xtype: 'label',
                            flex: 1,
                            hidden: true,
                            itemId: 'footerLabel2',
                            style: '{text-align: center;}',
                            text: 'Gangnam Mirae Tower 805, Saimdang-ro 174(Seocho-dong), Seocho-gu, Seoul, Korea'
                        },
                        {
                            xtype: 'label',
                            flex: 1,
                            hidden: true,
                            itemId: 'footerLabel3',
                            style: '{text-align: center;}',
                            text: '+ 82 (2) 516-0711, sales@osci.kr'
                        }
                    ]
                },
                {
                    xtype: 'leftMenuPanel',
                    collapsible: true,
                    region: 'west',
                    split: true
                }
            ]
        }
    ]

});
