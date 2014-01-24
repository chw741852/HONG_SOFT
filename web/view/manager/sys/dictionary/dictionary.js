/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-10
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
function ScriptUtil() {
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
    zTreeClick:function(event, treeId, treeNode) {
        $.ajax({
            type: "Get",
            url: contextPath + "/manager/sys/dictionary/ajaxView",
            data: "id=" + treeNode.id,
            dataType: "json",
            success: function(data) {
                var result = data[0];
                $('#dictId').val(result.id);
                $('#dictSequence').html(result.sequence);
                $('#dictName').html(result.name);
                $('#dictKey').html(result.key);
                $('#parentNode').html(result.parentNode);

                $('#dg').datagrid('load', {id:result.id});
            }
        });
    },
    add:function() {
        $('#dictForm')[0].reset();
        $('#id').val('');
        $('#parentId').combotree('reload', contextPath + "/manager/sys/dictionary/ajaxFindDict?id=0");
        var zTree = $.fn.zTree.getZTreeObj("dictTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length > 0) {
            $('#parentId').combotree('setValue', treeNode.id);
        }
        $('#dlg').dialog('open').dialog('setTitle', '新建');
        this.url = contextPath + "/manager/sys/dictionary/saveOrUpdate";
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
                        $('#dictForm')[0].reset();
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
    saveDict:function() {
        var _this = this;
        $('#dictForm').form('submit',{
            url: _this.url,
            onSubmit: function() {
                if ($(this).form('validate') == true) {
                    $.ajax({
                        url: contextPath + '/manager/sys/dictionary/checkKey',
                        data: "key=" + $('#key').val(),
                        dataType: 'json',
                        success:function(result) {
                            if (result.flag == false) {
                                _this.showMsg(result.msg);
                            }
                            return result.flag;
                        }
                    });
                } else {
                    return false;
                }
            },
            success: function(result) {
                var result = eval('(' + result + ')');
                $('#dlg').dialog('close');

                if (result.bean.id > 0) {
                    var zTree = $.fn.zTree.getZTreeObj("dictTree"),
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
                        $('#dictId').val(result.bean.id);
                        $('#dictSequence').html(result.bean.sequence);
                        $('#dictKey').html(result.bean.key);
                        $('#dictName').html(result.bean.name);
                        $('#parentNode').html(result.bean.parent.name);

                        $('#dg').datagrid('load', {id:result.id});
                    }
                }

                $('#parentId').combotree('reload');
                _this.showMsg(result.message);
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

function EasyuiUtil() {}


