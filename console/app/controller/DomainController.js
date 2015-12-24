/*
 * File: app/controller/DomainController.js
 *
 * This file was generated by Sencha Architect version 3.0.0.
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

Ext.define('webapp.controller.DomainController', {
    extend: 'Ext.app.Controller',

    onContainerActivate: function(component, eOpts) {
        var nameField = Ext.getCmp("domainNameField");
        var tomcatCountField = Ext.getCmp("tomcatInstancesField");
        var domainTypeField  = Ext.getCmp("domainTypeField");
        var dataGridServerGroupField = Ext.getCmp("datagridServerGroupField");

        Ext.Ajax.request({
            url: GlobalData.urlPrefix + "domain/get",
            params: {"id":GlobalData.lastSelectedMenuId},
            method:'GET',
            success: function(resp, ops) {

                var response = Ext.decode(resp.responseText);
                nameField.setValue(response.name);
                tomcatCountField.setValue(response.tomcatInstancesCount);
                domainTypeField.setValue(response.isClustering===true?"Clustering":"None clustering");
                dataGridServerGroupField.setValue(response.datagridServerGroupName);
            }
        });
    },

    init: function(application) {
        this.control({
            "#mycontainer37": {
                activate: this.onContainerActivate
            }
        });
    }

});
