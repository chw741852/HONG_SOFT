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
    <script type="text/javascript" src="${rc.contextPath}/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/validate/messages_zh.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>

    <script type="text/javascript">
        var contextPath = "${rc.contextPath}";
        var scriptTools = new ScriptTools();
        $(function(){
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;权限管理 -> 查看权限
</div>
<table cellpadding="0" cellspacing="0" class="lookupTable">
    <tr>
        <td class="fieldtitle">名 称</td>
        <td class="fieldinput">${(authority.name)!}</td>
        <td class="fieldtitle">是否启用</td>
        <td class="fieldinput">${(authority.enabled)?string("是", "否")}</td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td class="fieldtitle">备 注</td>
        <td class="fieldinput" colspan="5">${(authority.remark)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">创建人</td>
        <td class="fieldinput">${(authority.createdBy)!}</td>
        <td class="fieldtitle">创建日期</td>
        <td class="fieldinput">${(authority.createdDate)!}</td>
        <td class="fieldtitle">创建时间</td>
        <td class="fieldinput">${(authority.createdTime)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">修改人</td>
        <td class="fieldinput">${(authority.modifiedBy)!}</td>
        <td class="fieldtitle">修改日期</td>
        <td class="fieldinput">${(authority.modifiedDate)!}</td>
        <td class="fieldtitle">修改时间</td>
        <td class="fieldinput">${(authority.modifiedTime)!}</td>
    </tr>
</table>
<div class="newLine"></div>
<div style="width: 99%; margin-left: 6px;">
<table class="easyui-datagrid" title="资源列表" data-options="fitColumns:true, data:${resources!}">
    <thead>
    <tr>
        <th data-options="field:'name', width:$(this).width()*0.5">名 称</th>
        <th data-options="field:'url', width:$(this).width()*0.5">URL</th>
    </tr>
    </thead>
</table>
</div>
<div class="ffoot" style="float: none;text-align: center">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
</div>
</body>
</html>