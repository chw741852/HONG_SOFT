<!DOCTYPE html>
<html>
<head>
    <title>字典表管理</title>
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/ztree/css/zTreeStyle/zTreeStyle.css">

    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/form.css">
    <link id="tableTheme" rel="stylesheet" type="text/css" href="${rc.contextPath}/css/themes/default/table.css">

    <script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/ztree/js/jquery.ztree.exedit-3.5.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>

    <script type="text/javascript" src="${rc.contextPath}/js/tools/Tools.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/view/manager/sys/dictionary/dictionary.js"></script>
    <script type="text/javascript">
        var scriptTools = new ScriptTools();
        var contextPath = "${rc.contextPath}";
        var scriptInstance = new ScriptUtil();
        var easyuiInstance = new EasyuiUtil();

        var setting = {
            view: {
                selectedMulti: false
            },
            edit: {
                enable: true,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeDrag: scriptInstance.beforeDrag,
                beforeDrop: scriptInstance.beforeDrop,
                onClick: scriptInstance.zTreeClick
            }
        };

        var zNodes = ${nodes!""}

        function setEasyUiWidth(width) {
            var parentWidth = $(".combotree").eq(0).parent().width();
            $(".combotree,.numberspinner").css("width", parentWidth * width);
        }

        setEasyUiWidth(0.85);

        $(function(){
            scriptTools.setTheme($("#easyuiTheme"), "themes", $.cookie("themeName"), "default", "easyui.css");
            scriptTools.setTheme($("#tableTheme"), "themes", $.cookie("themeName"), "default", "table.css");

            $.fn.zTree.init($("#dictTree"), setting, zNodes);
            $(window).resize(function() {
                $("#childDg").datagrid('resize');
            });
            $(".easyui-tabs").width($("#divWidth").width());
        });
    </script>
</head>
<body class="easyui-layout" data-options="fit: true">
<div data-options="region:'west', split:true, title:'字典表管理', tools:'#p-tools'" style="width: 220px; overflow: hidden;">
    <div style="padding: 10px;" class="ztree" id="dictTree">

    </div>
</div>

<div data-options="region:'center'" style="overflow: hidden;">
    <table cellpadding="0" cellspacing="0" class="table1">
        <thead>
        <tr>
            <td colspan="4">字典表管理</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="fieldtitle">序 号：</td>
            <td class="fieldinput"><div id="dictSequence"></div></td>
            <td class="fieldtitle">键：</td>
            <td class="fieldinput"><div id="dictKey"></div></td>
        </tr>
        <tr>
            <td class="fieldtitle">名 称：</td>
            <td class="fieldinput">
                <div id="dictName"></div>
            </td>
            <td class="fieldtitle">父节点：</td>
            <td class="fieldinput"><div id="parentNode"></div></td>
        </tr>
        </tbody>
    </table>
    <div style="width: 100%; text-align: center;">
        <input type="hidden" id="dictId" value="0" />
        <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="scriptInstance.edit();">修 改</a>&nbsp;&nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="scriptInstance.remove();">删 除</a>
    </div>
    <div style="width: 99%; margin: 4px 4px;" id="divWidth">
        <div class="easyui-tabs">
            <div title="键值对列表">

            </div>
            <div title="子节点列表">
                <table class="easyui-datagrid" title="" style="margin: 2px auto;" id="dg"
                       data-options="singleSelect:true, collapsible:true, fitColumns:true, rownumbers:true,
                    toolbar: '#tb'">
                    <thead>
                    <th data-options="field:'sequence', width:$(this).width()*0.33, editor: 'text'">序 号</th>
                    <th data-options="field:'key', width:$(this).width()*0.33, editor: 'text'">键</th>
                    <th data-options="field:'name', width:$(this).width()*0.33, editor: 'text'">名 称</th>
                    <th data-options="field:'id'"></th>
                    </thead>
                </table>
            </div>
        </div>
    </div>

    <div id="dlg" class="easyui-dialog" style="width: 900px;height:500px;padding:10px;"
         data-options="iconCls:'icon-save', collapsible:true, minimizable:true, maximizable:true,
         resizable:true, closable:true, closed:true">
        <form id="dictForm" method="post" class="form1">
            <input type="hidden" name="id" id="id"/>
            <input type="hidden" name="version" id="version"/>
            <table cellpadding="0" cellspacing="2" style="width: 100%">
                <tr>
                    <td class="fieldtitle">序号：</td>
                    <td class="fieldinput">
                        <input name="sequence" id="sequence" class="numberspinner easyui-numberspinner"
                               data-options="required:true, min:0, missingMessage:'该字段不能为空！'">
                    </td>
                    <td class="fieldtitle">键：</td>
                    <td class="fieldinput">
                        <input name="key" id="key" class="inputtxt easyui-validatebox"
                               data-options="required:true,missingMessage:'该字段不能为空！'">
                    </td>
                </tr>
                <tr>
                    <td class="fieldtitle">名称：</td>
                    <td class="fieldinput">
                        <input name="name" id="name" class="inputtxt easyui-validatebox"
                               data-options="required:true,missingMessage:'该字段不能为空！'">
                    </td>
                    <td class="fieldtitle">父节点：</td>
                    <td class="fieldinput">
                        <input name="parentId" class="combotree easyui-combotree" id="parentId"
                               data-options="url: '${rc.contextPath}/manager/sys/module/ajaxFindModule?id=0',
                            method:'get', required:false, multiple:false">
                        <a href="javascript:void(0);" onclick="$('#parentId').combotree('setValue','');"
                           style="text-decoration: none;">清空</a>
                    </td>
                </tr>
            </table>
            <div class="ffoot">
                <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="scriptInstance.saveModule();">提 交</a>&nbsp;&nbsp;
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close');">取 消</a>
            </div>
        </form>
    </div>
</div>

<div id="p-tools" style="padding: 5px;">
    <a href="javascript:void(0)" title="添加" class="icon-add" onclick="scriptInstance.add();"></a>
    <a href="javascript:void(0)" title="修改" class="icon-edit" onclick="scriptInstance.edit();"></a>
    <a href="javascript:void(0)" title="删除" class="icon-remove" onclick="scriptInstance.remove();"></a>
</div>
<div id="tb" style="height: auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="easyuiInstance.append()">添 加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="easyuiInstance.edit()">修 改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="easyuiInstance.remove()">删 除</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="easyuiInstance.save()">保 存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="easyuiInstance.reject()">取 消</a>
</div>
</body>
</html>