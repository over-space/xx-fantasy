<!DOCTYPE HTML>
<html>
<head>
    <title>Home</title>
    <!-- for-mobile-apps -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="keywords" content="Great Taste Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template, Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design"/>

    <@resource path="${src}/xx-base/bootstrap/css/bootstrap.css, ${src}/xx-blog/css/style.css, ${src}/xx-base/markdown/css/editormd.css"/>
    <@resource path="${src}/xx-base/js/jquery-1.11.1.min.js"/>

    <link href="${src}/xx-blog/favicon.ico" rel="icon" type="image/x-icon" />
</head>

<body>
<!-- banner-body -->
<div class="banner-body">
    <div class="container">
        <div class="banner-body-content">

            <#include "/xx-blog/navbar.ftl"/>

                <div class="col-xs-10 banner-body-right">
                    <div class="banner-bottom border-solid-ddd">
                        <div id="editormd-view">
                            <textarea style="display:none;" name="test-editormd-markdown-doc"></textarea>
                        </div>
                    </div>
                </div>

            <div class="clearfix"></div>
            <#include "/xx-blog/footer.ftl"/>
        </div>
    </div>
</div>
<!-- //banner-body -->

<!-- for bootstrap working -->
<@resource base="${src}/xx-base/bootstrap" path="/js/bootstrap.js"/>
<!-- //for bootstrap working -->

<@resource base="${src}/xx-base/markdown/lib" path="/marked.min.js"/>
<@resource base="${src}/xx-base/markdown/lib" path="/prettify.min.js"/>
<@resource base="${src}/xx-base/markdown/lib" path="/raphael.min.js"/>
<@resource base="${src}/xx-base/markdown/lib" path="/underscore.min.js"/>
<@resource base="${src}/xx-base/markdown/lib" path="/sequence-diagram.min.js"/>
<@resource base="${src}/xx-base/markdown/lib" path="/flowchart.min.js"/>
<@resource base="${src}/xx-base/markdown/lib" path="/jquery.flowchart.min.js"/>

<@resource path="${src}/xx-base/markdown/js/editormd.js"/>

<script type="text/javascript">

    var id = getUrlParam("id");

    $(function() {
        $.get("${path}/xxblog/content/markdown.xhtml?id=" + id, function(markdown) {
            var mdText = markdown.data.content;
            testEditormdView = editormd.markdownToHTML("editormd-view", {
                markdown        : mdText ,//+ "\r\n" + $("#append-test").text(),
                //htmlDecode      : true,       // 开启 HTML 标签解析，为了安全性，默认不开启
                htmlDecode      : "style,script,iframe",  // you can filter tags decode
                //toc             : false,
                tocm            : true,    // Using [TOCM]
                //tocContainer    : "#custom-toc-container", // 自定义 ToC 容器层
                //gfm             : false,
                //tocDropdown     : true,
                // markdownSourceCode : true, // 是否保留 Markdown 源码，即是否删除保存源码的 Textarea 标签
                emoji           : true,
                taskList        : true,
                tex             : true,  // 默认不解析
                flowChart       : true,  // 默认不解析
                sequenceDiagram : true,  // 默认不解析
            });
        });
    });

    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
</script>

</body>
</html>