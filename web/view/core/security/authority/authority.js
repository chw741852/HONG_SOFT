/**
 * Created by hong on 14-2-23 下午5:09.
 */
function AuthorityScript(){}

AuthorityScript.prototype = {
    initValidate:function() {
        $("#authorityForm").validate({
            submitHandler:function(form) {
                $('#submitBtn').linkbutton('disable');
                form.submit();
            },
            rules: {
                name: "required"
            },
            messages: {
                name: "名称不能为空"
            },
            showErrors:function(errorMap, errorList) {
                var msg = "";
                $.each(errorList, function(i, v) {
                    msg += (v.message + "<br/>");
                });
                if (msg.trim() != "") {
                    scriptTools.showMsg(msg, 3000);
                }
            },
            onkeyup: false  // 输入时不验证
        });
    },
    initResourceTree:function(zNodes) {
        var setting = {
            check: {
                enable: true,
                chkboxType: { "Y": "", "N": "" }
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        }

        $.fn.zTree.init($("#resourceTree"), setting, zNodes);
    },
    subForm:function() {
        var zTree = $.fn.zTree.getZTreeObj("resourceTree");
        var nodes = zTree.getCheckedNodes(true);
        var nodeArr = [];
        $.each(nodes, function(i, node){
            nodeArr.push(node.id);
        });
        $("#resourceIds").val(nodeArr);
        $("#authorityForm").submit();
    }
}
