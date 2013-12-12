<!DOCTYPE html>
<html>
<head>
    <title>Hong Soft</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/metro/easyui.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/ztree/css/demo.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/ztree/css/zTreeStyle/zTreeStyle.css">

    <script type="text/javascript" src="${request.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/ztree/js/jquery.ztree.core-3.5.min.js"></script>

    <script type="text/javascript" src="${request.contextPath}/view/manager/js/home.js"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'north', split:true" style="height: 57px; padding: 0; width: 100%; background: transparent">
    <div style="width: 100%; height: 100%; background-image: url(${request.contextPath}/images/main/title.jpg);
            background-repeat: repeat; background-position: left; text-align: right;">
        <div style="margin: auto;"><h1 STYLE="COLOR: #FFFFCC"> </h1></div>
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
    var defaults = { contextPath:'${request.contextPath}' };
    var scriptInstance = new ScriptUtil(defaults);
</script>
</body>
</html>