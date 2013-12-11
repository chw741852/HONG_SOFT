<!DOCTYPE html>
<html>
<head>
    <title>模块管理</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/metro/easyui.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/ztree/css/zTreeStyle/zTreeStyle.css">

    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/form.css">

    <script type="text/javascript" src="${request.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/ztree/js/jquery.ztree.exedit-3.5.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>

    <script type="text/javascript" src="${request.contextPath}/view/manager/module/module.js"></script>

    <style>
        .ztree li span.button.add {
            margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top;
            *vertical-align:middle
        }
    </style>

</head>
<body class="easyui-layout">

<div data-options="region:'west', split:true, title:'模块管理', tools:'#p-tools'" style="width: 220px; overflow: hidden;">
    <div style="padding: 10px;" class="ztree" id="moduleTree">

    </div>
</div>

<div data-options="region:'center'" style="overflow: hidden;">
    <table cellpadding="0" cellspacing="0" class="table1">
        <thead>
        <tr>
            <td colspan="4">查看模块管理</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="fieldtitle">序 号：</td>
            <td class="fieldinput"><div id="moduleSequence"></div></td>
            <td class="fieldtitle">名 称：</td>
            <td class="fieldinput"><div id="moduleName"></div></td>
        </tr>
        <tr>
            <td class="fieldtitle">父节点：</td>
            <td class="fieldinput">
                <div id="parentNode"></div>
            </td>
            <td class="fieldtitle">URL地址：</td>
            <td class="fieldinput"><div id="moduleActionUrl"></div></td>
        </tr>
        <tr>
            <td class="fieldtitle">权限等级：</td>
            <td class="fieldinput">
                <div id="moduleAuthority"></div>
            </td>
            <td class="fieldtitle">是否菜单：</td>
            <td class="fieldinput">
                <div id="moduleMenu"></div>
            </td>
        </tr>
        <tr>
            <td class="fieldtitle">是否启用：</td>
            <td class="fieldinput">
                <div id="moduleDisplay"></div>
            </td>
            <td class="fieldtitle">是否系统模块：</td>
            <td class="fieldinput">
                <div id="moduleSys"></div>
            </td>
        </tr>
        </tbody>
    </table>
    <div style="width: 100%; text-align: center;">
        <input type="hidden" id="moduleId" value="0" />
        <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="scriptInstance.edit();">修 改</a>&nbsp;&nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="scriptInstance.remove();">删 除</a>
    </div>
    <div style="width: 99%; margin: 4px 4px;">
        <table class="easyui-datagrid" title="子节点列表" style="margin: 2px auto;" id="dg"
               data-options="singleSelect:true, collapsible:true, fitColumns:true, rownumbers:true,
                toolbar: '#tb', onLoadSuccess:easyuiInstance.onLoadSuccess,
                url:'${request.contextPath}/manager/module/loadChildNode', method:'get'">
            <thead>
            <th data-options="field:'sequence', width:$(this).width()*0.2, editor: 'text'">序 号</th>
            <th data-options="field:'name', width:$(this).width()*0.2, editor: 'text'">名 称</th>
            <th data-options="field:'actionUrl', width:$(this).width()*0.2, editor: 'text'">URL地址</th>
            <th data-options="field:'authority', width:$(this).width()*0.2,
                formatter:function(value, row) {
                    return row.authorityText;
                },
                editor: {
                    type: 'combobox',
                    options: {
                        valueField: 'value',
                        textField: 'label',
                        data:[{
                            label: '需要验证',
                            value: 1
                        },{
                            label: '基本权限',
                            value: 2
                        }]
                    }
                }">权限等级</th>
            <th data-options="field:'isMenu', width:$(this).width()*0.2">是否菜单</th>
            <th data-options="field:'isDisplay', width:$(this).width()*0.2">是否启用</th>
            <th data-options="field:'isSys', width:$(this).width()*0.2">是否系统模块</th>
            <th data-options="field:'id'"></th>
            </thead>
        </table>
    </div>

    <div id="dlg" class="easyui-dialog" style="width: 900px;height:500px;padding:10px;"
         data-options="iconCls:'icon-save', collapsible:true, minimizable:true, maximizable:true,
         resizable:true, closable:true, closed:true">
        <form id="moduleForm" method="post">
            <input type="hidden" name="id" id="id"/>
            <table cellpadding="0" cellspacing="2" style="width: 100%">
                <tr>
                    <td class="fieldtitle">序号：</td>
                    <td class="fieldinput">
                        <input name="sequence" id="sequence" class="numberspinner easyui-numberspinner"
                               data-options="required:true, min:0, missingMessage:'该字段不能为空！'">
                    </td>
                    <td class="fieldtitle">名称：</td>
                    <td class="fieldinput">
                        <input name="name" id="name" class="inputtxt easyui-validatebox"
                               data-options="required:true,missingMessage:'该字段不能为空！'">
                    </td>
                </tr>
                <tr>
                    <td class="fieldtitle">父节点：</td>
                    <td class="fieldinput">
                        <input name="parentId" class="combotree easyui-combotree" id="parentId"
                               data-options="url: '${request.contextPath}/manager/module/ajaxFindModule?id=0',
                            method:'get', required:false, multiple:false">
                        <a href="javascript:void(0);" onclick="$('#parentId').combotree('setValue','');"
                           style="text-decoration: none;">清空</a>
                    </td>
                    <td class="fieldtitle">URL地址：</td>
                    <td class="fieldinput"><input name="actionUrl" id="actionUrl" class="inputtxt"> </td>
                </tr>
                <tr>
                    <td class="fieldtitle">权限等级：</td>
                    <td class="fieldinput">
                        <select name="authority" class="selectfield" id="authority">
                            <option value="1">需要验证</option>
                            <option value="2">基本权限</option>
                        </select>
                    </td>
                    <td class="fieldtitle">是否菜单：</td>
                    <td class="fieldinput">
                        <input type="radio" name="menu" value="1" checked>是&nbsp;
                        <input type="radio" name="menu" value="0">否
                    </td>
                </tr>
                <tr>
                    <td class="fieldtitle">是否启用：</td>
                    <td class="fieldinput">
                        <input type="radio" name="display" value="1" checked>是&nbsp;
                        <input type="radio" name="display" value="0">否
                    </td>
                    <td class="fieldtitle">是否系统菜单：</td>
                    <td class="fieldinput">
                        <input type="radio" name="sys" value="1" checked>是&nbsp;
                        <input type="radio" name="sys" value="0">否
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

<script type="text/javascript">
    var defaults = { contextPath:'${request.contextPath}' };
    var scriptInstance = new ScriptUtil(defaults);
    var easyuiInstance = new EasyuiUtil(defaults);

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
            onClick: scriptInstance.zTreeClick
        }
    };

    var zNodes = ${nodes};

    function setEasyUiWidth(width) {
        var parentWidth = $(".combotree").eq(0).parent().width();
        $(".combotree,.numberspinner").css("width", parentWidth * width);
    }
    setEasyUiWidth(0.85);

    $(function(){
        $.fn.zTree.init($("#moduleTree"), setting, zNodes);
        $(window).resize(function() {
            $("#dg").datagrid('resize');
        })
    });
</script>

</body>
</html>