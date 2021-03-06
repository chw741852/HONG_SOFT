/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-25
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
function ScriptUtil(defaults) {
    var settings = { contextPath:'' }
    if (defaults) {
        $.extend(settings, defaults);
    }
    this.defaults = settings;
    var url = "";
    var flag = "";
}

ScriptUtil.prototype = {
    beforeDrag:function(treeId, treeNodes) {
        return true;
    },
    beforeDrop:function(treeId, treeNodes, targetNode, moveType) {
        return true;
    },
    add:function() {
        $('#moduleForm')[0].reset();
        $('#id').val('');
        $('#parentId').combotree('reload', this.defaults.contextPath + "/manager/sys/module/ajaxFindModule?id=0");
        var zTree = $.fn.zTree.getZTreeObj("moduleTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length > 0) {
            $('#parentId').combotree('setValue', treeNode.id);
        }
        $('#dlg').dialog('open').dialog('setTitle', '新建模块');
        this.url = this.defaults.contextPath + "/manager/sys/module/saveOrUpdate";
        this.flag = "add";
    },
    edit:function() {
        var zTree = $.fn.zTree.getZTreeObj("moduleTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length > 0) {
            var _this = this;

            $.ajax({
                type: "Get",
                url: _this.defaults.contextPath + "/manager/sys/module/edit",
                data: "id=" + treeNode.id,
                dataType: "json",
                success: function(result) {
                    if (result.id > 0) {
                        $('#moduleForm')[0].reset();
                        $('#id').val(result.id);
                        $('#version').val(result.version);
                        $('#sequence').numberspinner('setValue', result.sequence);
                        $('#name').val(result.name);
                        if (result.parent == null) {
                            $('#parentId').combotree('setValue', null);
                        } else {
                            $('#parentId').combotree('setValue', result.parent.id);
                        }
                        $('#actionUrl').val(result.actionUrl);
                        $('#authority').val(result.authority);
                        if (result.menu == false)
                            $("input[name='menu'][value='0']").attr("checked", true);
                        if (result.display == false)
                            $("input[name='display'][value='0']").attr("checked", true);
                        if (result.sys == false)
                            $("input[name='sys'][value='0']").attr("checked", true);
                        $('#parentId').combotree('reload', _this.defaults.contextPath + "/manager/sys/module/ajaxFindModule?id=" + result.id);
                        $('#dlg').dialog('open').dialog('setTitle','修改模块');
                        _this.url = _this.defaults.contextPath + "/manager/sys/module/saveOrUpdate";
                        _this.flag = "edit";
                    }
                }
            });
        } else {
            this.showMsg("请选择一个节点");
        }
    },

    remove:function() {
        var _this = this;
        var zTree = $.fn.zTree.getZTreeObj("moduleTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length == 0) {
            this.showMsg("请选择一个节点");
            return;
        }
        $.messager.confirm('Confirm', '确认删除 节点 -- ' + treeNode.name + ' 以及其子节点吗？', function(r){
            if (r){
                $.ajax({
                    type: "Get",
                    url: _this.defaults.contextPath + "/manager/sys/module/delete",
                    data: "id=" + treeNode.id,
                    success: function(result) {
                        if (result == "true") {
                            if ($('#moduleId').val() == treeNode.id) {
                                $('#moduleId').val('');
                                $('#moduleSequence').html('');
                                $('#moduleName').html('');
                                $('#parentNode').html('');
                                $('#moduleActionUrl').html('');
                                $('#moduleAuthority').html('');
                                $('#moduleMenu').html('');
                                $('#moduleDisplay').html('');
                                $('#dg').datagrid('reload', _this.defaults.contextPath + "/manager/sys/module/loadChildNode?id=0");
                            }
                            zTree.removeNode(treeNode, false);  //  false - 不促发回调函数
                        } else {
                            _this.showMsg("删除失败！");
                        }
                    }
                });
            }
        });
    },
    saveModule:function() {
        var _this = this;
        $('#moduleForm').form('submit',{
            url: _this.url,
            onSubmit: function() {
                return $(this).form('validate');
            },
            success: function(result) {
                var result = eval('(' + result + ')');
                $('#dlg').dialog('close');

                if (result.bean.id > 0) {
                    var zTree = $.fn.zTree.getZTreeObj("moduleTree"),
                        nodes = zTree.getNodesByParam('id', null);
                    if (result.bean.parent != null) {
                        nodes = zTree.getNodesByParam('id', result.bean.parent.id)
                    }
                    var treeNode = nodes[0];
                    if (_this.flag == "add") {
                        if (treeNode) {
                            zTree.addNodes(treeNode, {id:result.bean.id, pId:treeNode.id, name:result.bean.name});
                        } else {
                            zTree.addNodes(null, {id:result.bean.id, pId:0, name:result.bean.name});
                        }
                    } else {
                        nodes = zTree.getNodesByParam('id', result.bean.id);
                        var sourceNode = nodes[0];
                        zTree.editName(sourceNode);
                        zTree.cancelEditName(result.bean.name);
                        if (treeNode) {
                            zTree.moveNode(treeNode, sourceNode, 'inner');
                        } else {
                            zTree.moveNode(null, sourceNode, 'inner');
                        }
                        $('#moduleId').val(result.bean.id);
                        $('#moduleSequence').html(result.bean.sequence);
                        $('#moduleName').html(result.bean.name);
                        $('#parentNode').html(result.bean.parent.name);
                        $('#moduleActionUrl').html(result.bean.actionUrl);
                        $('#moduleAuthority').html(result.bean.authority);
                        if (result.bean.menu == false) {
                            $('#moduleMenu').html("否");
                        } else {
                            $('#moduleMenu').html("是");
                        }
                        if (result.bean.display == false) {
                            $('#moduleDisplay').html("否");
                        } else {
                            $('#moduleDisplay').html("是");
                        }
                        if (result.bean.sys == false) {
                            $('#moduleSys').html("否");
                        } else {
                            $('#moduleSys').html("是");
                        }

                        $('#dg').datagrid('load', {id:result.id});
                    }
                }

                $('#parentId').combotree('reload');
                _this.showMsg(result.message);
            }
        });
    },
    zTreeClick:function(event, treeId, treeNode) {
        $.ajax({
            type: "Get",
            url: scriptInstance.defaults.contextPath + "/manager/sys/module/ajaxView",
            data: "id=" + treeNode.id,
            dataType: "json",
            success: function(data) {
                var result = data[0];
                $('#moduleId').val(result.id);
                $('#moduleSequence').html(result.sequence);
                $('#moduleName').html(result.name);
                $('#parentNode').html(result.parentNode);
                $('#moduleActionUrl').html(result.actionUrl);
                $('#moduleAuthority').html(result.authority);
                $('#moduleMenu').html(result.menu);
                $('#moduleDisplay').html(result.display);
                $('#moduleSys').html(result.sys);

                $('#dg').datagrid('load', {id:result.id});
            }
        });
    },
    showMsg:function(msg) {
        $.messager.show({
            title:'提示',
            msg:msg,
            showType:'show',
            timeout:2500,
            style:{
                left:'',
                right:0,
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        });
    }
}

function EasyuiUtil(defaults) {
    var settings = { contextPath:'' };
    var editIndex = undefined;
    if (defaults) {
        $.extend(settings, defaults);
    }
    this.defaults = settings;
}

EasyuiUtil.prototype = {
    onLoadSuccess: function(data) {
        $('#dg').datagrid('hideColumn', 'id');
    },
    endEditing: function() {
        if (this.editIndex == undefined) {
            return true;
        }
        if ($('#dg').datagrid('validateRow', this.editIndex)){
            var ed = $('#dg').datagrid('getEditor', {index:this.editIndex,field:'authority'});
            var authorityText = $(ed.target).combobox('getText');
            $('#dg').datagrid('getRows')[this.editIndex]['authorityText'] = authorityText;
            $('#dg').datagrid('endEdit', this.editIndex);
            // TODO ajax后台持久化，待写
            this.editIndex = undefined;
            return true;
        } else {
            return false;
        }
    },
    append: function() {

    },
    remove:function() {

    },
    edit: function() {
        var row = $('#dg').datagrid('getSelected');
        if (row != undefined) {
            var index = $('#dg').datagrid('getRowIndex', row);
            if (this.editIndex != index) {
                if (this.endEditing()) {
                    $('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
                    this.editIndex = index;
                } else {
                    $('#dg').datagrid('selectRow', this.editIndex);
                }
            }
        } else {
            scriptInstance.showMsg("请选择一条记录");
        }
    },
    save:function() {
        if (this.endEditing()) {
            $('#dg').datagrid('acceptChanges');
        }
    },
    reject:function() {
        $('#dg').datagrid('rejectChanges');
        this.editIndex = undefined;
    }
}
