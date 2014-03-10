<!DOCTYPE html>
<html>
<head>
    <title>Hong Soft</title>
    <link type="text/css" rel="stylesheet" href="${rc.contextPath}/css/front.css">
    <link href="${rc.contextPath}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${rc.contextPath}/css/login_regist.css" rel="stylesheet" type="text/css" />

    <script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript">
        $(function(){
            $('#kaptchaImage').click(function () {//生成验证码
                $(this).hide().attr('src', '${rc.contextPath}/anonymous/kaptchaImg?' + Math.floor(Math.random()*100)).fadeIn();
                event.cancelBubble=true;
            });
        });

        function changeCode() {
            $("#kaptchaImage").hide().attr('src', '${rc.contextPath}/anonymous/kaptchaImg?' + Math.floor(Math.random()*100)).fadeIn();
            event.cancelBubble=true;
        }

        function checkSubmit() {
            var flag = false;
            $.ajax({
                async: false,
                type: "get",
                url: "${rc.contextPath}/anonymous/checkCaptcha",
                data: "captcha=" + $("#kaptcha").val(),
                success: function(result) {
                    if (result == "false") {
                        $("#errorMsg").html("验证码错误");
                        $("#kaptchaImage").hide().attr('src', '${rc.contextPath}/anonymous/kaptchaImg?' + Math.floor(Math.random()*100)).fadeIn();
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
            });

            return flag;
        }
    </script>
</head>
<body>

<div class="login_wrap">
    <div class="logo clearfix">
        <#--<h1 class="fl"><a href="#" class="out_text debte_png">车邦网上订单系统</a></h1>-->
        <#--<strong class="fr sys">CRM管理系统</strong>-->
    </div>
    <div class="login_content">
        <form action="${rc.contextPath}/j_spring_security_check" method="post" onsubmit="return checkSubmit()" id="f" focus="username">
            <ul class="form_ul">
                <li class="clearfix">
                    <input type="hidden" name="cardNumber" id="cardNumber" />
                    <span class="form_span fl mr20">用户名：</span>
                    <input id="username" name="username" size="18" class="text"/>
                </li>
                <li class="clearfix">
                    <span class="form_span fl mr20">密　码：</span>
                    <input type="password" id="password" name="password" size="18" class="text"/>
                </li>
                <li class="clearfix">
                    <span class="form_span fl mr20">验证码：</span>
                    <input name="kaptcha" id="kaptcha" type="text" class="text veryfyCode fl mr10"  maxlength="4">
                    <a href="#" title="点击更换验证码" class="fl mr15">
                        <img src="${rc.contextPath}/anonymous/kaptchaImg" id="kaptchaImage"  style="margin-bottom: -3px"/>
                        <#--<img src="" id="createCheckCode" style="border:#999999 solid 1px">-->
                    </a>
                    <a href="#" onClick="changeCode()" id="myReloadHref" class="change_code fl">看不清<span class="high_light">换一张</span></a>
                </li>
                <li class="clearfix pt15">
                    <input type="submit" value="登录" class="btn debte_png fl mr35"/>
                </li>
                <li><span id="errorMsg" style="color: #ff0000">
                    ${message!}
                </span></li>
            </ul>
        </form>
        <div class="top_border debte_png"></div>
        <div class="left_border debte_png"></div>
        <div class="right_border debte_png"></div>
        <div class="bottom_border debte_png"></div>
        <div class="white"></div>
    </div>
    <div class="copyright">
        版权所有2014
    </div>
</div>

</body>
</html>