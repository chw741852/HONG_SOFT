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

    <script type="text/javascript" src="${rc.contextPath}/view/core/security/role/role.js"></script>

    <script type="text/javascript">
        var contextPath = "${rc.contextPath}";
        var scriptTools = new ScriptTools();
        var scriptInstance = new RoleScript();
        $(function(){
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initValidate();
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;角色管理 -> 修改角色
</div>

<form id="roleForm" class="form2" action="${rc.contextPath}/core/security/role/update" method="post">
    <div id="p" class="easyui-panel" title="基础信息" style="padding:5px;">
        <input type="hidden" name="id" value="${(role.id)!}">
        <input type="hidden" name="version" value="${(role.version)!}">
        <input type="hidden" name="authorityIds" id="authorityIds">
        <table cellpadding="0" cellspacing="2">
            <tr>
                <td class="fieldtitle"><span class="bitian">* </span>名 称：</td>
                <td class="fieldinput"><input id="name" name="name" value="${(role.name)!}" class="inputtxt_notempty"></td>
                <td class="fieldtitle">是否启用：</td>
                <td class="fieldinput">
                    <select name="enabled" class="selectfield">
                        <#list yesOrNo! as instance>
                            <option value="${instance.number}"
                                <#if instance.number==role.enabled?string("1","0")>selected="selected"</#if>>
                            ${instance.name}</option>
                        </#list>
                    </select>
                </td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td class="fieldtitle">权 限：</td>
                <td colspan="5">
                <#list authorities as a>
                    <input type="checkbox" class="authorityId" name="authority_${a.id}" value="${a.id}">${a.name}&nbsp;&nbsp;
                    <#if (a_index+1)%10==0 && a_has_next>
                        <br>
                    </#if>
                </#list>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">备 注：</td>
                <td class="fieldinput" colspan="5">
                    <textarea cols="118" rows="2" name="remark">${(role.remark)!}</textarea>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">创建人：</td>
                <td class="fieldinput">
                    <input name="createdBy" value="${(role.createdBy)!}" class="inputtxt" readonly>
                    <input type="hidden" name="createdById" value="${(role.createdById)!}">
                </td>
                <td class="fieldtitle">创建日期：</td>
                <td class="fieldinput">
                    <input name="createdDate" class="inputtxt" value="${(role.createdDate)!}" readonly>
                </td>
                <td class="fieldtitle">创建时间：</td>
                <td class="fieldinput">
                    <input name="createdTime" class="inputtxt" value="${(role.createdTime)!}" readonly>
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
        <a href="javascript:void(0)" id="submitBtn" class="easyui-linkbutton" iconCls="icon-ok" onclick="$('#roleForm').submit()">提 交</a>&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
    </div>
</form>
</body>
<script type="text/javascript">
    function initCheckbox() {
    <#list (role.authorities)! as a>
        $(".authorityId[name='authority_${a.id}']").attr("checked", "true");
    </#list>
    }
    initCheckbox();
</script>
</html>