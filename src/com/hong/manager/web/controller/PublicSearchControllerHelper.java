package com.hong.manager.web.controller;

import com.hong.core.generic.service.IGenericService;
import com.hong.manager.sys.queryModule.domain.SysField;
import com.hong.manager.sys.queryModule.domain.SysListLink;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hong on 14-2-20 上午11:35.
 */
public class PublicSearchControllerHelper {
    private IGenericService genericService;

    public PublicSearchControllerHelper(IGenericService genericService) {
        this.genericService = genericService;
    }

    public void generateQueryHtml(SysQueryModule queryModule, ModelAndView mv) {
        StringBuffer html = new StringBuffer();
        int i = 1;
        html.append("<table cellpadding='0' cellspacing='2'><tr>");
        String fieldName;
        for (SysField sysField:queryModule.getSysFields()) {
            if (sysField.getQueryCondition() == true) {
                if (sysField.getFieldAliasName() != null && !sysField.getFieldAliasName().trim().equals("")) {
                    fieldName = sysField.getFieldAliasName();
                } else {
                    fieldName = sysField.getFieldName();
                }
                if (sysField.getControlKind().equals("1")) {    // 日期控件
                    if ((i - 1) % 3 == 0) {
                        html.append("<td class='fieldtitle'>" + sysField.getDisplayName() + "</td>");
                        html.append("<td class='fieldinput'>");
                        appendDateType(html, fieldName);
                    } else {
                        html.append("</td></tr><tr>");
                        html.append("<td class='fieldtitle'>" + sysField.getDisplayName() + "</td>");
                        html.append("<td class='fieldinput'>");
                        appendDateType(html, fieldName);
                    }
                    i = 3;
                } else if (sysField.getControlKind().equals("2")){  // 单选框，暂时配合SQL语句使用
                    html.append("<td class='fieldtitle'>" + sysField.getDisplayName() + "</td>");
                    html.append("<td class='fieldinput'>");
                    html.append("<select name='" + fieldName + "' class='selectfield'>");
                    if ("1".equals(sysField.getDataSourceType())) { // SQL语句
                        List<String[]> dataSource = genericService.executeSql(sysField.getDataSourceSql());
                        html.append("<option value='null'>空白</option>");
                        html.append("<option value='' selected>全部</option>");
                        for (String[] s:dataSource) {
                            html.append("<option value='" + s[0] + "'>" + s[1] + "</option>");
                        }
                    }
                    html.append("</select>");
                } else {
                    html.append("<td class='fieldtitle'>" + sysField.getDisplayName() + "</td>");
                    html.append("<td class='fieldinput'>");
                    html.append("<input name='" + fieldName + "' class='inputtxt'>");
                }
                html.append("</td>");
                if (i % 3 == 0) {
                    html.append("</tr><tr>");
                }
                i++;
            }
        }
        html.append("</tr></table>");
        mv.addObject("queryHtml", html.toString());
    }

    public void generateDataGridHtml(SysQueryModule queryModule, ModelAndView mv, String contextPath) {
        StringBuffer html = new StringBuffer();
//        title='" + mv.getModel().get("nav2") + "列表'
        html.append("<table class='easyui-datagrid' title='' id='dg'" +
                " data-options=\"singleSelect:false, fitColumns:true, rownumbers:true, toolbar:'#tb'," +
                " pagination:true, pageSize:");
        Integer[] pageList = new Integer[5];
        int pageSize;
        if (queryModule.getPageSize() != null && queryModule.getPageSize() > 0) {
            html.append(queryModule.getPageSize().toString());
            pageSize = queryModule.getPageSize();
        } else {
            html.append("10");
            pageSize = 10;
        }
        for (int i=0; i<5; i++) {
            pageList[i] = pageSize * (i+1);
        }
        html.append(", pageList:" + Arrays.toString(pageList));
        html.append(", url:'" + contextPath + "/manager/web/queryListInfo?queryModuleId=" + queryModule.getId() +
                "', method:'get', onLoadSuccess:scriptInstance.datagridOnLoadSuccess, " +
                "onDblClickRow:scriptInstance.datagridOnDblClickRow\">");
        html.append("<thead>" +
                "<th data-options=\"field:'easyui_ck', checkbox:true, print:false\"></th>");

        StringBuffer hideField = new StringBuffer();    // 保存隐藏字段

        float width = (float) (1.00/(queryModule.getSysFields().size()));
        for (SysField sysField:queryModule.getSysFields()) {
            String fieldName;
            if (sysField.getFieldAliasName() != null && !sysField.getFieldAliasName().trim().equals("")) {
                fieldName = sysField.getFieldAliasName();
            } else {
                fieldName = sysField.getFieldName();
            }
            html.append("<th data-options=\"field:'" + fieldName + "'");
            if (sysField.getRowWidth() != null && !sysField.getRowWidth().trim().equals("")) {
                html.append(", width:" + sysField.getRowWidth());
            } else {
                html.append(", width:$(this).width()*" + width);
            }
            html.append(", print:" + sysField.getPrint() + "\">" + sysField.getDisplayName() + "</th>");
            if (sysField.getDisplay() == false) {
                hideField.append(fieldName + ",");
            }
        }
        html.append("</thead></table>");

        /* ************ 按钮 ************** */
        html.append("<div id='tb'>");
        for (SysListLink link:queryModule.getSysListLinks()) {
            if (link.getDisplay() == true) {
                html.append("<a href='javascript:void(0)' url=\"" + link.getUrl() + "\" opType='" + link.getOpType() +
                        "' verify='" + link.getVerify() + "' linkType='" + link.getLinkType() + "' linkId='" + link.getId() +
                        "' verifyCondition=\"" + link.getVerifyCondition() +
                        "\" onclick='scriptInstance.btnClick.call(this)' class='easyui-linkbutton' ");
                if (link.getLinkType().equals("1")) {   // 新建
                    html.append("data-options=\"iconCls:'icon-add',plain:true\"");
                } else if (link.getLinkType().equals("2")) {    // 修改
                    html.append("data-options=\"iconCls:'icon-edit',plain:true\"");
                } else if (link.getLinkType().equals("3")) {    // 删除
                    html.append("data-options=\"iconCls:'icon-remove',plain:true\"");
                } else if (link.getLinkType().equals("4")) {    // 查看
                    html.append("data-options=\"iconCls:'icon-search',plain:true\"");
                } else if (link.getLinkType().equals("5")) {    // 打印
                    html.append("data-options=\"iconCls:'icon-print',plain:true\"");
                } else if (link.getLinkType().equals("6")) {    // 提交
                    html.append("data-options=\"iconCls:'icon-redo',plain:true\"");
                } else if (link.getLinkType().equals("7")) {    // 审核
                    html.append("data-options=\"iconCls:'icon-ok',plain:true\"");
                } else if (link.getLinkType().equals("8")) {    // 弃交
                    html.append("data-options=\"iconCls:'icon-undo',plain:true\"");
                } else if (link.getLinkType().equals("9")) {    // 反审核
                    html.append("data-options=\"iconCls:'icon-reload',plain:true\"");
                }
                html.append(">" + link.getName() + "</a>");
            }
        }
        html.append("</div>");

        mv.addObject("datagrid", html.toString());

        if (hideField.length() > 0) {
            mv.addObject("hideFieldStr", hideField.substring(0, hideField.length()-1));
        } else {
            mv.addObject("hideFieldStr", "");
        }
    }
    
    public String[] generateListInfoSql(SysQueryModule queryModule, HttpServletRequest request) {
        String[] sqls = new String[2];
        StringBuffer sql = new StringBuffer();
        StringBuffer querySql = new StringBuffer();
        StringBuffer fromSql = new StringBuffer(queryModule.getRelationSql());

        sql.append("select ");
        for (SysField sysField:queryModule.getSysFields()) {
            String tableName;
            if (sysField.getTableAliasName() != null && !sysField.getTableAliasName().trim().equals("")) {
                tableName = sysField.getTableAliasName();
            } else {
                tableName = sysField.getTableName();
            }
            sql.append(tableName + "." + sysField.getFieldName() + " " + sysField.getFieldAliasName() + ",");

            /* ********************************** 处理查询条件 **************************************** */
            if (sysField.getQueryCondition() == true) { // 是查询字段
                String fieldName;
                if (sysField.getFieldAliasName() != null && !sysField.getFieldAliasName().trim().equals("")) {
                    // 别名不为空
                    fieldName = sysField.getFieldAliasName();
                } else {
                    fieldName = sysField.getFieldName();
                }
                String queryCondition = request.getParameter(fieldName);
                if (queryCondition != null && !queryCondition.trim().equals("")) {  // 填写了该查询条件
                    try {   // 转码
                        queryCondition = URLDecoder.decode(queryCondition, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (sysField.getFieldDataType().equals("0") && sysField.getControlKind().equals("0")) {  // 字段数据类型为字符型
                        querySql.append(" and " + tableName + "." + sysField.getFieldName() + " like '%" +
                                queryCondition + "%'");
                    } else if (sysField.getControlKind().equals("1")){  // 日期类型查询条件
                        String startDate = request.getParameter(fieldName + "_startDate");
                        String endDate = request.getParameter(fieldName + "_endDate");
                        if (startDate != null && !startDate.equals("")) {
                            if (queryCondition.equals("0")) {   // 包含
                                if (endDate != null && !endDate.equals("")) {
                                    querySql.append(" and " + tableName + "." + sysField.getFieldName() +
                                            " between '" + startDate + "' and '" + endDate + "'");
                                }
                            } else if (queryCondition.equals("1")) {
                                querySql.append(" and " + tableName + "." + sysField.getFieldName() +
                                    ">'" + startDate + "'");
                            } else if (queryCondition.equals("2")) {
                                querySql.append(" and " + tableName + "." + sysField.getFieldName() +
                                        ">='" + startDate + "'");
                            } else if (queryCondition.equals("3")) {
                                querySql.append(" and " + tableName + "." + sysField.getFieldName() +
                                        "<'" + startDate + "'");
                            } else if (queryCondition.equals("4")) {
                                querySql.append(" and " + tableName + "." + sysField.getFieldName() +
                                        "<='" + startDate + "'");
                            } else {
                                querySql.append(" and " + tableName + "." + sysField.getFieldName() +
                                        "='" + startDate + "'");
                            }
                        }
                    } else {
                        if (sysField.getControlKind().equals("2") && queryCondition.equals("null")) {
                            querySql.append(" and " + tableName + "." + sysField.getFieldName() + " is null");
                        } else {
                            querySql.append(" and " + tableName + "." + sysField.getFieldName() + "='" +
                                queryCondition + "'");
                        }
                    }
                }
            }
        }
        sql.deleteCharAt(sql.length()-1);
        if (fromSql.indexOf(" where ") < 0) {
            fromSql.append(" where 1=1 ");
        }

        sqls[0] = sql.toString() + " " + fromSql + querySql;
        sqls[1] = "select count(*) " + fromSql + querySql;

        return sqls;
    }

    private void appendDateType(StringBuffer html, String fieldName) {
        html.append("<select name='" + fieldName + "' class='selectfield' onchange='scriptInstance.changeDateType.call(this)'>" +
                "<option value='0'>范围</option><option value='1'>大于</option>" +
                "<option value='2'>大于等于</option><option value='3'>小于</option>" +
                "<option value='4'>小于等于</option><option value='5'>等于</option></select>");
        html.append("</td>");
        html.append("<td class='fieldtitle'><span class='" + fieldName + "_dateFrom'>从</span></td>");
        html.append("<td class='fieldinput'>");
        html.append("<input name='" + fieldName + "_startDate' class='inputtxt' onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\">");
        html.append("</td>");
        html.append("<td class='fieldtitle'><span class='" + fieldName + "_dateTo'>至</span></td>");
        html.append("<td class='fieldinput'><span class='" + fieldName + "_dateTo'>");
        html.append("<input name='" + fieldName + "_endDate' class='inputtxt' onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\">");
        html.append("</span>");
    }
}
