/*
 * File: app/controller/UserController.js
 *
 * This file was generated by Sencha Architect version 3.2.0.
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

Ext.define('webapp.controller.UserController', {
    extend: 'Ext.app.Controller',
    alias: 'controller.UserController',

    onNewUserButtonClick: function(button, e, eOpts) {
        this.showUserWindow("new", 0);
    },

    onContainerActivate: function(component, eOpts) {

        Ext.getStore("UserStore").load();
        Ext.getStore("UserRoleStore").load();
    },

    onSubmitButtonClick: function(button, e, eOpts) {
        var form = Ext.getCmp("userForm");			// user form
        var formWindow = Ext.getCmp('UserWindow');	// Add user window

        var userName = form.getForm().findField("UserIDTextField");
        var password = form.getForm().findField("PasswordTextField");
        var retypePassword = form.getForm().findField("RetypePasswordTextField");
        var fullName = form.getForm().findField("FullNameTextField");
        var email = form.getForm().findField("EmailTextField");
        var userRole = form.getForm().findField("UserRoleDropdownList");
        var _id = form.getForm().findField("IDHiddenField");

        var userNameVal = userName.getValue().trim();
        var passwordVal = password.getValue().trim();
        var retypePasswordVal = retypePassword.getValue().trim();
        var fullNameVal = fullName.getValue().trim();
        var emailVal = email.getValue().trim();
        var userRoleVal = userRole.getValue();
        var _idVal = _id.getValue();

        if (!this.validate(userNameVal, passwordVal, retypePasswordVal, fullNameVal, emailVal, userRoleVal)) {
            return;
        }

        //submit new user request
        if (_idVal === "") {
            _idVal = 0;

        }

        this.save({"id" : _idVal, "username" : userNameVal,"password" : passwordVal, "fullName" : fullNameVal, "email":emailVal, "userRole":userRoleVal});



    },

    onTextfieldSpecialkey: function(field, e, eOpts) {
        if (e.getKey()===e.ENTER){
            var store = Ext.getStore("UserStore");
            var url = GlobalData.urlPrefix + "user/search";
            Ext.Ajax.request({
                url: url,
                params: {"userName":field.getValue()},
                success: function(resp, ops) {
                    var response = Ext.decode(resp.responseText);
                    Ext.getStore("UserStore").loadData(response, false);
                    Ext.getStore("UserStore").totalCount = response.length;
                    Ext.getCmp("userListPaging").onLoad();
                }
            });
        }
    },

    onUserRoleCreateBtnClick: function(button, e, eOpts) {
        this.showUserRoleWindow(0,"add");
    },

    onBtnUserRoleSubmitClick: function(button, e, eOpts) {
        var form = Ext.getCmp("userRoleForm");			// user form
        var formWindow = Ext.getCmp('UserRoleWindow');	// Add user window

        var userRoleName = form.getForm().findField("userRoleNameTextField");
        var _id = form.getForm().findField("IDHiddenField");

        var userRoleNameVal = userRoleName.getValue().trim();
        var _idVal = _id.getValue();

        if (!this.validateUserRole(userRoleNameVal)) {
            return;
        }

        //submit new user request
        if (_idVal === "") {
            _idVal = 0;

        }

        this.saveUserRole({"id" : _idVal, "name" : userRoleNameVal});

    },

    showUserWindow: function(type, user_id) {

        var userWindow = Ext.create("widget.UserWindow");
        var submitButton = Ext.getCmp("btnSubmit");
        if (type === "edit"){
            userWindow.setTitle("Edit User");
            submitButton.setText("Save");
            var form = Ext.getCmp("userForm");			// user form

            var userName = form.getForm().findField("UserIDTextField");
            var fullName = form.getForm().findField("FullNameTextField");
            var email = form.getForm().findField("EmailTextField");
            var userRole = form.getForm().findField("UserRoleDropdownList");
            var _id = form.getForm().findField("IDHiddenField");
            //load data to user form

             Ext.Ajax.request({
                    url: GlobalData.urlPrefix + "user/edit",
                    params: {"id":user_id},
                    success: function(resp, ops) {
                        var response = Ext.decode(resp.responseText);
                        userName.setValue(response.username);
                        fullName.setValue(response.fullName);
                        email.setValue(response.email);
                        userRole.setValue(response.userRoleId);
                        _id.setValue(user_id);
                    }
                });

        }

        userWindow.show();
    },

    validate: function(userName, password, retype_password, fullName, email, userRole) {
        if (password !== retype_password){
            Ext.Msg.show({
                title: "Message",
                msg: "Password and Retype password are not match.",
                buttons: Ext.Msg.OK,
                fn: function(choice) {
                    password.focus();
                },
                icon: Ext.Msg.WARNING
            });
            return false;
        }

        if (userName === "" || password === "" || retype_password === ""||fullName === "" ||email === ""||userRole < 0){
            Ext.Msg.show({
                title: "Message",
                msg: "Invalid data.",
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.WARNING
            });
            return false;
        }
        return true;
    },

    validateUserRole: function(name) {
        if (name === ""){
            Ext.Msg.show({
                title: "Message",
                msg: "Invalid data.",
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.WARNING
            });
            return false;
        }
        return true;
    },

    save: function(params) {
        var url = GlobalData.urlPrefix + "user/save";
        var userWindow = Ext.getCmp('UserWindow');	// Add user window
        Ext.Ajax.request({
             url: url,
             params: params,
             success: function(resp, ops) {

                    var response = Ext.decode(resp.responseText);
                    if(response===true){
                        Ext.getStore("UserStore").reload();
                        Ext.getStore("UserRoleStore").reload();
                        userWindow.close();
                    }
                    else {
                             Ext.Msg.show({
                                title: "Message",
                                msg: "Invalid information.",
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                    }

                }
            });


    },

    deleteUser: function(id) {
         Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this user?', function(btn){

             if(btn == "yes"){
                Ext.Ajax.request({
                    url: GlobalData.urlPrefix+ "/user/delete",
                    params: {"id":id},
                    success: function(resp, ops) {

                        var response = Ext.decode(resp.responseText);
                        if(response===true){
                            Ext.getStore("UserStore").reload();
                        }
                        else {
                            Ext.Msg.show({
                                title: "Message",
                                msg: "User is not existed",
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                        }

                    }
                });


             }
         });
    },

    showUserRoleWindow: function(id, type) {
        var userRoleWindow = Ext.create("widget.UserRoleWindow");
        var submitButton = Ext.getCmp("btnUserRoleSubmit");
        if (type === "edit"){
            userRoleWindow.setTitle("Edit User Role");
            submitButton.setText("Save");
            var form = Ext.getCmp("userRoleForm");			// user role form

            var userRoleName = form.getForm().findField("UserRoleNameTextField");
            var _id = form.getForm().findField("IDHiddenField");
            //load data to user form

             Ext.Ajax.request({
                    url: GlobalData.urlPrefix + "user/role/edit",
                    params: {"id":id},
                    success: function(resp, ops) {
                        var response = Ext.decode(resp.responseText);
                        userRoleName.setValue(response.name);
                        _id.setValue(id);
                    }
                });

        }

        userRoleWindow.show();
    },

    saveUserRole: function(params) {
        var url = GlobalData.urlPrefix + "user/role/save";
        var userRoleWindow =Ext.getCmp("UserRoleWindow");	// Add user role window
        Ext.Ajax.request({
             url: url,
             params: params,
             success: function(resp, ops) {

                    var response = Ext.decode(resp.responseText);
                    if(response===true){
                        Ext.getStore("UserRoleStore").reload();
                        Ext.getStore("UserStore").reload();
                        userRoleWindow.close();
                    }
                    else {
                             Ext.Msg.show({
                                title: "Message",
                                msg: "Invalid information.",
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                    }

                }
            });

    },

    deleteUserRole: function(id) {
         Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this role?', function(btn){

             if(btn == "yes"){
                Ext.Ajax.request({
                    url: GlobalData.urlPrefix+ "/user/role/delete",
                    params: {"id":id},
                    success: function(resp, ops) {

                        var response = Ext.decode(resp.responseText);
                        if(response===true){
                            Ext.getStore("UserRoleStore").reload();
                            Ext.getStore("UserStore").reload();
                        }
                        else {
                            Ext.Msg.show({
                                title: "Message",
                                msg: "User role is not existed",
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                        }

                    }
                });


             }
         });
    },

    init: function(application) {
        this.control({
            "#createBtn": {
                click: this.onNewUserButtonClick
            },
            "#mycontainer38": {
                activate: this.onContainerActivate
            },
            "#btnSubmit": {
                click: this.onSubmitButtonClick
            },
            "#mytextfield": {
                specialkey: this.onTextfieldSpecialkey
            },
            "#userRoleCreateBtn": {
                click: this.onUserRoleCreateBtnClick
            },
            "#btnUserRoleSubmit": {
                click: this.onBtnUserRoleSubmitClick
            }
        });
    }

});
