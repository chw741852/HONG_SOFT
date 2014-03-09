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

    <script type="text/javascript" src="${rc.contextPath}/view/core/security/role/role.js"></script>

    <script type="text/javascript">
        var contextPath = "${rc.contextPath}";
        var scriptTools = new ScriptTools();
        var scriptInstance = new RoleScript();
        $(function(){
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initValidate();
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;角色管理 -> 查看角色
</div>
<table cellpadding="0" cellspacing="0" class="lookupTable">
    <tr>
        <td class="fieldtitle">名 称</td>
        <td class="fieldinput">${(role.name)!}</td>
        <td class="fieldtitle">是否启用</td>
        <td class="fieldinput">${(role.enabled)?string("是", "否")}</td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td class="fieldtitle">权 限</td>
        <td class="fieldinput" colspan="5">
            <#list (role.authorities)?sort_by("id") as a>
                ${a.name}<#if a_has_next>，</#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="fieldtitle">备 注</td>
        <td class="fieldinput" colspan="5">${(role.remark)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">创建人</td>
        <td class="fieldinput">${(role.createdBy)!}</td>
        <td class="fieldtitle">创建日期</td>
        <td class="fieldinput">${(role.createdDate)!}</td>
        <td class="fieldtitle">创建时间</td>
        <td class="fieldinput">${(role.createdTime)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">修改人</td>
        <td class="fieldinput">${(role.modifiedBy)!}</td>
        <td class="fieldtitle">修改日期</td>
        <td class="fieldinput">${(role.modifiedDate)!}</td>
        <td class="fieldtitle">修改时间</td>
        <td class="fieldinput">${(role.modifiedTime)!}</td>
    </tr>
</table>
<div class="ffoot" style="float: none;text-align: center">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
</div>
</body>
</html>