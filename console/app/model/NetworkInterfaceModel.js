/*
 * File: app/model/NetworkInterfaceModel.js
 */

Ext.define('webapp.model.NetworkInterfaceModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.Field'
    ],

    fields: [
        {
            name: 'name'
        },
        {
            name: 'ipv4'
        },
        {
            name: 'ipv6'
        },
        {
            name: 'netmask'
        },
        {
            name: 'id'
        },
        {
            name: 'defaultGateway'
        },
        {
            name: 'nameAndIPAddr'
        }
    ]
});