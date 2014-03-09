/**
 * Created by hong on 14-2-20 下午2:29.
 */
function PublicListInfoScript() {}

PublicListInfoScript.prototype = {
    changeDateType:function() {
        var name = $(this).attr("name");
        switch ($(this).val()) {
            case "0":
                $("." + name + "_dateFrom").html("从");
                $("." + name + "_dateTo").show();
                break;
            case "1":
                $("." + name + "_dateFrom").html("大于");
                $("." + name + "_dateTo").hide();
                break;
            case "2":
                $("." + name + "_dateFrom").html("大于等于");
                $("." + name + "_dateTo").hide();
                break;
            case "3":
                $("." + name + "_dateFrom").html("小于");
                $("." + name + "_dateTo").hide();
                break;
            case "4":
                $("." + name + "_dateFrom").html("小于等于");
                $("." + name + "_dateTo").hide();
                break;
            case "5":
                $("." + name + "_dateFrom").html("等于");
                $("." + name + "_dateTo").hide();
                break;
        }
    },
    searchListInfo:function() {
        var params = {};
        $("form :input").each(function(i) {
            var name = $(this).attr("name");
            params[name] = encodeURI($(this).val());
        });
        $("#dg").datagrid("load", params);
    },
    datagridOnLoadSuccess:function(data) {
        var strs = hideFieldStr.split(",");
        $.each(strs, function(i, s) {
            if (s != null && s != "") {
                $("#dg").datagrid("hideColumn", s);
            }
        });
    },
    btnClick:function() {
        var url = $(this).attr("url");
        var opType = $(this).attr("opType");
        var verify = $(this).attr("verify");
        var linkType = $(this).attr("linkType");
        var rows = $("#dg").datagrid("getChecked");
        if (opType == "0") {
            location.href = contextPath + url;
            return true;
        }
        if (rows.length <= 0) {
            scriptTools.showMsg("请选择一条记录","");
            return false;
        }
        if (opType == "1") {
            if (rows.length > 1) {
                scriptTools.showMsg("只能操作单条记录", "");
                return false;
            }
            if (linkType == "3") {
                if (window.confirm("确定删除选中记录？")) {
                    location.href = contextPath + url + "?id=" + rows[0].id;
                    return true;
                }
            } else {
                location.href = contextPath + url + "?id=" + rows[0].id;
                return true;
            }
        } else {
            var ids = "";
            $.each(rows, function(i, row){
                ids = ids + row.id + ",";
            });
            ids = ids.substring(0, ids.length - 1);
            if (linkType == "3") {
                if (window.confirm("确定删除选中记录？")) {
                    location.href = contextPath + url + "?ids=" + ids;
                    return true;
                }
            }
        }
    },
    datagridOnDblClickRow:function(index, data) {
        if (dbClickRowLinkId != "" && dbClickRowLinkId != "null") {
            $("#tb :link").each(function(i){
                if ($(this).attr("linkId") == dbClickRowLinkId) {
                    if ($(this).attr("opType") == "1") {
                        location.href = contextPath + $(this).attr("url") + "?id=" + data.id;
                    } else {
                        location.href = contextPath + $(this).attr("url") + "?ids=" + data.id;
                    }
                }
            });
        }
    },
    initPagenationBtn:function() {
        var pager = $('#dg').datagrid().datagrid('getPager'); // get the pager of datagrid
        pager.pagination({
            buttons:[{
                iconCls:'icon-print',
                handler:function(){
                    scriptTools.printDateGrid("", $("#dg"));
                }
            }]
        });
    }
}