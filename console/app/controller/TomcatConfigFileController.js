/*
 * File: app/controller/TomcatConfigFileController.js
 */

Ext.define('webapp.controller.TomcatConfigFileController', {
    extend: 'Ext.app.Controller',

    displayConfigFile: function(objId, objType, confType, form) {
        var latestVersionUrl =GlobalData.urlPrefix + "configfile/"+ confType;
        var versionsLoadUrl = GlobalData.urlPrefix + "configfile/"+ confType;

        if(objType === GlobalData.objTypeTomcatInst){
            latestVersionUrl = latestVersionUrl + "/latest/domain/0/tomcat/" + objId;
            versionsLoadUrl = versionsLoadUrl + "/domain/0/tomcat/" + objId;
        }
        else if(objType === GlobalData.objTypeTomcatDomain){
            latestVersionUrl = latestVersionUrl + "/latest/domain/" + objId +"/tomcat/0";
            versionsLoadUrl = versionsLoadUrl + "/domain/" + objId+"/tomcat/0";
        }

        webapp.app.getController("globalController").ajaxRequest(latestVersionUrl, {}, "GET", function(json){
            var content = "";
            var latestVersionId = 0;
            if(json.data !== null){
                content = json.data.content;
                latestVersionId = json.data.id;
            }
            form.bindingData(versionsLoadUrl, latestVersionId, objType, confType, content);
        },null);
    }

});
