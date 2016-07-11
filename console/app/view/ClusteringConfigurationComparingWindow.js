/*
 * File: app/view/ClusteringConfigurationComparingWindow.js
 */

Ext.define('webapp.view.ClusteringConfigurationComparingWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.ClusteringConfigurationComparingWindow',

    requires: [
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.view.Table',
        'Ext.toolbar.Paging'
    ],
    height: 483,
    id: 'clusteringConfigurationComparingWindow',
    width: 633,
    title: 'Clutering Configuration Comparison',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
	
    items: [
        {
            xtype: 'gridpanel',
            flex: 9,
            id: 'clusteringConfComparisonGrid',
            margin: '10 10 10 10',
            forceFit: true,
            store: 'ClusteringConfComparisonStore',
			
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'name',
                    text: 'Name'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'firstValue',
                    text: 'Value'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'secondValue',
                    text: 'Compared Value'
                }
            ],
			
            dockedItems: [
                {
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    width: 360,
                    displayInfo: true,
                    store: 'ClusteringConfComparisonStore'
                }
            ]
        }
    ]
});