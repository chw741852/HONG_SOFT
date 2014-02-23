/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-27
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
// IE 不支持jquery的trim()方法，重写trim()方法
String.prototype.trim = function() {
    return this.replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
};

function ScriptTools() {

}

ScriptTools.prototype = {
    // iframe自适应高度
    dynIframeSize:function(down) {
        var pTar = null;
        if (document.getElementById){
            pTar = document.getElementById(down);
        }else{
            eval('pTar = ' + down + ';');
        }
        if (pTar && !window.opera){
            //begin resizing iframe
            pTar.style.display="block";
            if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
                //ns6 syntax
                pTar.height = pTar.contentDocument.body.offsetHeight +20;
                pTar.width = pTar.contentDocument.body.scrollWidth+20;
            }else if (pTar.Document && pTar.Document.body.scrollHeight){
                //ie5+ syntax
                pTar.height = pTar.Document.body.scrollHeight;
                pTar.width = pTar.Document.body.scrollWidth;
            }
        }
    },
    /**
     * 设置皮肤
     * @param obj
     * @param section 修改URL的截点位置
     * @param theme cookie中保存的皮肤
     * @param defaultTheme  默认皮肤
     * @param cssName
     */
    setTheme:function(obj, section, theme, defaultTheme, cssName) {
        if (theme == undefined) {
            theme = defaultTheme;
        }
        var url = $(obj).attr("href");
        var href = url.substring(0, url.indexOf(section)) + section + "/" + theme + "/" + cssName;
        $(obj).attr("href", href);
    },
    showMsg:function(msg, timeout) {
        if (timeout == "") {
            timeout = "3000"
        }
        $.messager.show({
            title:'提示',
            msg:msg,
            showType:'show',
            timeout:timeout,
            style:{
                left:'',
                right:0,
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        });
    },
    printDateGrid:function(printName, datagrid) {
        var tableString = "<div id='printDiv'><table cellspacing='0' class='pb'>";
        var frozenColumns = datagrid.datagrid("options").frozenColumns; // 得到frozenColumns对象
        var columns = datagrid.datagrid("options").columns; // 得到columns对象
        var nameList = "";

        // 载入title
        if (typeof columns != "undefined" && columns != "") {
            $(columns).each(function(index){
                tableString += "\n<tr>";
                if (typeof frozenColumns != "undefined" && typeof frozenColumns[index] != "undefined") {
                    for (var i=0; i<frozenColumns[index].length; i++) {
                        if (frozenColumns[index][i].print == true) {
                            tableString += '\n<th width="' + frozenColumns[index][i].width + '"';
                            if (typeof frozenColumns[index][i].rowspan != "undefined" && frozenColumns[index][i].rowspan > 1) {
                                tableString += ' rowspan="' + frozenColumns[index][i].rowspan + '"';
                            }
                            if (typeof frozenColumns[index][i].colspan != "undefined" && frozenColumns[index][i].colspan > 1) {
                                tableString += ' colspan="' + frozenColumns[index][i].colspan + '"';
                            }
                            if (typeof frozenColumns[index][i].field != "undefined" && frozenColumns[index][i].field != "") {
                                nameList += ',{"f":"' + frozenColumns[index][i].field + '", "a":"' + frozenColumns[index][i].align + '"}';
                            }
                            tableString += '>' + frozenColumns[0][i].title + '</th>';
                        }
                    }
                }
                for (var i=0; i<columns[index].length; i++) {
                    if (columns[index][i].print == true) {
                        tableString += '\n<th width="' + columns[index][i].width + '"';
                        if (typeof columns[index][i].rowspan != 'undefined' && columns[index][i].rowspan > 1) {
                            tableString += ' rowspan="' + columns[index][i].rowspan + '"';
                        }
                        if (typeof columns[index][i].colspan != 'undefined' && columns[index][i].colspan > 1) {
                            tableString += ' colspan="' + columns[index][i].colspan + '"';
                        }
                        if (typeof columns[index][i].field != 'undefined' && columns[index][i].field != '') {
                            nameList += ',{"f":"' + columns[index][i].field + '", "a":"' + columns[index][i].align + '"}';
                        }
                        tableString += '>' + columns[index][i].title + '</th>';
                    }
                }
                tableString += '\n</tr>';
            });
        }

        // 载入内容
        var rows = datagrid.datagrid("getRows");    // 获取当前页所有行
        var nl = eval('([' + nameList.substring(1) + '])');
        for (var i=0; i<rows.length; i++) {
            tableString += '\n<tr>';
            $(nl).each(function(j){
                var e = nl[j].f.lastIndexOf('_0');

                tableString += '\n<td';
                if (nl[j].a != "undefined" && nl[j].a != "") {
                    tableString += ' style="text-aligh:' + nl[j].a + ';"';
                }
                tableString += '>';
                if (e + 2 == nl[j].f.length) {
                    tableString += rows[i][nl[j].f.substring(0, e)];
                } else {
                    tableString += rows[i][nl[j].f];
                }
                tableString += '</td>';
            });
            tableString += '\n</tr>';
        }
        tableString += '\n</table></div>';
        window.showModalDialog(contextPath + "/js/tools/print.ftl", tableString,
            "location:No;status:No;help:No;dialogWidth:800px;dialogHeight:600px;scroll:auto;");
    }
}