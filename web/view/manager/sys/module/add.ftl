<!DOCTYPE html>
<html>
<head>
    <title>模块管理</title>

    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/form.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/metro/easyui.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/icon.css">

    <script type="text/javascript" src="${request.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/validate/messages_zh.js"></script>

    <script type="text/javascript" src="${request.contextPath}/view/manager/module/module.js"></script>

    <script type="text/javascript">
        $.validate.setDefaults({
            submitHandler: function() {
                alert("submitHandler");
                $("#savForm").submit();
            }
        });
        $(function(){
            $("#saveForm").validate();
        });
        function submit() {
            $("#saveForm").validate().submit();
        }
    </script>
</head>
<body>
<#escape x as x!>
<div class="ftitle">模块管理 - 新建</div>
<div>
    <form action="${request.ContextPath}/manager/module/save" name="saveForm" id="saveForm" method="post">
        <table cellpadding="0" cellspacing="2" style="width: 100%">
            <tr>
                <td class="fieldtitle">名称：</td>
                <td><input name="name" class="inputtxt required" required="required"></td>
                <td class="fieldtitle">父节点：</td>
                <td><input name="parentId" class="inputtxt"></td>
            </tr>
            <tr>
                <td class="fieldtitle">URL地址：</td>
                <td><input name="actionUrl" class="inputtxt"> </td>
                <td class="fieldtitle">权限等级：</td>
                <td>
                    <select name="authority" class="inputtxt">
                        <option value="1">需要验证</option>
                        <option value="2">基本权限</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">是否显示为菜单：</td>
                <td>
                    <input type="radio" name="menu" checked>是&nbsp;
                    <input type="radio" name="menu">否
                </td>
                <td class="fieldtitle">是否启用该模块：</td>
                <td>
                    <input type="radio" name="display" checked>是&nbsp;
                    <input type="radio" name="display">否
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">菜单所在层次：</td>
                <td>
                    <select name="level" class="inputtxt">
                        <option value="1">第一层</option>
                        <option value="2">第二层</option>
                        <option value="3">第三层</option>
                        <option value="4">第四层</option>
                        <option value="5">第五层</option>
                        <option value="6">第六层</option>
                    </select>
                </td>
            </tr>
        </table>
        <div class="ffoot">
            <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="submit();">提 交</a>&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-cancel">取 消</a>
        </div>
    </form>
</div>
</#escape>
</body>
</html>