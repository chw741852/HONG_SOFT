<!DOCTYPE html>
<html>
<head>
    <title>查询生成器</title>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/form.css">
    <link id="tableTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/css/themes/default/table.css">
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/ztree/css/zTreeStyle/zTreeStyle.css">

    <script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>

    <script type="text/javascript" src="${rc.contextPath}/view/core/security/authority/authority.js"></script>

    <script type="text/javascript">
        var contextPath = "${rc.contextPath}";
        var zNodes = ${zNodes};
        var scriptTools = new ScriptTools();
        var scriptInstance = new AuthorityScript();
        $(function(){
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initResourceTree(zNodes);
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png" />&nbsp;权限管理 -> 配置资源
</div>
<form id="authorityForm" action="${rc.contextPath}/core/security/authority/saveResource" method="post">
    <input type="hidden" name="id" value="${authority.id}"/>
    <input id="resourceIds" name="resourceIds" type="hidden"/>
    <table cellpadding="0" cellspacing="0" class="lookupTable" width="200">
        <tr>
            <td class="fieldtitle" style="width: 3%;">名 称</td>
            <td class="fieldinput" style="width: 97%;">${authority.name}</td>
        </tr>
        <tr>
            <td class="fieldtitle">分配资源</td>
            <td class="fieldinput">
                <div style="margin: 5px 0 7px;">
                    <ul id="resourceTree" class="ztree ztreeUl"></ul>
                </div>
            </td>
        </tr>
    </table>
    <div class="ffoot" style="float: none;text-align: center">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="scriptInstance.subForm()">提 交</a>
        &nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
    </div>
</form>

</body>
</html>