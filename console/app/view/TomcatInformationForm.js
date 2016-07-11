/*
 * File: app/view/SSHFormPanel.js
 */

Ext.define('webapp.view.TomcatInformationForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.tomcatinformationform',

     requires: [
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden'
    ],

    itemId: 'tomcatInfoForm',
    bodyPadding: 10,
	bodyCls: 'osc-body',
    frameHeader: false,
    header: false,
    manageHeight: false,
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 100,
        labelAlign: 'right',
		width: 250
    },
    items: [
			{
				xtype: 'fieldcontainer',
				layout: 'hbox',
				hideLabel: true,
				items: [
					{
						xtype: 'displayfield',
						fieldLabel: 'Health',
						value: 'OK'
					},
					{
						xtype: 'displayfield',
						fieldLabel: 'Availability',
						value: 'OK'
					},
					{
						xtype: 'displayfield',
						fieldLabel: 'Today Availability',
						value: '100%'
					}
				]
			},
			{
				xtype: 'fieldcontainer',
				layout: 'hbox',
				hideLabel: true,
				items: [
					{
						xtype: 'displayfield',
						fieldLabel: 'Today\'s Uptime',
						value: '12 hrs 10 mins 13 secs'
					},
					{
						xtype: 'displayfield',
						fieldLabel: 'Last Downtime',
						value: '2016/04/16 15:00:34'
					}
				]
			}
	]

});