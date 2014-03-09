/**
 * Created by hong on 14-2-23 下午5:09.
 */
function ResourceScript(){
    this.url = contextPath + "/core/security/resource/saveOrUpdate";
    this.flag = "";
}

ResourceScript.prototype = {
    beforeDrag:function(treeId, treeNodes) {
        return true;
    },
    beforeDrop:function(treeId, treeNodes, targetNode, moveType) {
        return true;
    },
    onDrop:function(event, treeId, treeNodes, targetNode, moveType) {
        // TODO 只支持拖动为子节点，否则不保存
        if (moveType == "inner") {
            $.ajax({
                type: "post",
                url: contextPath + "/core/security/resource/zTreeDrop",
                data: "id=" + treeNodes[0].id + "&targetId=" + targetNode.id + "&type=" + moveType,
                dataType: "json",
                success: function(result) {
                    if (result.errorMsg) {
                        scriptTools.showMsg(result.errorMsg, "");
                    }
                }
            });
        }
    },
    zTreeClick:function(event, treeId, treeNode) {
        $.ajax({
            type: "Get",
            url: contextPath + "/core/security/resource/ajaxView",
            data: "id=" + treeNode.id,
            dataType: "json",
            success: function(result) {
                $('#resourceId').val(result.id);
                $("#resourceName").html(result.name);
                $("#resourceUrl").html(result.url);
                if (result.parent.name == undefined) {
                    $("#parentNode").html("");
                } else {
                    $("#parentNode").html(result.parent.name);
                }
                if (result.enabled == true) {
                    $("#resourceEnabled").html("是");
                } else {
                    $("#resourceEnabled").html("否");
                }

                $('#dg').datagrid('load', {id:result.id});
            }
        });
    },
    add:function() {
        $('#resourceForm')[0].reset();
        $('#id').val('');
        $('#parentId').combotree('reload', contextPath + "/core/security/resource/ajaxFindResource?id=0");
        var zTree = $.fn.zTree.getZTreeObj("resourceTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length > 0) {
            $('#parentId').combotree('setValue', treeNode.id);
        }
        $('#dlg').dialog('open').dialog('setTitle', '新建资源');
        this.flag = "add";
    },
    edit:function() {
        var zTree = $.fn.zTree.getZTreeObj("resourceTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length > 0) {
            $('#parentId').combotree('reload', contextPath + "/core/security/resource/ajaxFindResource?id=" + treeNode.id);
            $("#resourceForm").form("load", contextPath + "/core/security/resource/edit?id=" + treeNode.id);
            $('#dlg').dialog('open').dialog('setTitle','修改资源');
            this.flag = "edit";
        } else {
            scriptTools.showMsg("请选择一个节点", "");
        }
    },
    saveResource:function() {
        var _this = this;
        $('#resourceForm').form('submit',{
            url: _this.url,
            onSubmit: function() {
                return $(this).form('validate');
            },
            success: function(result) {
                var result = eval('(' + result + ')');
                $('#dlg').dialog('close');

                if (result.bean.id > 0) {
                    var zTree = $.fn.zTree.getZTreeObj("resourceTree"),
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

                        $('#resourceId').val(result.bean.id);
                        $("#resourceName").html(result.bean.name);
                        $("#resourceUrl").html(result.bean.url);
                        $("#parentNode").html(result.bean.parent.name);
                        if (result.bean.enabled == true) {
                            $("#resourceEnabled").html("是");
                        } else {
                            $("#resourceEnabled").html("否");
                        }
                    }
                    $('#dg').datagrid('reload');
                }

                $('#parentId').combotree('reload');
                scriptTools.showMsg(result.message);
            }
        });
    },
    remove:function() {
        var _this = this;
        var zTree = $.fn.zTree.getZTreeObj("resourceTree"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length == 0) {
            scriptTools.showMsg("请选择一个节点");
            return;
        }
        $.messager.confirm('Confirm', '确认删除 节点 -- ' + treeNode.name + ' 以及其子节点吗？', function(r){
            if (r){
                $.ajax({
                    type: "Get",
                    url: contextPath + "/core/security/resource/delete",
                    data: "id=" + treeNode.id,
                    dataType: "json",
                    success: function(result) {
                        if (result.errorMsg) {
                            scriptTools.showMsg(result.errorMsg, "");
                        } else {
                            if ($('#resourceId').val() == treeNode.id) {
                                $('#resourceId').val("");
                                $("#resourceName").html("");
                                $("#resourceUrl").html("");
                                $("#parentNode").html("");
                                $("#resourceEnabled").html("");
                                $('#dg').datagrid('load', {id:0});
                            }
                            zTree.removeNode(treeNode, false);  //  false - 不促发回调函数
                        }
                    }
                });
            }
        });
    }
}

function ResourceEasyui() {}

ResourceEasyui.prototype = {
    onLoadSuccess: function(data) {
        $('#dg').datagrid('hideColumn', 'id');
    }
}
