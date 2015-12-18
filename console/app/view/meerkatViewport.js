/*
 * File: app/view/meerkatViewport.js
 *
 * This file was generated by Sencha Architect version 3.0.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
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
        'webapp.view.ResourceManagementContainer',
        'webapp.view.UserMntContainer',
        'Ext.layout.container.Border',
        'Ext.layout.container.Card',
        'Ext.ux.GMapPanel',
        'Ext.util.Point',
        'Ext.chart.*',
        'Ext.data.*'
    ],

    id: 'dollyViewport',
    itemId: 'dollyViewport',
    layout: {
        type: 'card'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
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
                    layout: {
                        type: 'border'
                    },
                    items: [
                        {
                            xtype: 'container',
                            region: 'center',
                            frame: false,
                            id: 'centerContainer',
                            itemId: 'centerContainer',
                            layout: {
                                align: 'stretch',
                                type: 'vbox'
                            },
                            items: [
                                {
                                    xtype: 'toolbar',
                                    flex: 0.5,
                                    width: 150,
                                    items: [
                                        {
                                            xtype: 'label',
                                            text: 'Navigation > Sub > Sub'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'container',
                                    flex: 9,
                                    id: 'subCenterContainer',
                                    width: 150,
                                    layout: {
                                        type: 'card'
                                    },
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
                                            xtype: 'resourcemanagementcontainer'
                                        },
                                        {
                                            xtype: 'usermntcontainer'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            margins: '0 0 5 0',
                            region: 'north',
                            height: 50,
                            itemId: 'northPanel',
                            layout: {
                                align: 'middle',
                                type: 'hbox'
                            },
                            items: [
                                {
                                    xtype: 'image',
                                    margins: '3 3 3 8',
                                    height: 45,
                                    itemId: 'logoImg',
                                    margin: '',
                                    width: 120,
                                    src: 'resources/images/logo/osc-logo.png',
                                    title: 'Open Source Consulting Co. ltd.'
                                },
                                {
                                    xtype: 'image',
                                    margins: '3 3 3 8',
                                    height: 45,
                                    itemId: 'dollyImg',
                                    src: 'resources/images/logo/athena_dolly.png',
                                    title: 'Athena Dolly'
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
                                                    id: 'topUsername',
                                                    itemId: 'topUsername',
                                                    text: '홍길동'
                                                },
                                                {
                                                    xtype: 'tbseparator'
                                                },
                                                {
                                                    xtype: 'button',
                                                    id: 'logoutBtn',
                                                    itemId: 'logoutBtn',
                                                    text: '로그아웃'
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            region: 'west',
                            split: true,
                            itemId: 'menuPanel',
                            width: 250,
                            resizable: false,
                            layout: {
                                type: 'fit'
                            },
                            bodyBorder: false,
                            collapsible: true,
                            title: 'MENU',
                            titleAlign: 'left',
                            titleCollapse: false,
                            items: [
                                {
                                    xtype: 'treepanel',
                                    id: 'menuTreePanel',
                                    itemId: 'menuTreePanel',
                                    header: false,
                                    title: 'My Tree Panel',
                                    hideHeaders: true,
                                    store: 'MenuTreeStore',
                                    rootVisible: false,
                                    dockedItems: [
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top'
                                        }
                                    ],
                                    viewConfig: {

                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            margins: '5 0 0 0',
                            region: 'south',
                            height: 50,
                            hidden: true,
                            itemId: 'southPanel',
                            layout: {
                                align: 'stretch',
                                pack: 'center',
                                type: 'vbox'
                            },
                            items: [
                                {
                                    xtype: 'label',
                                    flex: 1,
                                    itemId: 'footerLabel1',
                                    style: '{text-align: center;}',
                                    text: '© 2014 , Open Source Consulting, Inc. All rights reserved.'
                                },
                                {
                                    xtype: 'label',
                                    flex: 1,
                                    itemId: 'footerLabel2',
                                    style: '{text-align: center;}',
                                    text: 'Gangnam Mirae Tower 805, Saimdang-ro 174(Seocho-dong), Seocho-gu, Seoul, Korea'
                                },
                                {
                                    xtype: 'label',
                                    flex: 1,
                                    itemId: 'footerLabel3',
                                    style: '{text-align: center;}',
                                    text: '+ 82 (2) 516-0711, sales@osci.kr'
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});