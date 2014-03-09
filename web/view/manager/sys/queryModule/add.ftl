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
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>

    <script type="text/javascript" src="${rc.contextPath}/view/manager/sys/queryModule/queryModule.js"></script>

    <script type="text/javascript">
        var scriptTools = new ScriptTools();
        var scriptInstance = new QueryModuleScript();
        $(function(){
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptInstance.initValidate();
        });
    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;查询生成器 -> 新建查询模块
</div>
<div style="margin-left: 20px; margin-bottom: 10px;">

</div>

<form id="saveForm" class="form2" action="${rc.contextPath}/manager/sys/queryModule/save" method="post">
    <div id="p" class="easyui-panel" title="基础信息" style="padding:5px;">
        <table cellpadding="0" cellspacing="2">
            <tr>
                <td class="fieldtitle"><span class="bitian">* </span>模块名称：</td>
                <td class="fieldinput"><input id="name" name="name" class="inputtxt_notempty"></td>
                <td class="fieldtitle">导航位置描述：</td>
                <td class="fieldinput"><input id="pagePosition" name="pagePosition" class="inputtxt"></td>
                <td class="fieldtitle">导航图片路径：</td>
                <td class="fieldinput"><input id="headPicPath" name="headPicPath" class="inputtxt"></td>
            </tr>
            <tr><td style="height: 2px;"></td> </tr>
            <tr>
                <td class="fieldtitle">每页记录数：</td>
                <td class="fieldinput"><input id="pageSize" name="pageSize" class="inputtxt"></td>
                <td class="fieldtitle">双击行操作：</td>
                <td class="fieldinput">
                    <select name="dbClickRowLinkId" id="dbClickRowLinkId" class="selectfield">
                        <option value=""> </option>
                    </select>
                </td>
                <td class="fieldtitle">显示查询条件：</td>
                <td class="fieldinput"><input type="checkbox" name="showQueryCondition" checked></td>
            </tr>
            <tr><td style="height: 2px;"></td> </tr>
            <tr>
                <td class="fieldtitle">表之间关系SQL：</td>
                <td class="fieldinput" colspan="5">
                    <textarea cols="100" rows="4" name="relationSql"></textarea>
                </td>
            </tr>
        </table>
    </div>
    <div class="ffoot" style="float: none;text-align: center">
        <a href="javascript:void(0)" id="submitBtn" class="easyui-linkbutton" iconCls="icon-ok" onclick="$('#saveForm').submit()">提 交</a>&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="history.back()">返 回</a>
    </div>
</form>

</body>
</html>