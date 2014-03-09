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
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;用户管理 -> 修改用户
</div>

<form id="userForm" class="form2" action="${rc.contextPath}/core/security/user/update" method="post">
    <div id="p" class="easyui-panel" title="基础信息" style="padding:5px;">
        <input type="hidden" id="id" name="id" value="${(user.id)!}">
        <input type="hidden" name="version" value="${(user.version)!}">
        <input type="hidden" id="roleIds" name="roleIds">
        <table cellpadding="0" cellspacing="2">
            <tr>
                <td class="fieldtitle"><span class="bitian">* </span>用户名：</td>
                <td class="fieldinput"><input id="username" name="username" value="${(user.username)!}" class="inputtxt_notempty" readonly></td>
                <td class="fieldtitle"><span class="bitian">* </span>密 码：</td>
                <td class="fieldinput">
                    <input type="password" id="password" name="password" value="${(user.password)!}" class="inputtxt_notempty">
                </td>
                <td class="fieldtitle">重复密码：</td>
                <td class="fieldinput">
                    <input type="password" id="confirmPassword" name="confirmPassword" value="${(user.password)!}" class="inputtxt">
                </td>
            </tr>
            
            <tr>
                <td class="fieldtitle"><span class="bitian">* </span>真实姓名：</td>
                <td class="fieldinput"><input id="name" name="name" value="${(user.name)!}" class="inputtxt_notempty"></td>
                <td class="fieldtitle">性 别：</td>
                <td class="fieldinput">
                    <select name="sex" id="sex" class="selectfield">
                        <#list sexList! as sex>
                            <option value="${sex.number}"
                                <#if sex.number==(user.sex)!0>selected="selected" </#if>>
                            ${sex.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="fieldtitle">身份证：</td>
                <td class="fieldinput"><input name="idCard" class="inputtxt" value="${(user.idCard)!}"></td>
            </tr>
            
            <tr>
                <td class="fieldtitle">出生日期：</td>
                <td class="fieldinput">
                    <input id="bornDate" name="bornDate" value="${(user.bornDate)!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="inputtxt">
                </td>
                <td class="fieldtitle">电 话：</td>
                <td class="fieldinput">
                    <input name="phone" value="${(user.phone)!}" class="inputtxt">
                </td>
                <td class="fieldtitle">手 机：</td>
                <td class="fieldinput"><input name="mobile" value="${(user.mobile)!}" class="inputtxt"></td>
            </tr>
            
            <tr>
                <td class="fieldtitle">email：</td>
                <td class="fieldinput"><input name="email" value="${(user.email)!}" class="inputtxt"></td>
                <td class="fieldtitle">QQ：</td>
                <td class="fieldinput"><input name="qq" value="${(user.qq)!}" class="inputtxt"></td>
                <td class="fieldtitle">MSN：</td>
                <td class="fieldinput"><input name="msn" value="${(user.msn)!}" class="inputtxt"></td>
            </tr>
            
            <tr>
                <td class="fieldtitle">用户类别：</td>
                <td class="fieldinput">
                    <select name="type" class="selectfield">
                    <#list typeList as instance>
                        <option value="${instance.number}"
                                <#if instance.number==(user.type)!"">selected="selected"</#if>
                        >${instance.name}</option>
                    </#list>
                    </select>
                </td>
                <td class="fieldtitle">开始使用日期：</td>
                <td class="fieldinput"><input name="beginTime" value="${(user.beginTime)!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="inputtxt"></td>
                <td class="fieldtitle">结束使用日期：</td>
                <td class="fieldinput"><input name="endTime" value="${(user.endTime)!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="inputtxt"></td>
                <td colspan="2"></td>
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
                    <textarea cols="118" rows="2" name="remark">${(user.remark)!}</textarea>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">创建人：</td>
                <td class="fieldinput">
                    <input name="createdBy" value="${(user.createdBy)!}" class="inputtxt" readonly>
                    <input type="hidden" name="createdById" value="${(user.createdById)!}">
                </td>
                <td class="fieldtitle">创建日期：</td>
                <td class="fieldinput">
                    <input name="createdDate" class="inputtxt" value="${(user.createdDate)!}" readonly>
                </td>
                <td class="fieldtitle">创建时间：</td>
                <td class="fieldinput">
                    <input name="createdTime" class="inputtxt" value="${(user.createdTime)!}" readonly>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">修改人：</td>
                <td class="fieldinput">
                    <input name="modifiedBy" class="inputtxt" readonly>
                    <input type="hidden" name="modifiedById">
                </td>
                <td class="fieldtitle">修改日期：</td>
                <td class="fieldinput">
                    <input name="modifiedDate" class="inputtxt" value="${.now?date}" readonly>
                </td>
                <td class="fieldtitle">修改时间：</td>
                <td class="fieldinput">
                    <input name="modifiedTime" class="inputtxt" value="${.now?time}" readonly>
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
<script type="text/javascript">
    function initCheckbox() {
    <#list (user.roles)! as r>
        $(".roleId[name='role_${r.id}']").attr("checked", "true");
    </#list>
    }
    initCheckbox();
</script>
</html>