/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-25
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */

function ScriptUtil(defaults) {
    var settings = { contextPath:'' }
    var zTreeSetting = {};
    if (defaults) {
        $.extend(settings, defaults);
    }
    this.defaults = settings;
    this.initZTree();
}

ScriptUtil.prototype = {
    initZTree: function() {
        var _this = this;
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: function(event, treeId, treeNode){
                    if ($.trim(treeNode.action) == '' || $.trim(treeNode.action) == "''") {

                    } else {
                        _this.addTab(treeNode.name, treeNode.action);
                    }
                }
            }
        };
        _this.zTreeSetting = setting;
//        var zNodes = [
//            {
//                name: "系统设置",
//                open: true,
//                children: [
//                    {
//                        name: "模块管理",
//                        action: _this.defaults.contextPath + "/manager/module/browse"
//                    }
//                ]
//            }
//        ];
//
//        $.fn.zTree.init($("#sysTree"), setting, zNodes);
        _this.refreshZTree();
        $.fn.panel.defaults.onBeforeDestroy = function() {  // 释放内存
            var frame = $('iframe', this);
            if (frame.length > 0) {
                frame[0].contentWindow.document.write('');
                frame[0].contentWindow.close();
                if ($.browser.msie) {
                    CollectGarbage();
                }
                frame[0].remove();  // 此处在 IE8 中测试报错
            }
        }
    },
    refreshZTree:function() {       // TODO
        $.ajax({
            type: "get",
            url: this.defaults.contextPath + "/manager/home/loadZTreeNodes",
            dataType: "json",
            success: function(result) {
                var setting = scriptInstance.zTreeSetting;
                $.fn.zTree.init($("#sysTree"), setting, result);
            }
        });
    },
    addTab:function(tabName, action) {
        if (!$("#tabs").tabs('exists', tabName)) {
            var ableClose = true;
            if (tabName == "首页") {
                ableClose = false;
            }

            $("#tabs").tabs('add',{
                title: tabName,
                closable: ableClose,
                content: "<iframe name='" + tabName + "' src='" + this.defaults.contextPath + action
                    + "' width='100%' height='99%' frameborder='0'></iframe>"
            });
        } else {
            this.refreshTab(tabName, action);
            $("#tabs").tabs("select", tabName);
        }
    },
    refreshTab:function(tabName, action) {
        var tab = $("#tabs").tabs('getTab', tabName);
        tab.find('iframe')[0].contentWindow.location.href = this.defaults.contextPath + action;
    },
    changeTheme:function() {
        var url = $("#easyuiTheme").attr("href");
        var href = url.substring(0, url.indexOf("themes")) + "themes/" + $("#theme").val() + "/easyui.css";
        $("#easyuiTheme").attr("href", href);

        var $iframe = $("iframe");
        if ($iframe.length > 0) {
            for (var i=0; i<$iframe.length; i++) {
                $iframe.eq(i).contents().find("#easyuiTheme").attr("href", href);
                var $tableTheme = $iframe.eq(i).contents().find("#tableTheme");
                if ($tableTheme.attr("href") != undefined && $tableTheme.attr("href") != null) {
                    var tableUrl = $tableTheme.attr("href");
                    var tableHref = tableUrl.substring(0, tableUrl.indexOf("themes")) + "themes/" + $("#theme").val() + "/table.css";
                    $tableTheme.attr("href", tableHref);
                }
            }
        }

        $.cookie("themeName", $("#theme").val(), {
            path: this.defaults.contextPath + "/",
            expires: 7      // cookie期限为7天
        });
    }
}
