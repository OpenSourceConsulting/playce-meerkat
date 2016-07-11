/*
 * File: app/view/ConfigFileComparisonWindow.js
 */

Ext.define('webapp.view.ConfigFileComparisonWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.ConfigFileComparisonWindow',

    requires: [
        'Ext.Component'
    ],

    autoShow: true,
    height: 422,
    hidden: false,
    width: 842,
    layout: 'fit',
    title: 'Compare config file',
    defaultListenerScope: true,
	modal: true,
    items: [
        {
            xtype: 'component',
            autoEl: {
                tag: 'iframe',
                src: '#'
            },
            autoRender: true,
            autoShow: true,
            frame: true,
            id: 'compareConfigFileFrame',
            listeners: {
                activate: 'onCompareConfigFileFrameActivate'
            }
        }
    ],

    onCompareConfigFileFrameActivate: function(component, eOpts) {
        component.getForm().load({
            waitMsg: 'Loading...'
        });
    }
});