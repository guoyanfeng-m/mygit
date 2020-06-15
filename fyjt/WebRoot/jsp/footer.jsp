<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<style type="text/css">
	.ewm1{
		position: absolute;
		left: 250px;
		margin-top: -175px;
	}
	.ewm2{
		position: absolute;
		left: 350px;
		margin-top: -110px;
	}
	.hide{
		display: none;
	}
	/* .footer{
		position:fixed;
		left:0px;
		width:100%;
		bottom:38px !important;
		_position:absolute;
	}
	.copyright {
		position:fixed;
		width:100%;
		bottom:0;
	    height: 38px;
	    line-height: 38px;
	    background: #114070;
	    text-align: center;
	    color: #a5b6cb;
	    font-size: 12px;
	} */
</style>
<div class="footer">
	<div class="footer-content">
		
		<div class="footer-left">
		<!-- 	<div class="about"><span class="label">手机客户端下载</span><a href="javascript:;" class="app1">IOS客户端/Android客户端</a><span class="line">/</span><a href="javascript:;" class="app2">android客户端下载</a></div> -->
			<div class="contact-us"><span class="label">联系方式</span>
			<span class="phone-icon"><p:configTag mark="contactPhone" /> </span>
			<span class="fax-icon"><p:configTag mark="contactFax" /></span> 
			</div>
			<div class="contact-us" style="margin-top:20px;"><span class="label">邮箱</span><span class="email-icon"><p:configTag mark="contactEmail" /></span></div>
			</div>
	</div>	
</div>
<!-- <div class="copyright">© 2014-2016 上海新微科技发展有限公司    版权所有  沪ICP备16018908号</div> -->
<div class="copyright">©2020 山西省平遥峰岩集团有限公司  版权所有 </div>
<script type="text/javascript">
	$(function(){
		$('.app1').hover(function(){
			$('.ewm1').toggleClass('hide');
		});
		$('.app2').hover(function(){
			$('.ewm2').toggleClass('hide');
		});
	});
	initFooterWeiZhi();
	//初始化footer位置
	function initFooterWeiZhi(){
		var contentHeight=(getPageSize()[3])
			-$('.logo1:first').height()
			-$('.kw_header:first').height()
			-$(".footer:first").height()
			-$(".copyright:first").height();
		//将中间的box高度置为可用高度
		var hei =contentHeight;
		hei = hei<150?150:hei;
		$(".row:first").css("min-height",hei);
	}
	function getPageSize() {
	    var xScroll, yScroll;
	    if (window.innerHeight && window.scrollMaxY) {
	        xScroll = window.innerWidth + window.scrollMaxX;
	        yScroll = window.innerHeight + window.scrollMaxY;
	    } else {
	        if (document.body.scrollHeight > document.body.offsetHeight) { // all but Explorer Mac    
	            xScroll = document.body.scrollWidth;
	            yScroll = document.body.scrollHeight;
	        } else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari    
	            xScroll = document.body.offsetWidth;
	            yScroll = document.body.offsetHeight;
	        }
	    }
	    var windowWidth, windowHeight;
	    if (self.innerHeight) { // all except Explorer    
	        if (document.documentElement.clientWidth) {
	            windowWidth = document.documentElement.clientWidth;
	        } else {
	            windowWidth = self.innerWidth;
	        }
	        windowHeight = self.innerHeight;
	    } else {
	        if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode    
	            windowWidth = document.documentElement.clientWidth;
	            windowHeight = document.documentElement.clientHeight;
	        } else {
	            if (document.body) { // other Explorers    
	                windowWidth = document.body.clientWidth;
	                windowHeight = document.body.clientHeight;
	            }
	        }
	    }       
	    // for small pages with total height less then height of the viewport    
	    if (yScroll < windowHeight) {
	        pageHeight = windowHeight;
	    } else {
	        pageHeight = yScroll;
	    }    
	    // for small pages with total width less then width of the viewport    
	    if (xScroll < windowWidth) {
	        pageWidth = xScroll;
	    } else {
	        pageWidth = windowWidth;
	    }
	    arrayPageSize = new Array(pageWidth, pageHeight, windowWidth, windowHeight);
	    return arrayPageSize;
	}
	
	$("div.home").click(function(){
		window.location.href="";
	});
</script>