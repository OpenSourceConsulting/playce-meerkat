/*
 * File: app/controller/footerController.js
 */

Ext.define('webapp.controller.footerController', {
    extend: 'Ext.app.Controller',

    refs: {
        footerLabel2: '#footerLabel2'
    },

    onLaunch: function() {
        /**
         * address Label click event를 catch 하도록 설정
         */
        this.getFooterLabel2().getEl().on('click', function() {
            var mapwin;

            // create the window on the first click and reuse on subsequent clicks
            if(mapwin) {
                mapwin.show();
            } else {
                mapwin = Ext.create('Ext.window.Window', {
                    autoShow: true,
                    layout: 'fit',
                    title: 'OSCI Location',
                    closeAction: 'hide',
                    width:600,
                    height:500,
                    border: true,
                    x: 40,
                    y: 60,
                    items: {
                        xtype: 'gmappanel',
                        center: {
                            geoCodeAddr: '서울특별시 서초구 서초2동 1337'
                        },
                        markers: [{
                            lat: 37.492359,
                            lng: 127.028590,
                            title: 'Gangnam Mirae Tower 805, Saimdang-ro 174(Seocho-dong), Seocho-gu, Seoul, Korea',
                            listeners: {
                                click: function(e){
                                    Ext.Msg.alert('Address', 'Gangnam Mirae Tower 805, Saimdang-ro 174(Seocho-dong), Seocho-gu, Seoul, Korea');
                                }
                            }
                        }]
                    }
                });
            }
        });

        // Add below script to index.html manually
        // <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    }

});
