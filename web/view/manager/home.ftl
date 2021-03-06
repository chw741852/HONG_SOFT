<!DOCTYPE html>
<html>
<head>
    <title>Hong Soft</title>
    <link id="easyuiTheme" rel="stylesheet" type="text/css"
          href="${rc.contextPath}/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/ztree/css/demo.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/ztree/css/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/view/manager/js/home.js"></script>

    <script type="text/javascript">
        var scriptTools = new ScriptTools();
        $(function(){
            var themeName = $.cookie("themeName");
            scriptTools.setTheme($("#easyuiTheme"), "themes", themeName, "default", "easyui.css");
            if (themeName != undefined) {
                $("#theme option[value=" + themeName + "]").attr("selected", "true");
            }
        });
    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'north', split:true" style="height: 57px; padding: 0; width: 100%;">
    <#--<div style="width: 100%; height: 100%; background-image: url(${rc.contextPath}/images/main/title.jpg);
            background-repeat: repeat; background-position: left; text-align: right;">
        <div style="margin: auto;"><h1 STYLE="COLOR: #FFFFCC"> </h1></div>
    </div>-->
    <div style="float: left; margin: 13px 20px;">
        欢迎 ${(Session["USER"].name)!} 回来！<a href="${rc.contextPath}/logout">退出</a>
    </div>
    <div style="float: right; margin: 13px 20px;">
        皮肤管理
        <select id="theme" onchange="scriptInstance.changeTheme()">
            <option value="default">default</option>
            <option value="metro-gray">metro-gray</option>
            <option value="metro-green">metro-green</option>
            <option value="metro-orange">metro-orange</option>
            <option value="metro-red">metro-red</option>
            <option value="metro-blue">metro-blue</option>
        </select>
    </div>
</div>

<div data-options="region:'west', split:true, title:'导行面板'" style="width: 220px; padding: 0; overflow: hidden;">
    <div class="easyui-accordion" data-options="fit: true, border: false">
        <div title="功能模块" style="padding: 10px;" id="sysTree" class="ztree">

        </div>
    </div>
</div>

<div data-options="region:'center'" style="overflow: hidden;">
    <div id="tabs" class="easyui-tabs" data-options="fit:true, border:false">

    </div>
</div>

<#-- TODO 此处未用上，后期需要处理 -->
<div id="mm" class="easyui-menu" style="width: 150px; display: none">
    <div id="mm-tabclose">关闭标签</div>
    <div id="mm-tabcloseall">全部关闭</div>
    <div id="mm-tabcloseother">关闭其他标签</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright">关闭右侧标签</div>
    <div id="mm-tabcloseleft">关闭左侧标签</div>
    <div class="menu-sep"></div>
    <div id="mm-tabrefresh">刷新</div>
    <div id="mm-exit">退出</div>
</div>
<div id="treeRightMenu" class="easyui-menu" style="width: 150px; display: none;">
    <div id="rm-openTab">打开</div>
    <div id="rm-openNewTab">在新标签页打开</div>
    <div id="rm-insertBookMark">添加到收藏夹</div>
    <div id="rm-refreshMenuTree">刷新菜单</div>
</div>

<script type="text/javascript">
    var defaults = { contextPath:'${rc.contextPath}' };
    var scriptInstance = new ScriptUtil(defaults);
</script>
</body>
</html>