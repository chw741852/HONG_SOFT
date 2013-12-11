/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-25
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */

function ScriptUtil(defaults) {
    var settings = { contextPath:'' }
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
                key: {
                    name: "name"
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
        var zNodes = [
            {
                name: "系统设置",
                open: true,
                children: [
                    {
                        name: "模块管理",
                        action: _this.defaults.contextPath + "/manager/module/browse"
                    },
                    { name: "子节点" }
                ]
            },
            {
                name: "用户管理",
                children: [
                    {
                        name: "新建用户"
                    },
                    { name: "用户列表" }
                ]
            }
        ];

        $.fn.zTree.init($("#sysTree"), setting, zNodes);
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
    refreshZTree:function() {
        $.ajax({
            type: "Get",
            url: this.defaults.contextPath + ""
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
                content: '<iframe name="' + tabName + '" src="' + action + '" width="100%" height="99%"' +
                    ' frameborder="0"></iframe>'
            });
        } else {
            this.refreshTab(tabName, action);
        }
    },
    refreshTab:function(tabName, action) {
        var tab = $("#tabs").tabs('getTab', tabName);
        tab.find('iframe')[0].contentWindow.location.href = action;
    }
}
