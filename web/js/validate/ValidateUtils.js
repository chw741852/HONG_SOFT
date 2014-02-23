/**
 *@memo 每个验证规则
 **/
function ValidateRule(){
    this.name="";//控件名称
    this.labelName="";//标签名
    this.length=0;//长度
    this.type="string";//type：string,int,digit,float,email,ip,url,date,datetime,time
    //  tel,mobileCn,idCard,postcodeCn
    // default: string.
    this.hintMsg="";//验证无效提示语句
    this.express="";//验证的条件表达式
    this.validatePremiss="";//验证前提条件表达式
    this.notEmptyExpress="";//验证不为空的条件表达式
    this.attachEvent=null;//事件名称，添加到控件中哪个事件中进行验证
    this.callBackFun=null;//验证算法函数，若没提供则用系统中的进行校验
    this.hintContainer=null;//提示语句所在容器，若没有则alert提示
    this.required=false;//可选值有true和false.default:false.
    this.components=-1;//要求控件数条件
    this.notEmptyComponents=-1;//要求不为空数据的控件数
    this.maxComponents=-1;//要求不为空数据的最多控件数
    this.minComponents=-1;//有效的最少控件数
    this.control=null;//要验证的控制对象，若没有this.name属性，可直接传递对象
    this.controlRange=null;
    this.serviceRule=null;//与后台服务端交互验证规则
    this.includeNotDisplay=true;//是否包括不显示的
    this.excludeLastComponents=0;//需要除去最后的控件数

}
/**
 *@memo 与后台服务端交互验证规则
 **/
function serviceRuleRule(){
    this.parameter="";
    this.url="";
    this.async=false;
    this.callBackFun=null;
}
ValidateRule.TYPE_STRING="string";
ValidateRule.TYPE_INT="int";
ValidateRule.TYPE_POSITIVEINT="positiveInt";//正整数 
ValidateRule.TYPE_DIGIT="digit";//值中每个字符是否为都为数字
ValidateRule.TYPE_FLOAT="float";
ValidateRule.TYPE_DOUBlE="double";
ValidateRule.TYPE_POSITIVEDOUBlE="positiveDouble";//正的double类型

ValidateRule.TYPE_EMAIL="email";
ValidateRule.TYPE_IP="ip";
ValidateRule.TYPE_URL="url";
ValidateRule.TYPE_POSTCODECN="postcodeCn";//邮政编码
ValidateRule.TYPE_DATE="date";
ValidateRule.TYPE_DATETIME="datetime";
ValidateRule.TYPE_TIME="time";
ValidateRule.TYPE_MOBILECN="mobileCn";//移动电话
ValidateRule.TYPE_TEL="tel";//固定电话
ValidateRule.TYPE_IDCARD="idCard";//身份证验证 
/**验证器**/
DataValidator=function(){
    this.rules=null;
    this.getRules=function(){
        return this.rules;
    }
    this.setRules=function(rules){
        this.rules=rules;
    }
}
DataValidator.prototype.initValidator=function(rules){
    this.rules=rules;
    for(var i=0;i<this.rules.length;i++){
        if (typeof(this.rules[i].includeNotDisplay)=="undefined")
            this.rules[i].includeNotDisplay=false;
        if (typeof(this.rules[i].express)=="undefined")
            this.rules[i].express="";
        if (typeof(this.rules[i].validatePremiss)=="undefined")
            this.rules[i].validatePremiss="";
        if (typeof(this.rules[i].control)=="undefined")
            this.rules[i].control=null;
        if (typeof(this.rules[i].type)=="undefined")
            this.rules[i].type="string";
        if (typeof(this.rules[i].required)=="undefined")
            this.rules[i].required=false;
        if (typeof(this.rules[i].attachEvent)=="undefined")
            this.rules[i].attachEvent=null;
        if (typeof(this.rules[i].hintMsg)=="undefined")
            this.rules[i].hintMsg="";
        if (typeof(this.rules[i].controlRange)=="undefined")
            this.rules[i].controlRange=null;
        if (typeof(this.rules[i].notEmptyExpress)=="undefined")
            this.rules[i].notEmptyExpress="";
        if (typeof(this.rules[i].excludeLastComponents)=="undefined")
            this.rules[i].excludeLastComponents=0;
        if (typeof(this.rules[i].attachEvent)!="undefined"  && this.rules[i].attachEvent!=null){
            var checkObj=null;
            if (this.rules[i].control==null)
                checkObj=document.getElementsByName(this.rules[i].name);
            else
                checkObj=this.rules[i].control;
            for(var n=0;n<checkObj.length;n++){
                BrowserUtil.addEvent(checkObj[n],this.rules[i].attachEvent,this.validate,this,this.rules[i]);
            }
        }
    }
}
/**
 *@memo 表单数据有效性验证
 **/
DataValidator.prototype.checkForm=function(){
    for(var i=0;i<this.rules.length;i++){
        if (this.rules[i].attachEvent==null){
            var result=this.validate(this.rules[i]);
            if (typeof(result)=="undefined" )
                return false;
            if (result=="undefined" || result==false)
                return false;
        }
    }
    return true;
}
/**
 *@memo 表单数据权限验证
 *@parameters 包括sysQueryModuleId、async属性,
 **/
DataValidator.prototype.checkFormAuthority=function(parameters){
    var url=urlContextPath+"/jcf/userFormAuthorityServlet?timeStamp="+new Date().getTime();
    var http=new jcf.net.XMLHttpConnection();
    if(typeof(parameters)=="undefined") return ;
    if(parameters.constructor==String){
        var id=parameters;
        parameters=new Object();
        parameters.sysQueryModuleId=id;
    }
    if(typeof(parameters.sysQueryModuleId)=="undefined")
        return ;
    if(typeof(parameters.async)=="undefined")
        parameters.async=false;
    http.parameters=parameters;
    http.result=null;
    http.Onload=this.callbackCheckAuthorityFunc;
    http.Open("post",url,parameters.async);
    http.Send(parameters.toJSONString());
    return http.result;
}
DataValidator.prototype.callbackCheckAuthorityFunc=function(http){
    if (http.responseText!=null && http.responseText!="" ){
        this.result =http.responseText.parseJson();
    } else {
        this.result=jcf.db.ExecuteEngine.prototype.convertXmlToList(http.responseXML);
    }
    if(this.result!=null && this.result!="" && typeof(this.result.length)!="undefined"){
        for(var i=0;i<this.result.length;i++){
            var bean=this.result[i];
            if(bean.isAuthority=="1") continue;
            var controls=document.getElementsByName(bean.fieldName);
            if (controls!=null && typeof(controls.length)!="undefined" && controls.length>0)
                for(var n=0;n<controls.length;n++){
                    var node=controls[n];

                    if (typeof(node.value)!="undefined"){

                        node.value=bean.fieldDisplayValue;
                    } else if (typeof(node.text)!="undefined"){
                        node.text=bean.fieldDisplayValue;

                    }else if (typeof(node.firstChild)!=null && node.firstChild!=null ){

                        node.innerHTML=bean.fieldDisplayValue;
                        //node.firstChild.nodeValue=bean.fieldDisplayValue;
                    }
                }
        }
    }
    return  this.result;
}
DataValidator.prototype._notNeedValidate=function (checkObj,rule){
    if(rule.includeNotDisplay==false) {
        if (typeof(checkObj.style)!="undefined"){
            if (checkObj.style.display=="none")
                return true;
        }
    }
    if (typeof(checkObj.disabled)!="undefined"){
        if (checkObj.getAttribute("disabled")==true)
            return true;
    }
    if(typeof(checkObj.parentNode)!="undefined"){
        if(rule.includeNotDisplay==false) {
            if (typeof(checkObj.parentNode.style)!="undefined"){

                if (checkObj.parentNode.style.display=="none")
                    return true;
            }
        }
        if (typeof(checkObj.parentNode.disabled)!="undefined"){
            if (checkObj.parentNode.getAttribute("disabled")==true)
                return true;
        }
        if (typeof(checkObj.parentNode.parentNode)!="undefined"){
            if(rule.includeNotDisplay==false) {
                if (typeof(checkObj.parentNode.parentNode.style)!="undefined"){
                    if (checkObj.parentNode.parentNode.style.display=="none"){
                        return true;
                    }
                }
            }
            if (typeof(checkObj.parentNode.parentNode.disabled)!="undefined"){
                if (checkObj.parentNode.parentNode.getAttribute("disabled")==true)
                    return true;
            }
            if (typeof(checkObj.parentNode.parentNode.parentNode)!="undefined"){
                if(rule.includeNotDisplay==false) {
                    if (typeof(checkObj.parentNode.parentNode.parentNode.style)!="undefined"){
                        if (checkObj.parentNode.parentNode.parentNode.style.display=="none")
                            return true;
                    }
                }
                if (typeof(checkObj.parentNode.parentNode.parentNode.disabled)!="undefined"){
                    if (checkObj.parentNode.parentNode.parentNode.getAttribute("disabled")==true)
                        return true;
                }
            }
        }
    }

    return false;
}
DataValidator.prototype._setErrMessage=function(rule,msg){
    if(rule.hintContainer!= null){
        //rule.hintContainer.innerHTML =msg;
        BrowserUtil.innerText(rule.hintContainer,msg);
    }else{
        alert(msg);
    }
}
DataValidator.prototype._cleartErrMessage=function(rule){
    if(rule.hintContainer!= null){
        BrowserUtil.innerText(rule.hintContainer,"");
    }
}
DataValidator.prototype._notEmpty=function(checkObj,rule,i){
    if (typeof(rule.required)!=undefined && rule.required==true){
        if (getNodeValue(checkObj).Trim()==""){
            if(typeof(rule.notEmptyExpress)!="undefined" && rule.notEmptyExpress!=""){
                var expr=this._parseExpress(rule.notEmptyExpress,checkObj,rule,i);
                var booleanResult= ExecScript(expr.toString());
                if (booleanResult==false){
                    this._setErrMessage(rule,rule.labelName+"不能为空！");
                    if (typeof(checkObj.focus)!="undefined")
                    {
                        window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                    }
                    return false;
                }
            }  else {

                if (typeof(checkObj.focus)!="undefined")
                {
                    this._setErrMessage(rule,rule.labelName+"不能为空！");

                    window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);

                }
                return false;
            }
        }
    }
    return true;
}
DataValidator.prototype._totalNotEmptyValue=function(checkObj,rule){
    if (typeof(rule.required)!=undefined && rule.required==true){
        if (getNodeValue(checkObj).Trim()==""){
            return false;
        }
    }
    return true;
}
DataValidator.prototype._isAllowEmpty=function(checkObj,rule){
    if (typeof(rule.required)=="undefined" || rule.required==false){
        if ( getNodeValue(checkObj).Trim()==""){
            return true;
        }
    }
    return false;
}
DataValidator.prototype._checkLength=function(checkObj,rule){
    if (typeof(rule.length)!="undefined" && rule.length>0){
        if (validateUtil.stringRealLength(checkObj.value)>rule.length){
            this._setErrMessage(rule,"录入"+rule.labelName+"的内容长度不能超过"+rule.length+"个字符！");
            window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
            return false;
        }
    }
    return true;
}
DataValidator.prototype._checkChoseComponent=function(rule){

}

DataValidator.prototype.validate=function(rule){
    var checkObj=document.getElementsByName(rule.name);
    var result=false;
    var vtype = "string";
    var accordCondiComponents=0;//符合条件控件数
    var checkedComponents=0;//类型为checkbox、radio选中的控件数
    var notEmptyValueControls=0;//不为空值的控件数
    var totalControls=0;//有效控件总数
    if (typeof(rule.type)!="undefined")
        vtype=rule.type;

    if (checkObj.length>=1){
        var controls=checkObj.length;
        if(rule.excludeLastComponents>0){
            controls=controls-rule.excludeLastComponents;

        }
        for(var i=0;i<controls;i++){
            if (this._notNeedValidate(checkObj[i],rule)==true) {
                continue;
            }
            if (this._isAllowEmpty(checkObj[i],rule)==true){
                accordCondiComponents++;
                continue;
            }
            if(typeof(rule.premissValidateFun)!="undefined" && typeof(rule.premissValidateFun)=="function"){
                var trueOrFalse=rule.premissValidateFun.call(checkObj[i],checkObj[i],rule);
                if(trueOrFalse==false) continue;
            }
            var premissResult=this._checkPremissExpress(checkObj[i], rule,i);
            if (premissResult==false){
                continue
            }
            if (this._notEmpty(checkObj[i],rule,i)==false) {
                return;
            }else
                accordCondiComponents++;

            if (this._checkLength(checkObj[i], rule)==false)
                return false;
            if (this._checkType(checkObj[i], rule,i)==false)
                return false;
            var expressResult=this._checkExpress(checkObj[i], rule,i);
            if (expressResult==false)
                return false;
            else if(expressResult==-1)
                totalControls--;
            if (this._checkServiceValidator(checkObj[i], rule,i)==true)
                return false;
            if (typeof(rule.callBackFun)=="function"){
                var result=rule.callBackFun.call(checkObj[i],checkObj[i],rule);
                if (result==false)
                    return false;
            }
            if (checkObj[i].type=="checkbox" ||  checkObj[i].type=="radio"){
                if (checkObj[i].checked)
                    checkedComponents++;
            }
            if (typeof(rule.components)!="undefined")
                accordCondiComponents++;
            if(typeof(rule.notEmptyComponents)!="undefined")
                if( this._totalNotEmptyValue(checkObj[i],rule)==true)
                    notEmptyValueControls++;
            totalControls++;
        }
        var amount=accordCondiComponents;
        if (rule.required==true && amount<=0)
        {
            var needValidate=false;
            if (checkObj!=null && checkObj.length>=1)
                for(var i=0;i<1;i++){
                    if (this._notNeedValidate(checkObj[i],rule)==false) {
                        needValidate=true;
                    }
                    if(typeof(rule.premissValidateFun)!="undefined" && typeof(rule.premissValidateFun)=="function"){
                        var trueOrFalse=rule.premissValidateFun.call(checkObj[i],checkObj[i],rule);
                        if(trueOrFalse==false) {
                            needValidate=false;
                        } else {
                            needValidate=true;
                        }
                    }
                }
            if(needValidate==false) return true;
            this._setErrMessage(rule,rule.labelName+"不能为空！");
            if (typeof(checkObj.focus)!="undefined")
            {
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
            }
            return false;
        }
        if (typeof(rule.components)!="undefined"){
            if (checkObj[0].type=="checkbox" ||  checkObj[0].type=="radio"){
                amount=checkedComponents;
            }
            var booleanResult= ExecScript(amount.toString()+">="+rule.components);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.hintMsg);
                return false;
            }
        }
        if (typeof(rule.notEmptyComponents)!="undefined"){
            var booleanResult= ExecScript(notEmptyValueControls.toString()+">="+rule.notEmptyComponents);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.hintMsg);
                return false;
            }
        }
        if (typeof(rule.minComponents)!="undefined" && parseFloat(rule.minComponents)>=0){
            var booleanResult= ExecScript(totalControls.toString()+">="+rule.minComponents);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.labelName+"不能少于"+rule.minComponents+"条记录！");
                return false;
            }
        }
        if (typeof(rule.maxComponents)!="undefined" && parseFloat(rule.maxComponents)>=0){
            var booleanResult= ExecScript(totalControls.toString()+"<="+rule.maxComponents);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.labelName+"不能超过"+rule.maxComponents+"条记录！");
                return false;
            }
        }
        this._cleartErrMessage(rule);
    } else {

        if (typeof(rule.required)!=undefined && rule.required==true){
            if(rule.minComponents<=0){
                return true;
            }
            this._setErrMessage(rule,rule.labelName+"不存在！");
            return false;
        }
        if (typeof(rule.components)!="undefined"){
            var booleanResult= ExecScript("0>="+rule.components);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.hintMsg);
                return false;
            }
        }
        if (typeof(rule.notEmptyComponents)!="undefined"){
            var booleanResult= ExecScript(notEmptyValueControls.toString()+">="+rule.notEmptyComponents);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.hintMsg);
                return false;
            }
        }
        totalControls++;
        if (typeof(rule.minComponents)!="undefined" && parseFloat(rule.minComponents)>=0){
            var booleanResult=ExecScript(totalControls.toString()+">="+rule.minComponents);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.labelName+"不能少于"+rule.minComponents+"条记录！");
                return false;
            }
        }
        if (typeof(rule.maxComponents)!="undefined" && parseFloat(rule.maxComponents)>0){
            var booleanResult=ExecScript(notEmptyValueControls.toString()+"<="+rule.maxComponents);
            if (booleanResult==false){
                this._setErrMessage(rule,rule.labelName+"不能超过"+rule.maxComponents+"条记录！");
                return false;
            }
        }
    }
    return true;
}
DataValidator.prototype.parseJsToControls=function(checkObj,js){
    if (typeof(checkObj.id)!="undefined" && checkObj.id!="")
        js=" var "+checkObj.id+"=checkObj;"+js;
    else
        js=" var "+checkObj.name+"=checkObj;"+js;
    return eval(js);
}
DataValidator.prototype._validateExistRecord=function(sql){
    this.responseXML=null;
    this.existRecord=false;
    this.sql=sql;
    this.validateExistRec=function(){
        var parameter="<sql>"+ sql+ "<\/sql>";
        if (sql.indexOf ("<![CDATA[")<0){
            parameter = "<sql><![CDATA["+ sql+ "]]><\/sql>";
        }

        var url=urlContextPath+"/jcf/AjaxXmlDbServlet?timeStamp=" + new Date().getTime();
        var http=new jcf.net.XMLHttpConnection();
        http.Onload=this.callBackExistRecord;
        http.Open("post",url,false);
        http.Send(parameter);
        return http.existRecord;
    }
    this.callBackExistRecord=function(xmlhttp){

        if (xmlhttp.responseText!=""){
            var parser=new jcf.data.DOMImpl();
            this.responseXML =parser.Parse(xmlhttp.responseText);
        } else {
            this.responseXML=xmlhttp.responseXML;
        }
        var results = this.responseXML.getElementsByTagName("tableRecord");
        if (results.length==0){
            this.existRecord=false;
        } else {
            this.existRecord=true;
        }
    }
    return this;
}
/**
 *@memo 验证服务端业务规则
 **/
DataValidator.prototype._checkServiceValidator=function(checkObj,rule,i){

    if(typeof(rule.serviceRule)!="undefined" && rule.serviceRule!=null){
        var booleanResult=false;

        if(rule.serviceRule.constructor==String){

            var script=this._parseExpress(rule.serviceRule,checkObj,rule,i);
            var validator=this._validateExistRecord(script);
            booleanResult=validator.validateExistRec();
        } else {

            if(typeof(rule.serviceRule.callBackFun)=="undefined")
                rule.serviceRule.callBackFun=null;
            if(typeof(rule.serviceRule.url)=="undefined")
                rule.serviceRule.url="/jcf/AjaxXmlDbServlet?timeStamp=";
            var http=new jcf.net.XMLHttpConnection();
            http.existRecord=false;

            var parameter=this._parseExpress(rule.serviceRule.parameter,checkObj,rule,i);

            if (parameter=="")
                return false;
            if (parameter.indexOf ("<![CDATA[")<0){
                parameter = "<sql><![CDATA["+ parameter+ "]]><\/sql>";
            } else {
                parameter="<sql>"+ parameter+ "<\/sql>";
            }

            if(rule.serviceRule.callBackFun==null){
                http.Onload=this._checkServiceValidatorCallBack;

            } else{
                http.Onload=rule.serviceRule.callBackFun;
            }
            http.Open("post",urlContextPath+rule.serviceRule.url+new Date().getTime(),rule.serviceRule.async);
            http.Send(parameter);
            booleanResult= http.existRecord;
        }
        if (booleanResult==true){
            this._setErrMessage(rule,rule.hintMsg);
            window.setTimeout(function(){checkObj.focus();},0);
            return true;
        }
    }
    return false;
}
DataValidator.prototype._checkServiceValidatorCallBack=function(xmlhttp){

    if (xmlhttp.responseText!=""){
        var parser=new jcf.data.DOMImpl();
        this.responseXML =parser.Parse(xmlhttp.responseText);
    } else {
        this.responseXML=xmlhttp.responseXML;
    }
    var results = this.responseXML.getElementsByTagName("tableRecord");

    if (results.length==0){
        this.existRecord=false;
    } else {
        this.existRecord=true;
    }

}
DataValidator.prototype._parseExpress=function(express,checkObj,rule,i){
    if (express=="") return "";
    if (checkObj.parentNode!=null)
        if (checkObj.parentNode.parentNode!=null && typeof(checkObj.parentNode.parentNode.style)!="undefined"){
            if (checkObj.parentNode.parentNode.style.display=="none")
                return "";
        }
    var builderNodes=(new jcf.parser.ScriptParser()).parseToNodes(express);
    var expr=new jcf.lang.StringBuffer();
    var variant="";
    for(var n=0;n<builderNodes.length;n++){
        var node=builderNodes[n];
        var text=node.getText();
        if (node.getTagType()==jcf.parser.INode.TAGTYPE_MACRO){
            if (text.indexOf("[]")>0){
                expr.append(getNodeValue(document.getElementsByName(text.substring(0,text.length-2))[i]));
            }else if (text.indexOf("[0]")>0){
                if(getNodeValue(document.getElementsByName(text.substring(0,text.length-3))[i])=="")
                    expr.append("0");
                else
                    expr.append(document.getElementsByName(text.substring(0,text.length-3))[i].value);
            } else if (text.indexOf("\(\)")>0) {
                variant+=" var "+text.substring(0,text.length-4)+"=document.getElementsByName(\""+text.substring(0,text.length-4)+"\")["+i.toString()+"];";
                expr.append(text.substring(0,text.length-4));
            }else if (text.indexOf("\(i\)")>0) {
                expr.append("document.getElementsByName(\""+text.substring(0,text.length-3)+"\")["+i.toString()+"]");
            }else{
                expr.append(getNodeValue(document.getElementsByName(text)[0]));
            }
        } else if (node.getTagType()==jcf.parser.INode.TAGTYPE_EXPR){
            if (text.indexOf("[]")>0)
                expr.append(getNodeValue(document.getElementsByName(text.substring(0,text.length-2))[i]));
            else if (text.indexOf("[0]")>0){
                if(getNodeValue(document.getElementsByName(text.substring(0,text.length-3))[i])=="")
                    expr.append("0");
                else
                    expr.append(getNodeValue(document.getElementsByName(text.substring(0,text.length-3))[i]));
            }else if (text.indexOf("\\(\\)")>0) {
                variant+=" var "+text.substring(0,text.length-4)+"=document.getElementsByName(\""+text.substring(0,text.length-4)+"\")["+i.toString()+"];";
                expr.append(text.substring(0,text.length-4));
            } else{
                expr.append(getNodeValue(document.getElementsByName(text)[0]));
            }
        } else {
            expr.append(node.getText());
        }
    }
    expr=variant+expr;
    return expr;
}
/**
 *@memo 验证控件的表达式
 **/
DataValidator.prototype._checkExpress=function(checkObj,rule,i){
    if (typeof(rule.express)=="undefined" || rule.express=="") return true;
    if (checkObj.parentNode!=null)
        if (checkObj.parentNode.parentNode!=null && typeof(checkObj.parentNode.parentNode.style)!="undefined"){
            if (checkObj.parentNode.parentNode.style.display=="none")
                return true;
        }
    var expr=this._parseExpress(rule.express,checkObj,rule,i);
    var booleanResult=ExecScript(expr.toString());
    if (booleanResult==false ){
        if(typeof(rule.hintMsg)!="undefined" && rule.hintMsg!=""){
            this._setErrMessage(rule,rule.hintMsg);
            window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
            return false;
        } else {
            return -1;
        }
    }
    return true;
}
/**
 *@memo 验证前提的表达式
 **/
DataValidator.prototype._checkPremissExpress=function(checkObjs,rule,i){
    if (typeof(rule.validatePremiss)=="undefined" || rule.validatePremiss=="") return true;
    if (checkObjs.parentNode!=null)
        if (checkObjs.parentNode.parentNode!=null && typeof(checkObjs.parentNode.parentNode.style)!="undefined"){
            if (checkObjs.parentNode.parentNode.style.display=="none")
                return true;
        }
    var expr=this._parseExpress(rule.validatePremiss,checkObjs,rule,i);
    var checkObj=checkObjs;
    var booleanResult=ExecScript(expr.toString());
    alert(booleanResult);
    if (booleanResult==false ){
        return false;
    }
    return true;
}
DataValidator.prototype._checkType=function(checkObj,rule,i){
    var vtype = "string";
    if (typeof(rule.type)!=undefined)
        vtype=rule.type;
    switch(vtype){
        case ValidateRule.TYPE_INT:
            if (validateUtil.checkIsInteger(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的整数！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_POSITIVEINT:
            if (validateUtil.checkIsPlusInteger(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的正整数！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_FLOAT:
            if (validateUtil.isFloat(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的浮点数！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_DIGIT:
            if (validateUtil.isDigit(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的数字！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_POSITIVEDOUBlE:
            if (validateUtil.checkIsPlusDouble(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的正数！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_DOUBlE:
            if (validateUtil.checkIsDouble(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的数字！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_EMAIL:
            if (validateUtil.checkEmail(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的邮箱！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_DATE:
            if (validateUtil.checkIsValidDate(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的日期格式(yyyy-MM-dd)！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_DATETIME:
            if (validateUtil.isDateTimeYMD(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的日期时间格式(yyyy-MM-dd hh:mm:ss)！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_TIME:
            if (validateUtil.isTime(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的时间格式(hh:mm:ss)！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_URL:
            if (validateUtil.isURL(checkObj.value)==false){
                this._setErrMessage(rule,rule.labelName+"不是有效的url格式！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_IP:
            if (validateUtil.isIP(checkObj.value)==false){
                this._setErrMessage(rule,"不是有效的IP地址！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_MOBILECN:
            if (validateUtil.isMobileCN(checkObj.value)==false){
                this._setErrMessage(rule,"不是有效的移动号码！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_TEL:
            if (validateUtil.isTel(checkObj.value)==false){
                this._setErrMessage(rule,"不是有效的固定电话号码！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_IDCARD:
            if (validateUtil.checkIDCard(checkObj.value)==false){
                this._setErrMessage(rule,"不是有效的身份证号码！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
        case ValidateRule.TYPE_POSTCODECN:
            if (validateUtil.isPostalCodeCN(checkObj.value)==false){
                this._setErrMessage(rule,"不是有效的邮政编码！");
                window.setTimeout(function(){try{ checkObj.focus();}catch (e){}},0);
                return false;
            }
            break;
    }
    return true;
}

function ValidateUtil(){
    this.sum=function(express,controlName,range){
        if (express=="") return true;
        var scriptParser=new jcf.parser.ScriptParser();
        if (typeof(controlName)=="undefined" || controlName==null){
            var builderNodes=scriptParser.parseToNodes(express);
            for(var n=0;n<builderNodes.length;n++){
                var node=builderNodes[n];
                controlName=node.getText();
                if (controlName.indexOf("[]")>0)
                    controlName=controlName.substring(0,controlName.length-2)
                else if (controlName.indexOf("\\(\\)")>0)
                    controlName=controlName.substring(0,controlName.length-4);
                break;
            }
        }
        var resultValue=0;
        var builderNodes=scriptParser.parseToNodes(express);
        var controls=null;
        if (typeof(range)!="undefined")
        {
            controls=new Array();
            scriptParser.getChildControls(range,controlName,controls);
        } else
            controls=document.getElementsByName(controlName);
        var contextMap=new jcf.util.Map();
        contextMap.put(controlName,controls);
        for(var i=0;i<controls.length;i++){
            var expr=new jcf.lang.StringBuffer();
            var variant="";
            for(var n=0;n<builderNodes.length;n++){
                var node=builderNodes[n];
                var text=node.getText();
                if (node.getTagType()==jcf.parser.INode.TAGTYPE_MACRO){
                    if (text.indexOf("[]")>0){
                        expr.append(document.getElementsByName(text.substring(0,text.length-2))[i].value);
                    }else if (text.indexOf("\\(\\)")>0) {
                        variant+=" var "+text.substring(0,text.length-4)+"=document.getElementsByName(\""+text.substring(0,text.length-4)+"\")["+i.toString()+"];";
                        expr.append(text.substring(0,text.length-4));
                    }else
                        expr.append(document.getElementsByName(text)[0].value);
                } else if (node.getTagType()==jcf.parser.INode.TAGTYPE_EXPR){
                    if (text.indexOf("[]")>0)
                        expr.append(document.getElementsByName(text.substring(0,text.length-2))[i].value);
                    else if (text.indexOf("\\(\\)")>0) {
                        variant+=" var "+text.substring(0,text.length-4)+"=document.getElementsByName(\""+text.substring(0,text.length-4)+"\")["+i.toString()+"];";
                        expr.append(text.substring(0,text.length-4));
                    } else
                        expr.append(document.getElementsByName(text)[0].value);
                } else {
                    expr.append(node.getText());
                }
            }
            expr=variant+expr;
            var booleanResult=ExecScript(expr.toString());
            resultValue=resultValue+booleanResult;
        }
        return resultValue;
    }
    this.checkIDCard=function(StrNumber){
        //判断身份证号码格式函数
        //公民身份号码是特征组合码，
        //排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码
        //身份证号码长度判断
        if(StrNumber.length<15||StrNumber.length==16||StrNumber.length==17||StrNumber.length>18){
            CheckIDCard =false;
        }
        //身份证号码最后一位可能是超过100岁老年人的X
        //所以排除掉最后一位数字进行数字格式测试
        //全部换算成17位数字格式
        var Ai;
        if(StrNumber.length==18){
            Ai = StrNumber.substring(0,17);
        } else {
            Ai =StrNumber.substring(0,6)+"19"+StrNumber.substring(6,9);
        }
        if(this.IsNumeric(Ai)==false){
            return false;
        }
        var strYear,strMonth,strDay,strBirthDay;
        strYear = parseInt(Ai.substring(Ai,6,4));
        strMonth = parseInt(Ai.substring(Ai,10,2)) ;
        strDay = parseInt(Ai.substring(Ai,12,2));
        if (this.checkIsValidDate(strYear+"-"+strMonth+"-"+strDay)==false){
            return false;
        }
        var arrVerifyCode = new Array("1","0","x","9","8","7","6","5","4","3","2");
        var Wi = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2);
        var i,TotalmulAiWi=0;
        for (i=0; loop<16;loop++){
            TotalmulAiWi= TotalmulAiWi + parseInt(Ai.substring(i+1,1)) * Wi[i];
        }
        var modValue =TotalmulAiWi%11 ;
        var strVerifyCode = arrVerifyCode[modValue];
        Ai = Ai & strVerifyCode;
        if((StrNumber.length== 18)&&(StrNumber!=Ai)){
            return false;
        }
    }
    this.IsNumeric=function(oNum){
        if(!oNum) return false;
        var strP=/^d+(.d+)?$/;
        if(!strP.test(oNum)) return false;
        try{
            if(parseFloat(oNum)!=oNum) return false;
        }catch(ex) {
            return false;
        }
        return true;
    }

    this.validateActiveComp=function (compName,validityMsg,expressPattern){
        var leftSign="\${";
        var rightSign="}";
        var compValue=0;
        if (typeof(expressPattern)=="undefined")
            expressPattern="";
        var validateComp=document.getElementById(compName);
        compValue=validateComp.value;
        if (expressPattern.indexOf(leftSign+compName+rightSign)>=0) {
            var expressPatternTmp=expressPattern.replace(leftSign+compName+rightSign,compValue);
            var booleanResult=eval(expressPatternTmp);
            if ( booleanResult==false){
                try{
                    validateComp.focus();
                }catch(e){}
                alert(validityMsg);
                return false;
            }
        }  else {
            if(validateComp.value==""){
                try{
                    validateComp.focus();
                }catch(e){}
                alert(validityMsg);
                return false;
            }
        }
        return true;
    }


    this.alidateArrayActiveComp=function (arrayCompName,validityMsg,expressPattern,leastOneArrayComp){
        var arrayComp=document.getElementsByName(arrayCompName);
        var leftSign="\${";
        var rightSign="}";
        var compValue=0;
        if (typeof(leastOneArrayComp)=="undefined")
            leastOneArrayComp=false;
        if (typeof(expressPattern)=="undefined")
            expressPattern="";
        var existsOneComp=false;
        var needReplace=false;
        if (expressPattern!=null && expressPattern.indexOf(leftSign+arrayCompName+rightSign)>=0)
            needReplace=true;
        for (var i=0;i<arrayComp.length;i++) {
            if (typeof(arrayComp[i].disabled)!="undefined"){
                if (arrayComp[i].getAttribute("disabled")==true)
                    continue;
            }
            if (typeof(arrayComp[i].parentNode.parentNode)!="undefined"){
                if (arrayComp[i].parentNode.parentNode.style.display=="none"){
                    continue;
                }
            }
            existsOneComp=true;
            if (needReplace){
                compValue=arrayComp[i].value;
                var expressPatternTmp=expressPattern.replace(leftSign+arrayCompName+rightSign,compValue);
                var booleanResult=eval(expressPatternTmp);
                if ( booleanResult==false){

                    arrayComp[i].focus();

                    alert(validityMsg);
                    return false;
                }
            } else {
                if(arrayComp[i].value==""){

                    arrayComp[i].focus();

                    alert(validityMsg);
                    return false;
                }
            }
        }
        if (leastOneArrayComp==true){
            if (existsOneComp==false){
                alert(validityMsg);
                return false;
            }
        }

        return true;
    }
    this.validateArrayActiveComp=function (arrayCompName,validityMsg,expressPattern,leastOneArrayComp){
        return this.alidateArrayActiveComp(arrayCompName,validityMsg,expressPattern,leastOneArrayComp);
    }
    this.calculateFormulaBooleanValue=function (trNode,formula){
        try{
            var leftSign="\${";
            var rightSign="}";
            var returnValue="0";
            var compValue=0;
            for (var i=0;i<trNode.childNodes.length;i++){
                if (trNode.childNodes[i].innerHTML!="" && trNode.childNodes[i].hasChildNodes()==true){
                    for (var k=0;k<trNode.childNodes[i].childNodes.length;k++){
                        if (typeof(trNode.childNodes[i].childNodes[k].name)=="undefined") continue;
                        if (typeof(trNode.childNodes[i].childNodes[k].disabled)!="undefined")
                        {
                            if (trNode.childNodes[i].childNodes[k].getAttribute("disabled")==true)
                            {
                                continue;
                            }
                        }
                        var compName=trNode.childNodes[i].childNodes[k].name;
                        if (formula.indexOf(leftSign+compName+rightSign)>=0){
                            compValue=trNode.childNodes[i].childNodes[k].value;
                            if (compValue=="" || isNaN(compValue)){
                                compValue=0;
                            }
                            formula=formula.replace(leftSign+compName+rightSign,compValue);
                        }
                    }
                }
            }
            compValue=eval(formula);
            if (isFinite(compValue)==false)
                return false;
            return compValue;
        } catch(err) {
            return false;
        }
    }
    this.validateArrayCompFormula=function (arrayCompName,validityMsg,expressPattern,leastOneArrayComp){
        var arrayComp=document.getElementsByName(arrayCompName);
        var leftSign="\${";
        var rightSign="}";
        var compValue=0;
        if (typeof(leastOneArrayComp)=="undefined")
            leastOneArrayComp=false;
        if (typeof(expressPattern)=="undefined")
            expressPattern="";
        var needReplace=true;
        if (expressPattern=="")
            needReplace=false;
        var existsOneComp=false;

        for (var i=0;i<arrayComp.length;i++) {
            if (typeof(arrayComp[i].disabled)!="undefined")
            {
                if (arrayComp[i].getAttribute("disabled")==true)
                    continue;
            }
            if (arrayComp[i].parentNode.parentNode.style.display=="none"){
                continue;
            }
            existsOneComp=true;
            if (needReplace){
                if (this.calculateFormulaBooleanValue(arrayComp[i].parentNode.parentNode,expressPattern)==false){
                    arrayComp[i].focus();
                    alert(validityMsg);
                    return false;
                }

            } else {
                if(arrayComp[i].value==""){
                    arrayComp[i].focus();
                    alert(validityMsg);
                    return false;
                }
            }
        }
        if (leastOneArrayComp==true){
            if (existsOneComp==false){
                alert(validityMsg);
                return false;
            }
        }
        return true;
    }

    this.JHshNumberText=function (){
        if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
            || (window.event.keyCode == 13) || (window.event.keyCode == 46)
            || (window.event.keyCode == 45)))
        {
            window.event.keyCode = 0 ;
        }
    }

    /**
     * 验证只能输入数字，支持谷歌、火狐
     */
    this.JHshNumberboxText=function (obj){
        //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    }

    /**
     * 控制只能输入两位小数的金额
     * @param obj
     * @return
     */
    this.checkMoneyText = function(obj) {
        if(/^\d+\.?\d{0,2}$/.test(obj.value)){
            obj.value = obj.value;
        }else{
            obj.value = obj.value.substring(0,obj.value.length-1);
        }
    }

    /**
     * 控制输入为正整数
     * @param obj
     * @return
     */
    this.JHshNumberInteger = function (obj) {
        var temp = obj.value.charAt(0);
        //先把非数字的都替换掉
        obj.value = obj.value.replace(/[^\d]/g,"");
//		//如果第一位是负号，则允许添加
//		if(temp == '-') {
//			obj.value = '-'+obj.value; 
//		}
    }

    this.checkIsInteger=function (str){
        if(str == "")
            return true;
        if(/^(\-?)(\d+)$/.test(str))
            return true;
        else
            return false;
    }
    this.checkIsPlusInteger=function (str){
        if(str == "")
            return true;
        if(/^(\-?)(\d+)$/.test(str)){
            if(parseFloat(str)>=0)
                return true;
            else
                return false;
        }else
            return false;
    }
    this.checkIsDouble=function (str){
        if(str == "")
            return true;
        if(str.indexOf(".") == -1)
        {
            if(this.checkIsInteger(str) == true)
                return true;
            else
                return false;
        }
        else {
            str= str.replace(/,/g,"");
            if(/^(\-?)(\d+)(.{1})(\d+)$/g.test(str)){
                return true;
            }else
                return false;
        }
    }
    this.checkIsPlusDouble=function (str){
        if(str == "")
            return true;
        if(str.indexOf(".") == -1)
        {
            if(this.checkIsInteger(str) == true){
                if(parseFloat(str)>=0){
                    return true;
                }else
                    return false;
            }else
                return false;
        }
        else
        {
            str= str.replace(/,/g,"");
            if(/^(\-?)(\d+)(.{1})(\d+)$/g.test(str)){
                if(parseFloat(str)>=0)
                    return true;
                else
                    return false;
            } else
                return false;
        }
    }
    /** *是否是浮点数 **/
    this.isFloat=function (s){
        var patrn = /^-?\d*.?\d+$/;
        return patrn.test(s);
    }
    /**URL*/
    this.isURL=function (s){
        var patrn = /^http:\/\/([\w-]+(\.[\w-]+)+(\/[\w-   .\/\?%&=\u4e00-\u9fa5]*)?)?$/;
        return patrn.test(s);
    }
    /**
     *日期时间 yyyy/mm/dd hh:mm:ss 或 yyyy-mm-dd hh:mm:ss
     *Date Regex author:Michael Ash
     *Modified by Shaw Sunkee
     *支持1600年以后
     */
    this.isDateTimeYMD=function(s){
        var patrn = /^(?:(?:(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(\/|-|\.)(?:0?2\1(?:29)))|(?:(?:(?:1[6-9]|[2-9]\d)?\d{2})(\/|-|\.)(?:(?:(?:0?[13578]|1[02])\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\2(29|30))|(?:(?:0?[1-9])|(?:1[0-2]))\2(?:0?[1-9]|1\d|2[0-8]))))[ ]([0-1]?[0-9]|[2][0-3]):([0-5]?[0-9]):([0-5]?[0-9])$/;;
        return patrn.test(s);
    }
    /**
     *时间
     *hh:mm:ss 24小时制 0 ~ 23 hour
     */
    this.isTime=function (s){
        var patrn = /^([0-1]?[0-9]|[2][0-3]):([0-5]?[0-9]):([0-5]?[0-9])$/;
        return patrn.test(s);
    }
    /**
     * 电话号码
     * 必须以数字开头，除数字外，可含有"-"
     **/
    this.isTel=function (s){
        var patrn=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
        return patrn.test(s);
    }

    /**
     * 中国大陆地区手机号码
     * 以13或15开头，使用时请根据变化修改
     * 校验普通电话，除数字外，可用"-"或空格分开
     **/
    this.isMobileCN=function (s){
        var patrn = /^1[3|5]{1}[0-9]{1}[-| ]?\d{8}$/;
        return patrn.test(s);
    }

    /**
     * 中国地区邮编
     ***/
    this.isPostalCodeCN=function (s){
        var patrn=/^[1-9]\d{5}$/;
        return patrn.test(s);
    }
    /**
     * IP
     **/
    this.isIP=function (s) {
        var patrn=/^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/;
        return patrn.test(s);
    }
    this.checkIsValidDate=function (str){ //format:yyyy-MM-dd
        if(str == "")
            return true;
        var pattern = /^((\d{4})|(\d{2}))-(\d{1,2})-(\d{1,2})$/g;
        if(!pattern.test(str))
            return false;

        var arrDate = str.split("-");
        if(parseInt(arrDate[0],10) < 100)
            arrDate[0] = 2000 + parseInt(arrDate[0],10) + "";
        var date =  new Date(arrDate[0],(parseInt(arrDate[1],10) -1)+"",arrDate[2]);

        if (date.format("yyyy-MM-dd")==str)
            return true;
        else
            return false;

    }

    this.checkEmail=function (str)
    {
        if(str == "")
            return true;
        if (str.charAt(0) == "." || str.charAt(0) == "@" || str.indexOf('@', 0) == -1
            || str.indexOf('.', 0) == -1 || str.lastIndexOf("@") == str.length-1 || str.lastIndexOf(".") == str.length-1)
            return false;
        else
            return true;
    }
    /**
     *校验两个日期的先后
     *返回值：
     *如果其中有一个日期为空，校验通过,          返回true
     *如果起始日期早于等于终止日期，校验通过，   返回true
     *如果起始日期晚于终止日期，                 返回false    参考提示信息： 起始日期不能晚于结束日期。
     */

    this.checkDateEarlier=function (strStart,strEnd)
    {
        if(this.checkIsValidDate(strStart) == false || this.checkIsValidDate(strEnd) == false)
            return false;
        if (( strStart == "" ) || ( strEnd == "" ))
            return true;
        var arr1 = strStart.split("-");
        var arr2 = strEnd.split("-");
        var date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
        var date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
        if(arr1[1].length == 1)
            arr1[1] = "0" + arr1[1];
        if(arr1[2].length == 1)
            arr1[2] = "0" + arr1[2];
        if(arr2[1].length == 1)
            arr2[1] = "0" + arr2[1];
        if(arr2[2].length == 1)
            arr2[2]="0" + arr2[2];
        var d1 = arr1[0] + arr1[1] + arr1[2];
        var d2 = arr2[0] + arr2[1] + arr2[2];
        if(parseInt(d1,10) > parseInt(d2,10))
            return false;
        else
            return true;
    }

    /**
     *校验字符串是否为中文
     *返回值：
     *如果为空，定义校验通过，           返回true
     *如果字串为中文，校验通过，         返回true
     *如果字串为非中文，             返回false    参考提示信息：必须为中文！
     */
    this.checkIsChinese=function (str){
        if (str == "")
            return true;
        var pattern = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])*$/gi;
        if (pattern.test(str))
            return true;
        else
            return false;
    }

    /**
     * 计算字符串的长度，一个汉字两个字符
     */
    this.stringRealLength = function(str){
        return str.replace(/[^\x00-\xff]/g,"**").length;
    }

    /**
     *校验字符串是否符合自定义正则表达式
     *str 要校验的字串  pat 自定义的正则表达式
     *返回值：
     *如果为空，定义校验通过，           返回true
     *如果字串符合，校验通过，           返回true
     *如果字串不符合，                   返回false    参考提示信息：必须满足***模式
     */
    this.checkMask=function(str,pat){
        if (str == "")
            return true;
        var pattern = new RegExp(pat,"gi")
        if (pattern.test(str))
            return true;
        else
            return false;
    }
    /* 检测字符串是否全为数字 */
    this.isNumber=function(str){
        var number_chars = "1234567890";
        var i;
        for (i=0;i<str.length;i++)
        {
            if (number_chars.indexOf(str.charAt(i))==-1) return false;
        }
        return true;
    }
    /**
     *是否数字
     */
    this.isDigit=function(s){
        var patrn=/^-?[0-9]{1,10}$/;
        return patrn.test(s);
    }

}
var validateUtil=new ValidateUtil();

/*commentsArray 页面备注名称及验证长度集合
 *如：[{name : "comments1", length : 200, callBackFun : null},{name : "comments2", length : 200, callBackFun : null}];
 */
function CommentsValidater(){
    this.defaultCommentsSetting = [{name : "comments", length : 10, callBackFun : null,hintContainer:null}];
    this.defaultCommertValidateFun = null;


}
CommentsValidater.prototype={
    init : function (){
        if(typeof(commentsSetting)!="undefined"){
            this.defaultCommentsSetting = commentsSetting;
        }

        for(var i=0;i<this.defaultCommentsSetting.length;i++){
            var name = this.defaultCommentsSetting[i]["name"];
            var length = this.defaultCommentsSetting[i]["length"];
            var callBackFun = this.defaultCommentsSetting[i]["callBackFun"];
            var hintContainer=this.defaultCommentsSetting[i]["hintContainer"];
            var nodes = document.getElementsByName(name);

            for(var j=0;j<nodes.length;j++){
                var txtNode = document.createTextNode("字符长度请小于"+length);

                if (hintContainer==null)
                    nodes[j].parentNode.appendChild(txtNode);
                else {

                    if (typeof(hintContainer)!="string")
                        hintContainer.appendChild(txtNode);
                }
                if(callBackFun==null)
                    nodes[j].attachEvent("onblur",this.validateCallBackFun);
                else
                    nodes[j].attachEvent("onblur",callBackFun);
            }
        }
    },
    getLengthByName : function(str){

        for(var i=0;i<this.defaultCommentsSetting.length;i++){
            var name = this.defaultCommentsSetting[i]["name"];
            var length = this.defaultCommentsSetting[i]["length"];
            if(str==name){
                return length;
            }
        }
    },
    validateCallBackFun : function(event){
        var node = event.srcElement;
        var str = node.getAttribute("name");
        var length = commentsValidater.getLengthByName(str);
        if(validateLib.stringRealLength(node.value)>length){
            alert("字符长度大于"+length+",请重新输入");
            //node.className="errorTxtBox";
            node.focus();
            return false;
        }else{
            return true;
        }

    }

}
commentsValidater = new  CommentsValidater();
commentsValidater.init();
