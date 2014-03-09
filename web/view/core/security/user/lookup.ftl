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

    <script type="text/javascript" src="${rc.contextPath}/view/core/security/user/user.js"></script>

    <script type="text/javascript">
        var contextPath = "${rc.contextPath}";
        var scriptTools = new ScriptTools();
        var scriptInstance = new UserScript();
        $(function(){
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initValidate();
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;用户管理 -> 查看用户
</div>
<table cellpadding="0" cellspacing="0" class="lookupTable">
    <tr>
        <td class="fieldtitle">用户名</td>
        <td class="fieldinput">${(user.username)!}</td>
        <td class="fieldtitle">开始使用日期</td>
        <td class="fieldinput">${(user.beginTime)!}</td>
        <td class="fieldtitle">结束使用日期</td>
        <td class="fieldinput">${(user.endTime)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">真实姓名</td>
        <td class="fieldinput">${(user.name)!}</td>
        <td class="fieldtitle">性 别</td>
        <td class="fieldinput">
            <#list sexList! as sex>
                <#if sex.number==(user.sex)!0>${sex.name}</#if>
            </#list>
        </td>
        <td class="fieldtitle">身份证</td>
        <td class="fieldinput">${(user.idCard)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">出生日期</td>
        <td class="fieldinput">
            ${(user.bornDate)!}
        </td>
        <td class="fieldtitle">电 话</td>
        <td class="fieldinput">
            ${(user.phone)!}
        </td>
        <td class="fieldtitle">手 机</td>
        <td class="fieldinput">${(user.mobile)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">email</td>
        <td class="fieldinput">${(user.email)!}</td>
        <td class="fieldtitle">QQ</td>
        <td class="fieldinput">${(user.qq)!}</td>
        <td class="fieldtitle">MSN</td>
        <td class="fieldinput">${(user.msn)!}</td>
    </tr>
    <tr>
        <td class="fieldtitle">用户类别</td>
        <td class="fieldinput">
        <#list typeList! as instance>
            <#if instance.number==(user.type)!"">${instance.name}</#if>
        </#list>
        </td>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td class="fieldtitle">备 注</td>
        <td class="fieldinput" colspan="5">
            ${(user.remark)!}
        </td>
    </tr>
    <tr>
        <td class="fieldtitle">创建人</td>
        <td class="fieldinput">
            ${(user.createdBy)!}
        </td>
        <td class="fieldtitle">创建日期</td>
        <td class="fieldinput">
            ${(user.createdDate)!}
        </td>
        <td class="fieldtitle">创建时间</td>
        <td class="fieldinput">
            ${(user.createdTime)!}
        </td>
    </tr>
    <tr>
        <td class="fieldtitle">修改人</td>
        <td class="fieldinput">
            ${(user.modifiedBy)!}
        </td>
        <td class="fieldtitle">修改日期</td>
        <td class="fieldinput">
            ${(user.modifiedDate)!}
        </td>
        <td class="fieldtitle">修改时间</td>
        <td class="fieldinput">
            ${(user.modifiedTime)!}
        </td>
    </tr>
</table>
<div class="ffoot" style="float: none;text-align: center">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
</div>
</body>
</html>