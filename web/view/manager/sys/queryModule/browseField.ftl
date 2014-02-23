<!DOCTYPE html>
<html>
<head>
    <title>查询生成器</title>
    <link id="tableTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/css/themes/default/table.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/form.css">
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/icon.css">

    <script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>

    <script type="text/javascript" src="${rc.contextPath}/view/manager/sys/queryModule/queryModule.js"></script>

    <script type="text/javascript">
        var scriptTools = new ScriptTools();
        var contextPath = "${rc.contextPath}";
        var easyuiInstance = new fieldEasyuiScript();
        $(function(){
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");
            $("#tableName").blur(function() {
                if ($("#tableName").val().trim() != "") {
                    $.ajax({
                        type: "get",
                        url: contextPath + "/manager/sys/field/checkTableName",
                        data: "tableName=" + $("#tableName").val(),
                        dataType: "json",
                        success: function(result) {
                            if (result.errorMsg) {
                                scriptTools.showMsg(result.errorMsg, "");
                            } else {
                                $("#fieldName").combobox("reload", contextPath
                                        + "/manager/sys/field/loadFieldName?tableName=" + $("#tableName").val());
                            }
                        }
                    });
                }
            });
        });

    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;查询生成器 -> 查询字段清单
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <img src="${rc.contextPath}/images/diy/2.png"/>
    <a href="${rc.contextPath}/manager/sys/queryModule/edit?id=${queryModule.id}">查询模块</a>&nbsp;&nbsp;&nbsp;
    <img src="${rc.contextPath}/images/diy/2.png"/>
    <a href="${rc.contextPath}/manager/sys/listLink/browse?queryModuleId=${queryModule.id}">查询列表链接</a>
</div>
<table cellpadding="0" cellspacing="0" class="table1" style="margin: 0; width: 100%">
    <thead>
    <tr>
        <td colspan="4">查询模块信息</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="fieldtitle">id号：</td>
        <td class="fieldinput"><div id="moduleId">${queryModule.id}</div></td>
        <td class="fieldtitle">名 称：</td>
        <td class="fieldinput"><div id="moduleName">${queryModule.name}</div></td>
    </tr>
    <tr>
        <td class="fieldtitle">表之间关系SQL语句：</td>
        <td class="fieldinput" colspan="3">
            <div id="relationSql">${queryModule.relationSql}</div>
        </td>
    </tr>
    </tbody>
</table>
<br/>
<table class="easyui-datagrid" title="字段列表" id="dg"
       data-options="singleSelect:true, fitColumns:true, rownumbers:true, toolbar: '#tb',
        url:'${rc.contextPath}/manager/sys/field/queryFieldList?queryModuleId=${queryModule.id}', method:'get',
        onLoadSuccess:function(data) {
            $('#dg').datagrid('hideColumn', 'id');
        }">
    <thead>
    <th data-options="field:'displayName', width:$(this).width()*0.125">显示名称</th>
    <th data-options="field:'fieldName', width:$(this).width()*0.125">字段名称</th>
    <th data-options="field:'tableName', width:$(this).width()*0.125">表 名</th>
    <th data-options="field:'tableAliasName', width:$(this).width()*0.125">表别名</th>
    <th data-options="field:'fieldAliasName', width:$(this).width()*0.125">字段别名</th>
    <th data-options="field:'isDisplay', width:$(this).width()*0.125">是否显示</th>
    <th data-options="field:'querySequence', width:$(this).width()*0.125">条件顺序</th>
    <th data-options="field:'displaySequence', width:$(this).width()*0.125">显示顺序</th>
    <th data-options="field:'id'"></th>
    </thead>
</table>

<div id="tb" style="height: auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="easyuiInstance.addField()">添 加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="easyuiInstance.editField()">修 改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="easyuiInstance.removeField()">删 除</a>
</div>

<div id="dlg" class="easyui-dialog" style="width: 900px;height:500px;padding:10px;"
     data-options="iconCls:'icon-save', collapsible:true, minimizable:true, maximizable:true,
         resizable:true, closable:true, closed:true">
    <form id="fieldForm" method="post" class="form1">
        <input type="hidden" name="id" id="id"/>
        <input type="hidden" name="version" id="version"/>
        <table cellpadding="0" cellspacing="2" style="width: 100%">
            <tr>
                <td class="fieldtitle">字段显示名称：</td>
                <td class="fieldinput">
                    <input name="displayName" id="displayName" class="inputtxt easyui-validatebox" data-options="required:true">
                </td>
                <td class="fieldtitle">字段操作类型：</td>
                <td class="fieldinput">
                    <select name="fieldOpType" class="selectfield">
                    <#list fieldOpTypeList! as instance>
                        <option value="${(instance.number)!}">${(instance.name)!}</option>
                    </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">表 名：</td>
                <td class="fieldinput">
                    <input name="tableName" id="tableName" class="inputtxt easyui-validatebox" data-options="required:true">
                </td>
                <td class="fieldtitle">表别名：</td>
                <td class="fieldinput">
                    <input name="tableAliasName" id="tableAliasName" class="inputtxt">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">字段名：</td>
                <td class="fieldinput">
                    <input name="fieldName" id="fieldName" class="combotree easyui-combobox"
                        data-options="required:true, multiple:false, valueField:'label', textField:'text'">
                </td>
                <td class="fieldtitle">字段别名：</td>
                <td class="fieldinput">
                    <input name="fieldAliasName" id="fieldAliasName" class="inputtxt">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">替换值：</td>
                <td class="fieldinput">
                    <input name="replacementValue" id="replacementValue" class="inputtxt">
                </td>
                <td class="fieldtitle">字段数据类型：</td>
                <td class="fieldinput">
                    <select name="fieldDataType" class="selectfield">
                        <#list dataTypeList! as instance>
                            <option value="${(instance.number)!}">${(instance.name)!}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">超链接：</td>
                <td class="fieldinput" colspan="3">
                    <input name="hyperlink" class="inputtxt"><br>
                    <span style="color:#ff0000">
                        注:若想通过弹出对话打开链接，格式如下：return showDialogOpenLink('url');
                    </span>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">是否查询条件：</td>
                <td class="fieldinput">
                    <select name="queryCondition" id="queryCondition" class="selectfield">
                        <#list yesOrNo! as instance>
                            <option value="${instance.number}">${instance.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="fieldtitle">显示查询条件顺序：</td>
                <td class="fieldinput">
                    <input name="querySequence" id="querySequence" value="1" class="inputtxt easyui-numberbox"
                           data-options="required:false, min:0">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">是否显示结果：</td>
                <td class="fieldinput">
                    <select name="display" id="display" class="selectfield">
                    <#list yesOrNo! as instance>
                        <option value="${instance.number}">${instance.name}</option>
                    </#list>
                    </select>
                </td>
                <td class="fieldtitle">显示结果顺序：</td>
                <td class="fieldinput">
                    <input name="displaySequence" id="displaySequence" value="1" class="inputtxt easyui-numberbox"
                           data-options="required:false, min:0">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">是否打印：</td>
                <td class="fieldinput">
                    <select name="print" id="print" class="selectfield">
                    <#list yesOrNo! as instance>
                        <option value="${instance.number}">${instance.name}</option>
                    </#list>
                    </select>
                </td>
                <td class="fieldtitle">打印顺序：</td>
                <td class="fieldinput">
                    <input name="printSequence" id="printSequence" value="1" class="inputtxt easyui-numberbox"
                           data-options="required:false, min:0">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">列宽：</td>
                <td class="fieldinput">
                    <input name="rowWidth" class="inputtxt easyui-numberbox" data-options="required:false, min:0"/>
                </td>
                <td class="fieldtitle">显示方式：</td>
                <td class="fieldinput">
                    <select name="displayType" class="selectfield">
                        <#list displayTypeList! as instance>
                            <option value="${instance.number}">${instance.name}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">排序类型：</td>
                <td class="fieldinput">
                    <select name="orderBy" class="selectfield">
                        <#list orderByList! as instance>
                            <option value="${instance.number}">${instance.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="fieldtitle">页面控件类型：</td>
                <td class="fieldinput">
                    <select name="controlKind" class="selectfield">
                        <option value="0">编辑框</option>
                        <option value="1">日期控件</option>
                        <option value="2">单选框</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">数据来源类型：</td>
                <td class="fieldinput">
                    <select name="dataSourceType" class="selectfield">
                        <#--<#list dataSourceTypeList! as instance>-->
                            <#--<option value="${instance.number}">${instance.name}</option>-->
                        <#--</#list>-->
                        <option value="0"> </option>
                        <option value="1">SQL语句</option>
                    </select>
                </td>
                <td class="fieldtitle">数据来源SQL语句：</td>
                <td class="fieldinput">
                    <input name="dataSourceSql" class="inputtxt"/><br/>
                    <span style="color:#ff0000">select value,text from...</span>
                </td>
            </tr>
        </table>
        <div class="ffoot">
            <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="easyuiInstance.saveField();">提 交</a>&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close');">取 消</a>
        </div>
    </form>
</div>
</body>
<script type="text/javascript">
    function setEasyUiWidth(width) {
        var parentWidth = $(".combotree").eq(0).parent().width();
        $(".combotree,.numberspinner").css("width", parentWidth * width);
    }
    setEasyUiWidth(0.85);
</script>
</html>