<!DOCTYPE html>
<html>
<head>
    <title>Hong Soft</title>
    <link type="text/css" rel="stylesheet" href="${rc.contextPath}/css/front.css">
</head>
<body>
<div class="center">
    <form action="${rc.contextPath}/j_spring_security_check" method="post">
        <label for="username">用户名：</label>
        <input id="username" name="username"/><br/>
        <label for="password">密 码：</label>
        <input type="password" id="password" name="password"/><br/>
        <input type="submit" value="登录"/>
    </form>
</div>

</body>
</html>