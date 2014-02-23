/**
 * Created by hong on 14-2-11 下午12:33.
 */
function QueryModuleScript(){}

QueryModuleScript.prototype = {
    initValidate:function() {
        $("#saveForm").validate({
            submitHandler:function(form) {
                $('#submitBtn').linkbutton('disable');
                form.submit();
            },
            rules: {
                "name": "required",
                "pageSize": {
                    digits: true
                }
            },
            messages: {
                "name": "请填写名称",
                "pageSize": "只能输入整数"
            },
            showErrors:function(errorMap, errorList) {
                var msg = "";
                $.each(errorList, function(i, v) {
                    msg += (v.message + "<br/>");
                });
                if (msg.trim() != "") {
                    scriptTools.showMsg(msg, 3000);
                }
            },
            onfocusout: false   // 失去焦点不验证
        });
    }
}

function fieldEasyuiScript() {
    this.url = contextPath + "/manager/sys/field/saveOrUpdate";
    this.flag = "";
}

fieldEasyuiScript.prototype = {
    addField:function() {
        $("#id").val("");
        $("#version").val("");
        $('#dlg').dialog('open').dialog('setTitle', '新建字段');
        this.flag = "add";
    },
    editField:function() {
        var row = $("#dg").datagrid("getSelected");
        if (row == undefined) {
            scriptTools.showMsg("请选择一条记录", "");
        } else {
            $.ajax({
                type: "get",
                url: contextPath + "/manager/sys/field/edit",
                data: "id=" + row.id,
                dataType: "json",
                success: function(result) {
                    $("#fieldForm").form("load", result);
                    $("#fieldName").combobox("reload", contextPath
                        + "/manager/sys/field/loadFieldName?tableName=" + result.tableName);
                    if (result.queryCondition == true) {
                        $("#queryCondition").val(1);
                    }
                    if (result.display == true) {
                        $("#display").val(1);
                    }
                    if (result.print == true) {
                        $("#print").val(1);
                    }
                    $("#dlg").dialog("open").dialog("setTitle", "修改字段");
                }
            });
        }
    },
    removeField:function() {
        var row = $("#dg").datagrid("getSelected");
        if (row == undefined) {
            scriptTools.showMsg("请选择一条记录", "");
        } else {
            $.ajax({
                type: "get",
                url: contextPath + "/manager/sys/field/delete",
                data: "id=" + row.id,
                dataType: "json",
                success: function(result) {
                    if (result.errorMsg) {
                        scriptTools.showMsg(result.errorMsg, "");
                    } else {
                        scriptTools.showMsg("删除成功", "");
                        var index = $("#dg").datagrid("getRowIndex", row);
                        $("#dg").datagrid("deleteRow", index);
                    }
                }
            });
        }
    },
    saveField:function() {
        var _this = this;
        $("#fieldForm").form('submit', {
            url: _this.url + "?queryModuleId=" + $("#moduleId").html(),
            onSubmit: function() {
                return $(this).form('validate');
            },
            success: function(result) {
                var result = eval('(' + result + ')');
                if (result.errorMsg){
                    scriptTools.showMsg(result.errorMsg, "")
                } else {
                    $('#dlg').dialog('close');
                    $('#dg').datagrid('reload');

                    var value = $("#querySequence").numberbox("getValue");
                    $("#querySequence").numberbox("setValue", parseInt(value) + 1);
                    value = $("#displaySequence").numberbox("getValue");
                    $("#displaySequence").numberbox("setValue", parseInt(value) + 1);
                    value = $("#printSequence").numberbox("getValue");
                    $("#printSequence").numberbox("setValue", parseInt(value) + 1);
                }
            }
        });
    }
}

function linkEasyuiScript() {
    this.flag = "";
    this.url = contextPath + "/manager/sys/listLink/saveOrUpdate";
}

linkEasyuiScript.prototype = {
    addLink:function() {
        $("#id").val("");
        $("#version").val("");
        $('#dlg').dialog('open').dialog('setTitle', '新建链接');
        this.flag = "add";
    },
    editLink:function() {
        var row = $("#dg").datagrid("getSelected");
        if (row == undefined) {
            scriptTools.showMsg("请选择一条记录", "");
        } else {
            $.ajax({
                type: "get",
                url: contextPath + "/manager/sys/listLink/edit",
                data: "id=" + row.id,
                dataType: "json",
                success: function(result) {
                    $("#linkForm").form("load", result);
                    if (result.display == true) {
                        $("#display").val(1);
                    }
                    if (result.verify == true) {
                        $("#verify").val(1);
                    }
                    $("#dlg").dialog("open").dialog("setTitle", "修改链接");
                }
            });
        }
    },
    removeLink:function() {
        var row = $("#dg").datagrid("getSelected");
        if (row == undefined) {
            scriptTools.showMsg("请选择一条记录", "");
        } else {
            $.ajax({
                type: "get",
                url: contextPath + "/manager/sys/listLink/delete",
                data: "id=" + row.id,
                dataType: "json",
                success: function(result) {
                    if (result.errorMsg) {
                        scriptTools.showMsg(result.errorMsg, "");
                    } else {
                        scriptTools.showMsg("删除成功", "");
                        var index = $("#dg").datagrid("getRowIndex", row);
                        $("#dg").datagrid("deleteRow", index);
                    }
                }
            });
        }
    },
    saveLink:function() {
        var _this = this;
        $("#linkForm").form('submit', {
            url: _this.url + "?queryModuleId=" + $("#moduleId").html(),
            onSubmit: function() {
                return $(this).form('validate');
            },
            success: function(result) {
                var result = eval('(' + result + ')');
                if (result.errorMsg){
                    scriptTools.showMsg(result.errorMsg, "")
                } else {
                    $('#dlg').dialog('close');
                    $('#dg').datagrid('reload');

                    var value = $("#sequence").numberbox("getValue");
                    $("#sequence").numberbox("setValue", parseInt(value) + 1);
                }
            }
        });
    }
}
