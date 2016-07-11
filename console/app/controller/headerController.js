/*
 * File: app/controller/headerController.js
 */

Ext.define('webapp.controller.headerController', {
    extend: 'Ext.app.Controller',

    refs: {
        logoImg: '#logoImg'
    },

    onLaunch: function() {
        /**
         * CI Logo Image click event를 catch 하도록 설정
         */
        this.getLogoImg().getEl().on('click', function() {
            window.open('http://www.osci.kr', '_blank');
        });
    }

});
