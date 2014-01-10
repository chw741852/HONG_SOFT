/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-27
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
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
    setTheme:function(obj, section, theme, defaultTheme, cssName) {
        if (theme == undefined) {
            theme = defaultTheme;
        }
        var url = $(obj).attr("href");
        var href = url.substring(0, url.indexOf(section)) + section + "/" + theme + "/" + cssName;
        $(obj).attr("href", href);
    }
}