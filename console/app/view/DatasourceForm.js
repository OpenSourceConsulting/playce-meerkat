/*
 * File: app/view/DatasourceForm.js
 */

Ext.define('webapp.view.DatasourceForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.datasourceform',

    requires: [
        'Ext.form.field.ComboBox',
        'Ext.form.field.Checkbox',
        'Ext.form.FieldSet',
        'Ext.form.field.Number',
        'Ext.form.Label'
    ],
    id: 'datasourceForm',
    bodyPadding: 10,
    defaultListenerScope: true,

    items: [
        {
            xtype: 'textfield',
            anchor: '100%',
            id: 'formDSNameField',
            fieldLabel: 'Datasource name',
            labelWidth: 120
        },
        {
            xtype: 'combobox',
            anchor: '100%',
            id: 'formDSDBTypeCombobox',
			itemId: 'dbTypeCombobox',
            fieldLabel: 'Database 유형 :',
            labelWidth: 120,
            editable: false,
            displayField: 'codeNm',
            store: 'DBTypeStore',
            valueField: 'id'
        },
        {
            xtype: 'textfield',
            anchor: '100%',
            id: 'formDSJDBCUrlField',
            fieldLabel: 'JDBC Url',
            labelWidth: 120,
			name:'jdbcUrl'
        },
        {
            xtype: 'textfield',
            anchor: '100%',
            id: 'formDSUsernameField',
            fieldLabel: 'Username',
            labelWidth: 120
        },
        {
            xtype: 'container',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'textfield',
                    id: 'formDSPasswordField',
                    fieldLabel: '비밀번호',
                    labelWidth: 120,
                    inputType: 'password'
                },
                {
                    xtype: 'checkboxfield',
                    margin: '0 0 0 10',
                    fieldLabel: '',
                    boxLabel: 'Show password',
                    listeners: {
                        change: 'onCheckboxfieldChange'
                    }
                }
            ]
        },
        {
            xtype: 'fieldset',
            title: 'Connection pool',
            items: [
                {
                    xtype: 'numberfield',
                    anchor: '100%',
                    id: 'formDSMaxConnField',
                    fieldLabel: '최대 연결 개수 ',
                    labelWidth: 120
                },
                {
                    xtype: 'numberfield',
                    anchor: '100%',
                    id: 'formDSMaxConnPoolField',
                    fieldLabel: '최대 연결 Pool 개수',
                    labelWidth: 120
                },
                {
                    xtype: 'numberfield',
                    anchor: '100%',
                    id: 'formDSMinConnPoolField',
                    fieldLabel: '최소 연결 Pool 개수',
                    labelWidth: 120
                },
                {
                    xtype: 'container',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'numberfield',
                            id: 'formDSTimeoutField',
                            fieldLabel: '최대 연결 대기 시간',
                            labelWidth: 120
                        },
                        {
                            xtype: 'label',
                            margin: '0 0 0 10',
                            text: '  (ms)'
                        }
                    ]
                }
            ]
        }
    ],

    onCheckboxfieldChange: function(field, newValue, oldValue, eOpts) {
        var passwordField = Ext.getCmp("formDSPasswordField");
        if(!newValue){
            passwordField.inputEl.dom.setAttribute('type', 'password');
            passwordField.inputEl.addCls('x-form-password');
        }
        else{
            passwordField.inputEl.dom.setAttribute('type', 'text');
            passwordField.inputEl.addCls('x-form-text');
            passwordField.inputEl.removeCls('x-form-password');
        }
    }
});