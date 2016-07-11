/*
 * File: app/controller/UserController.js
 */

Ext.define('webapp.controller.UserController', {
    extend: 'Ext.app.Controller',
    alias: 'controller.UserController',

    control: {
        "#createBtn": {
            click: 'onNewUserButtonClick'
        },
        "#mycontainer38": {
            activate: 'onContainerActivate'
        },
        "#btnSubmit": {
            click: 'onSubmitButtonClick'
        },
        "#mytextfield": {
            specialkey: 'onTextfieldSpecialkey'
        },
        "#userRoleCreateBtn": {
            click: 'onUserRoleCreateBtnClick'
        },
        "#btnUserRoleSubmit": {
            click: 'onBtnUserRoleSubmitClick'
        }
    },

    onNewUserButtonClick: function(button, e, eOpts) {
        this.showUserWindow("new", 0);
    },

    onContainerActivate: function(component, eOpts) {

        Ext.getStore("UserStore").load();
        Ext.getStore("UserRoleStore").load();
    },

    onSubmitButtonClick: function(button, e, eOpts) {
        var form = Ext.getCmp("userForm");			// user form
    
		if(form.getForm().isValid()){
			var password = form.down("[name=password]");
			var retype_password = form.down("[name=retypePassword]");
			if(password.getValue()!== retype_password.getValue()){
				Ext.Msg.show({
					title: "Message",
					msg: "Retype password not match.",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING
				});
				retype_password.addCls("x-form-empty-field");
				retype_password.setActiveError("Password not match");
				return;
			}
			else{
				retype_password.removeCls("x-form-empty-field");
			}
			
			var url = GlobalData.urlPrefix + "user/save";
			var items = form.down("grid").getStore();
			var userRoleIds = "";
			items.each(function(rec){
				if(rec.get("selected") === true){
					userRoleIds += "#" + rec.get("id");
				}
			});
			form.getForm().submit({
				url: url,
				params:{"userRoleStrIds": userRoleIds},
				submitEmptyText: false,
				waitMsg: 'Saving Data...',
				success: function(formBasic, action){
					var response = Ext.decode(action.response.responseText);
					if(response.success){
						Ext.getStore("UserStore").reload();
                        Ext.getStore("UserRoleStore").reload();
                        form.up("window").close();
					}
				}
			});
		}

    },

    onTextfieldSpecialkey: function(field, e, eOpts) {

        if(e.getKey() === e.ENTER){
            var store = Ext.getStore("UserStore");
            var url = GlobalData.urlPrefix + "user/search";
			var store = field.up("gridpanel").getStore();
			store.getProxy().url = url;
			store.getProxy().extraParams = {"userName":field.getValue()};
			store.load();
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
			form.getForm().load({
				url: GlobalData.urlPrefix + "user/edit",
				params: {"id":user_id},
			    submitEmptyText: false,
                waitMsg: 'Loading Data...',
				success: function(formBasic, action){
					var response = Ext.decode(action.response.responseText);
					var roles = response.data.userRoles;
					form.down("[name=userRoles]").getStore().loadData(roles, false);
					
				}
			});

        }

        userWindow.show();
    },

    validate: function(userName, password, retype_password, fullName, email, userRole) {
        if(password.getValue()!== retype_password.getValue()){
            Ext.Msg.show({
                        title: "Message",
                        msg: "Retype password not match.",
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.WARNING
                    });
            retype_password.addCls("x-form-empty-field");
            retype_password.setActiveError("Password not match");
            return false;
        }
        else{
            retype_password.removeCls("x-form-empty-field");
        }

        if (!(userName.isValid()  && password.isValid() && retype_password.isValid() && fullName.isValid() && email.isValid() && userRole.getValue() > 0)){
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
                    url: GlobalData.urlPrefix+ "user/delete",
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
                    url: GlobalData.urlPrefix+ "user/role/delete",
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
    }

});
