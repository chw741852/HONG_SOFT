<!DOCTYPE html>
<html>
<head>
    <title>HONG_SOFT</title>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/form.css">
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/icon.css">

    <script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/view/manager/web/publicListInfo.js"></script>

    <script type="text/javascript">
        var contextPath = "${rc.contextPath}";
        var hideFieldStr = "${hideFieldStr}";
        var dbClickRowLinkId = "${dbClickRowLinkId!}";
        var scriptTools = new ScriptTools();
        var scriptInstance = new PublicListInfoScript();
        $(function(){
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initPagenationBtn();
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/1_open.png"/>
    <span style="font-size: 16px; font-weight: bolder">${nav1}</span><img src="${rc.contextPath}/images/diy/basexiao.gif"/><span>${nav2}</span>
</div>

<div id="p" class="easyui-panel" title=" 查询条件" style="padding:5px;" data-options="collapsible:true">
    <form id="searchForm" class="form2">
        ${queryHtml!}
    </form>
    <div class="ffoot" style="float: none;text-align: center;margin-top: 5px">
        <a href="javascript:void(0)" id="searchBtn" class="easyui-linkbutton" iconCls="icon-search" onclick="scriptInstance.searchListInfo()">查 询</a>&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" onclick="$('#searchForm').form('reset')">重 置</a>
    </div>
</div>
<br/>
${datagrid}
</body>
</html>