/**
 * Created by hong on 14-2-23 下午5:09.
 */
function RoleScript(){}

RoleScript.prototype = {
    initValidate:function() {
        var _this = this;
        $("#roleForm").validate({
            submitHandler:function(form) {
                _this.checkAuthority();
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
    checkAuthority:function() {
        var ids = "";
        $(".authorityId:checked").each(function(i){
            ids = ids + $(this).val() + ",";
        });
        if (ids.length > 0) {
            ids = ids.substring(0, ids.length-1);
        }
        $("#authorityIds").val(ids);
    }
}
