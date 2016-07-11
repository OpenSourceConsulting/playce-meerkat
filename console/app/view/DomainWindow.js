/*
 * File: app/view/DomainWindow.js
 */

Ext.define('webapp.view.DomainWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.DomainWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],
    id: 'domainWindow',
    width: 480,
    layout: 'fit',
    title: 'New Domain',
    modal: true,
	defaultFocus: 'domainNameTextField',
    items: [
        {
            xtype: 'domainform'
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
                    id: 'btnSubmitNewDomain',
                    itemId: 'btnSubmitNewDomain',
                    text: 'Create'
                },
                {
                    xtype: 'button',
                    handler: function(button, e) {
                        button.up("window").close();
                    },
                    text: 'Cancel'
                }
            ]
        }
    ]

});