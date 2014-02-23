<!DOCTYPE html>
<html>
<head>
    <title>数据打印</title>
    <meta http-equiv="CONTENT-TYPE" content="text/html; charset=UTF-8">

    <script type="text/javascript" src="../jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="../LodopFuncs.js"></script>

    <style id="style1" type="text/css">
        body{background:white;margin:0px;padding:0px;font-size:13px;text-align:left;}
        .pb{font-size:13px;border-collapse:collapse;}
        .pb th{font-weight:bold;text-align:center;border:1px solid #333333;padding:2px;}
        .pb td{border:1px solid #333333;padding:2px;}
    </style>
</head>
<body>
    <DIV align=center>
        <INPUT onclick="javascript:print()" type="button" value="直接打印" />
        <INPUT onclick="javascript:printSetup()" type="button" value="打印页面设置" />
        <INPUT onclick="javascript:printPreview()" type="button" value="打印预览" />
        <INPUT onclick="javascript:saveFile()" type="button" value="导出Excel" />
        <hr>
    </DIV>
    <script type="text/javascript">
        document.write(window.dialogArguments);
    </script>
</body>
<script type="text/javascript">
    var printHtm = "<style>" + $("#style1").html() + "</style><body>" + $("#printDiv").html() + "</body>";

    function print() {
        var LODOP=getLodop();
        LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
        LODOP.ADD_PRINT_HTM(20, 10, 750, 900, printHtm);
        LODOP.PRINT();
    }
    function printSetup() {
        var LODOP=getLodop();
        LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
        LODOP.ADD_PRINT_HTM(20, 10, 750, 900, printHtm);
        LODOP.PRINT_SETUP();
    }
    function printPreview() {
        var LODOP=getLodop();
        LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
        LODOP.ADD_PRINT_HTM(0, 0, 750, 900, printHtm);
        LODOP.PREVIEW();
    }
    function saveFile() {
        var LODOP=getLodop();
        LODOP.SET_SAVE_MODE("FILE_PROMPT", 1);
        LODOP.SET_SAVE_MODE("LINESTYLE", 1);
        LODOP.SET_SAVE_MODE("CAPTION", "数据列表");
        LODOP.ADD_PRINT_TABLE(0, 0, 0, 0, printHtm);
        LODOP.SAVE_TO_FILE("a.xls");
    }
</script>
</html>