/*
 * File: app/view/AlertSettingWindow.js
 */

Ext.define('webapp.view.AlertSettingWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.AlertSettingWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],
    height: 120,
    id: 'alertSettingWindow',
    width: 432,
    title: 'Modify alert setting',
    modal: true,
    defaultListenerScope: true,
	layout: 'fit',
    items: [
        {
			xtype:'form',
			bodyPadding: 10,
            frameHeader: false,
			defaults: {
				margin: '5 5 5 5'
			},
			layout: 'hbox',
            header: false,
			items: [
				{
					xtype:'displayfield',
					name:'alertItemCdNm'
				},
				{
					xtype:'combobox',
					name:'thresholdOpCdId',
					store:"AlertOperatorStore",
					displayField: 'codeNm',
					valueField: 'id',
					width:150
				},
				{
					xtype:'textfield',
					width:70,
					name:'thresholdValue'
				},
				{
					xtype:'displayfield',
					value:'%'
				},
				{
					xtype:'hiddenfield',
					name:'id'
				},
				{
					xtype:'hiddenfield',
					name:'alertItemCdId'
				}
			]
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'bottom',
            layout: {
                type: 'hbox',
                pack: 'center'
            },
            items: [
                {
                    xtype: 'button',
                    text: 'Save',
					itemId:'saveAlertBtn'
                },
                {
                    xtype: 'button',
					text: 'Cancel',
					handler: function(button){
						Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){
							if(btn == "yes"){
								button.up("window").close();
							}
						});
					}
                    
                }
            ]
        }
    ]

});