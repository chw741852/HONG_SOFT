/**
 * Created by hong on 14-2-23 下午5:09.
 */
function UserScript(){}

UserScript.prototype = {
    initValidate:function() {
        var _this = this;
        $("#userForm").validate({
            submitHandler:function(form) {
                if (_this.checkUsername() == true) {
                    _this.checkRole();
                    $('#submitBtn').linkbutton('disable');
                    form.submit();
                }
            },
            rules: {
                name: "required",
                username: "required",
                password: {
                    required: true,
                    minlength: 6
                },
                confirmPassword: {
                    equalTo: "#password"
                },
                email: "email"
            },
            messages: {
                name: "真实姓名不能为空",
                username: "用户名不能为空",
                password: {
                    required: "密码不能为空",
                    minlength: $.format("密码不能小于{0}个字符")
                },
                confirm_password: {
                    equalTo: "两次输入密码不一致"
                },
                email: "请输入正确的email地址"
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
    checkUsername:function() {
        if ($("#id").val() == undefined) {
            var flag;
            $.ajax({
                type: "get",
                async: false,
                url: contextPath + "/core/security/user/checkUsername",
                data: "username=" + $("#username").val(),
                dataType: "json",
                success: function(data) {
                    if(data.errorMsg) {
                        $("#username").focus();
                        scriptTools.showMsg(data.errorMsg, "");
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
            });
            return flag;
        } else {
            return true;
        }
    },
    checkRole:function() {
        var ids = "";
        $(".roleId:checked").each(function(i){
            ids = ids + $(this).val() + ",";
        });
        if (ids.length > 0) {
            ids = ids.substring(0, ids.length-1);
        }
        $("#roleIds").val(ids);
    }
}
