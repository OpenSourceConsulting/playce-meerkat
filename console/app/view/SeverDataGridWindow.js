/*
 * File: app/view/SeverDataGridWindow.js
 *
 * This file was generated by Sencha Architect version 3.1.0.
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

Ext.define('webapp.view.SeverDataGridWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Ext.form.FieldSet',
        'Ext.form.field.ComboBox',
        'Ext.button.Button'
    ],

    height: 298,
    width: 503,
    title: 'New Server Datagrid',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'fieldset',
                    flex: 1,
                    dock: 'top',
                    height: 485,
                    layout: 'column',
                    title: '',
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
                            fieldLabel: 'Type'
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

        me.callParent(arguments);
    }

});