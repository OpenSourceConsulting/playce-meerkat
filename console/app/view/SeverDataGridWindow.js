/*
 * File: app/view/SeverDataGridWindow.js
 */

Ext.define('webapp.view.SeverDataGridWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.severdatagridwindow',

    requires: [
        'Ext.form.FieldSet',
        'Ext.form.field.ComboBox',
        'Ext.button.Button'
    ],

    height: 298,
    width: 503,
    title: 'New Server Datagrid',
	modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    dockedItems: [
        {
            xtype: 'fieldset',
            flex: 1,
            dock: 'top',
            height: 485,
            layout: 'column',
            items: [
                {
                    xtype: 'textfield',
                    margin: '30 0 10 50',
                    width: 414,
                    fieldLabel: 'Name'
                },
                {
                    xtype: 'combobox',
                    margin: '0 0 10 50',
                    width: 414,
                    fieldLabel: 'Type',
                    store: 'TempoStore'
                },
                {
                    xtype: 'textfield',
                    margin: '0 0 10 50',
                    width: 414,
                    fieldLabel: 'IP Address'
                },
                {
                    xtype: 'button',
                    margin: '0 10 10 400',
                    text: 'Lookup'
                },
                {
                    xtype: 'textfield',
                    margin: '0 0 10 50',
                    width: 414,
                    fieldLabel: 'Hostname'
                },
                {
                    xtype: 'button',
                    margin: '0 0 0 150',
                    text: 'Create'
                },
                {
                    xtype: 'button',
                    margin: '0 0 0 10',
                    text: 'Canel'
                }
            ]
        }
    ]

});