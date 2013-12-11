<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>测试Freemarker</title>
    <link type="text/css" rel="stylesheet" href="${request.contextPath}/css/front.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/metro/easyui.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/easyui/themes/icon.css">

    <script type="text/javascript" src="${request.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
</head>
<body>
<div class="nav"></div>
<div class="center">
    <h1>测试Freemarker</h1>
    <#list employee as e>
    <b>姓名：</b>${e.name}&nbsp;&nbsp;<b>年龄：</b>${e.age}
    <hr/>
    </#list>
</div>

<table class="easyui-datagrid" title="子节点列表" style="margin: 2px auto;" id="dg"
       data-options="singleSelect:true, collapsible:true, fitColumns:true, rownumbers:true,
                toolbar: '#tb', url:'${request.contextPath}/test/easyui', method:'get'">
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

</body>
</html>