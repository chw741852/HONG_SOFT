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
    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>

    <script type="text/javascript" src="${rc.contextPath}/view/manager/sys/queryModule/queryModule.js"></script>

    <script type="text/javascript">
        var scriptTools = new ScriptTools();
        var contextPath = "${rc.contextPath}";
        var linkEasyui = new linkEasyuiScript();
        $(function(){
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");
        });

    </script>
</head>

<body>
<div class="ftitle">
    <img src="${rc.contextPath}/images/diy/8.png"/>&nbsp;查询生成器 -> 查询列表链接
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <img src="${rc.contextPath}/images/diy/2.png"/>
    <a href="${rc.contextPath}/manager/sys/queryModule/edit?id=${queryModule.id}">查询模块</a>&nbsp;&nbsp;&nbsp;
    <img src="${rc.contextPath}/images/diy/2.png"/>
    <a href="${rc.contextPath}/manager/sys/field/browse?queryModuleId=${queryModule.id}">查询字段清单</a>
</div>
<table cellpadding="0" cellspacing="0" class="table1" style="margin: 0; width: 100%">
    <thead>
    <tr>
        <td colspan="4">查询模块信息</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="fieldtitle">id号：</td>
        <td class="fieldinput"><div id="moduleId">${queryModule.id}</div></td>
        <td class="fieldtitle">名 称：</td>
        <td class="fieldinput"><div id="moduleName">${queryModule.name}</div></td>
    </tr>
    <tr>
        <td class="fieldtitle">表之间关系SQL语句：</td>
        <td class="fieldinput" colspan="3">
            <div id="relationSql">${queryModule.relationSql}</div>
        </td>
    </tr>
    </tbody>
</table>
<br/>
<table class="easyui-datagrid" title="链接列表" style="margin: 2px auto;" id="dg"
       data-options="singleSelect:true, fitColumns:true, rownumbers:true, toolbar: '#tb',
        url:'${rc.contextPath}/manager/sys/listLink/queryListLink?queryModuleId=${queryModule.id}', method:'get',
        onLoadSuccess:function(data) {
            $('#dg').datagrid('hideColumn', 'id');
        }">
    <thead>
    <th data-options="field:'name', width:$(this).width()*0.25">名 称</th>
    <th data-options="field:'url', width:$(this).width()*0.25">链 接</th>
    <th data-options="field:'sequence', width:$(this).width()*0.25">排 序</th>
    <th data-options="field:'display', width:$(this).width()*0.25">显示</th>
    <th data-options="field:'id'"></th>
    </thead>
</table>

<div id="tb" style="height: auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="linkEasyui.addLink()">添 加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="linkEasyui.editLink()">修 改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="linkEasyui.removeLink()">删 除</a>
</div>

<div id="dlg" class="easyui-dialog" style="width: 900px;height:500px;padding:10px;"
     data-options="iconCls:'icon-save', collapsible:true, minimizable:true, maximizable:true,
         resizable:true, closable:true, closed:true">
    <form id="linkForm" method="post" class="form1">
        <input type="hidden" name="id" id="id"/>
        <input type="hidden" name="version" id="version"/>
        <table cellpadding="0" cellspacing="2" style="width: 100%">
            <tr>
                <td class="fieldtitle">名 称：</td>
                <td class="fieldinput">
                    <input name="name" class="inputtxt easyui-validatebox" data-options="required:true">
                </td>
                <td class="fieldtitle">超链接：</td>
                <td class="fieldinput">
                    <input name="url" class="inputtxt">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">排 序：</td>
                <td class="fieldinput">
                    <input name="sequence" id="sequence" class="inputtxt easyui-numberbox" value="0"
                           data-options="required:false, min:0">
                </td>
                <td class="fieldtitle">操作类型：</td>
                <td class="fieldinput">
                    <select name="linkType" class="selectfield">
                        <option value="1">新建</option>
                        <option value="2">修改</option>
                        <option value="3">删除</option>
                        <option value="4">查看</option>
                        <option value="5">打印</option>
                        <option value="6">提交</option>
                        <option value="7">审核</option>
                        <option value="8">弃交</option>
                        <option value="9">反审核</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">是否显示：</td>
                <td class="fieldinput">
                    <select name="display" id="display" class="selectfield">
                        <#list yesOrNo! as instance>
                            <option value="${instance.number}">${instance.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="fieldtitle">选择记录状态：</td>
                <td class="fieldinput">
                    <select name="opType" class="selectfield">
                        <option value="1">操作单条记录</option>
                        <option value="2">操作多条记录</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">替换值：</td>
                <td class="fieldinput">
                    <input name="replacementValue" id="replacementValue" class="inputtxt">
                </td>
                <td class="fieldtitle">需要验证：</td>
                <td class="fieldinput">
                    <select name="verify" id="verify" class="selectfield">
                    <#list yesOrNo! as instance>
                        <option value="${instance.number}">${instance.name}</option>
                    </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">验证表达式：</td>
                <td class="fieldinput" colspan="3">
                    <input name="verifyCondition" class="inputtxt">
                </td>
            </tr>
            <tr>
                <td class="fieldtitle">备注：</td>
                <td class="fieldinput" colspan="3">
                    <textarea cols="88" rows="4" name="remark" id="remark"></textarea>
                </td>
            </tr>
        </table>
        <div class="ffoot">
            <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="linkEasyui.saveLink();">提 交</a>&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close');">取 消</a>
        </div>
    </form>
</div>
</body>
<script type="text/javascript">
    function setEasyUiWidth(width) {
        var parentWidth = $(".combotree").eq(0).parent().width();
        $(".combotree,.numberspinner").css("width", parentWidth * width);
    }
    setEasyUiWidth(0.85);
</script>
</html>