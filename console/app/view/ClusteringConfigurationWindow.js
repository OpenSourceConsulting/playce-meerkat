/*
 * File: app/view/ClusteringConfigurationWindow.js
 */

Ext.define('webapp.view.ClusteringConfigurationWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.ClusteringConfigurationWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.form.field.Hidden'
    ],
	
    height: 159,
    id: 'clusteringConfigurationWindow',
    width: 400,
    title: 'New Configuration ',
    modal: true,

    items: [
        {
            xtype: 'form',
            id: 'clusteringConfigurationForm',
            bodyPadding: 10,
            items: [
				{
                    xtype: 'hiddenfield',
					itemId: 'Id',
                    name: 'id'
                },
                {
                    xtype: 'textfield',
                    id: 'clusteringConfigurationName',
                    margin: '10 0 10 10',
                    width: 352,
                    fieldLabel: 'Name',
                    name: 'name',
                    allowBlank: false,
                    allowOnlyWhitespace: false
                },
                {
                    xtype: 'textfield',
                    id: 'clusteringConfigurationValue',
                    margin: '0 0 10 10',
                    width: 353,
                    fieldLabel: 'Value',
                    name: 'value',
                    allowBlank: false,
                    allowOnlyWhitespace: false
                },
                {
                    xtype: 'button',
                    id: 'btnClusteringConfigurationSubmit',
                    itemId: 'btnClusteringConfigurationSubmit',
                    margin: '0 0 0 120',
                    text: 'Create'
                },
                {
                    xtype: 'button',
                    margin: '0 0 0 10',
                    text: 'Cancel'
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    id: 'objIdClusteringWindowHiddenField',
                    itemId: 'objIdClusteringWindowHiddenField',
                    fieldLabel: 'Label',
                    name: 'objectId'
                }
                /*{
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    id: 'clusteringVersionHiddenField',
                    itemId: 'clusteringVersionHiddenField',
                    fieldLabel: 'Label',
                    name: 'IDHiddenField'
                },
                {
                    xtype: 'hiddenfield',
                    id: 'objTypeClusteringWindowHiddenField',
                    fieldLabel: 'Label'
                }*/
            ]
        }
    ]
});