/*
 * File: app/view/DomainForm.js
 */

var MSG_DOMAIN_NAME='도메인명';
var MSG_SESSION_SERVER_GROUP='세션 서버 그룹';

Ext.define('webapp.view.DomainForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.domainform',

    requires: [
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden'
    ],

    itemId: 'domainForm',
	bodyCls: 'osc-body',
    bodyPadding: 10,
    frameHeader: false,
    header: false,
    manageHeight: false,
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 140,
        labelAlign: 'right'
    },

    items: [
        {
            xtype: 'textfield',
            itemId: 'domainNameTextField',
            width: 400,
            fieldLabel: MSG_DOMAIN_NAME,
            name: 'name',
            allowBlank: false,
            validateBlank: true
        },
        {
            xtype: 'container',
			layout: 'hbox',
            items:[
				 {
					xtype: 'combobox',
					itemId: 'dataGridServerGroupComboBoxField',
					width: 400,
					fieldLabel: MSG_SESSION_SERVER_GROUP,
					name: 'serverGroup',
					editable: false,
					autoLoadOnValue: true,
					displayField: 'name',
					store: 'DatagridServerGroupStore',
					valueField: 'id'
				},
				{
					xtype:'button',
					iconCls: 'icon-delete',
					cls:'custom-button', 
					itemId: 'removeServerGroupBtn',
					margin: '0 0 0 5',
					handler: function(button, e) {
						var combobox = button.up("container").down("#dataGridServerGroupComboBoxField");
						combobox.reset();
                    }
				}
			]
        },
        {
            xtype: 'hiddenfield',
            itemId: 'domainIdHiddenField',
            fieldLabel: 'Label',
            name: 'id'
        }
    ]

});