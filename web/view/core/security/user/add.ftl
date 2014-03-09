<!DOCTYPE html>
<html>
<head>
    <title>查询生成器</title>
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
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initValidate();
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;用户管理 -> 新建用户
</div>

<form id="userForm" class="form2" action="${rc.contextPath}/core/security/user/save" method="post">
    <input type="hidden" name="roleIds" id="roleIds">
    <div id="p" class="easyui-panel" title="基础信息" style="padding:5px;">
        <table cellpadding="0" cellspacing="2">
            <tr>
                <td class="fieldtitle"><span class="bitian">* </span>用户名：</td>
                <td class="fieldinput"><input id="username" name="username" class="inputtxt_notempty"></td>
                <td class="fieldtitle"><span class="bitian">* </span>密 码：</td>
                <td class="fieldinput"><input type="password" id="password" name="password" class="inputtxt_notempty"></td>
                <td class="fieldtitle">重复密码：</td>
                <td class="fieldinput"><input type="password" id="confirmPassword" name="confirmPassword" class="inputtxt"></td>
            </tr>

            <tr>
                <td class="fieldtitle"><span class="bitian">* </span>真实姓名：</td>
                <td class="fieldinput"><input id="name" name="name" class="inputtxt_notempty"></td>
                <td class="fieldtitle">性 别：</td>
                <td class="fieldinput">
                    <select name="sex" id="sex" class="selectfield">
                        <#list sexList! as sex>
                            <option value="${sex.number}">${sex.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="fieldtitle">身份证：</td>
                <td class="fieldinput"><input name="idCard" class="inputtxt"></td>
            </tr>

            <tr>
                <td class="fieldtitle">出生日期：</td>
                <td class="fieldinput">
                    <input id="bornDate" name="bornDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="inputtxt">
                </td>
                <td class="fieldtitle">电 话：</td>
                <td class="fieldinput">
                    <input name="phone" class="inputtxt">
                </td>
                <td class="fieldtitle">手 机：</td>
                <td class="fieldinput"><input name="mobile" class="inputtxt"></td>
            </tr>

            <tr>
                <td class="fieldtitle">email：</td>
                <td class="fieldinput"><input name="email" class="inputtxt"></td>
                <td class="fieldtitle">QQ：</td>
                <td class="fieldinput"><input name="qq" class="inputtxt"></td>
                <td class="fieldtitle">MSN：</td>
                <td class="fieldinput"><input name="msn" class="inputtxt"></td>
            </tr>

            <tr>
                <td class="fieldtitle">用户类别：</td>
                <td class="fieldinput">
                    <select name="type" class="selectfield">
                        <#list typeList as instance>
                            <option value="${instance.number}">${instance.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="fieldtitle">开始使用日期：</td>
                <td class="fieldinput"><input name="beginTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="inputtxt"></td>
                <td class="fieldtitle">结束使用日期：</td>
                <td class="fieldinput"><input name="endTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="inputtxt"></td>
            </tr>
            <tr>
                <td class="fieldtitle">角 色：</td>
                <td colspan="5">
                <#list roles! as role>
                    <input type="checkbox" class="roleId" name="role_${role.id}" value="${role.id}">${role.name}&nbsp;&nbsp;
                    <#if (role_index+1)%10==0 && role_has_next>
                        <br>
                    </#if>
                </#list>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">备 注：</td>
                <td class="fieldinput" colspan="5">
                    <textarea cols="118" rows="2" name="remark"></textarea>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">创建人：</td>
                <td class="fieldinput">
                    <input name="createdBy" class="inputtxt" readonly>
                    <input type="hidden" name="createdById">
                </td>
                <td class="fieldtitle">创建日期：</td>
                <td class="fieldinput">
                    <input name="createdDate" class="inputtxt" value="${.now?date}" readonly>
                </td>
                <td class="fieldtitle">创建时间：</td>
                <td class="fieldinput">
                    <input name="createdTime" class="inputtxt" value="${.now?time}" readonly>
                </td>
            </tr>
        </table>

    </div>
    <div class="ffoot" style="float: none;text-align: center">
        <a href="javascript:void(0)" id="submitBtn" class="easyui-linkbutton" iconCls="icon-ok" onclick="$('#userForm').submit()">提 交</a>&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
    </div>
</form>

</body>
</html>