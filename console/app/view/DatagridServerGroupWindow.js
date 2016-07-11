/*
 * File: app/view/DatagridServerGroupWindow.js
 */

Ext.define('webapp.view.DatagridServerGroupWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.datagridservergroupwindow',

    requires: [
        'Ext.form.FieldSet',
        'Ext.form.field.ComboBox',
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.button.Button'
    ],
	modal : true,
    height: 530,
    width: 553,
    title: 'New Session Server Group',
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
			xtype:'datagridservergroupform'
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
					//xtype: 'button',// default is button
					id: 'btnSubmitServerGroup',
					itemId: 'btnSubmitServerGroup',
					text: 'Create'
			    },
			    {
					handler: function(button, e) {
						Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){
							if(btn == "yes"){
								button.up("window").close();
							}
						});
					},
					text: 'Cancel'
			    },
				{
					id:"newServerGroupWindowBtn",
					text: 'New Sever'
				}
			]
        }
    ]
});