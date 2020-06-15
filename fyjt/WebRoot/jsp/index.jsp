<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="../WEB-INF/fyjttag.tld" prefix="p"%>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<c:set var="path" value="${pageContext.request.contextPath }" ></c:set>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>峰岩集团</title>
		<link rel="stylesheet" type="text/css" href="js/css/tree.css">
	</head>
	<style type="text/css">
		*{
			touch-action: pan-y;
		}
		.xcConfirm .popBox {width:450px;}
		.xcConfirm .popBox .txtBox p {
	    	margin-top: -4px;
	    	width:230px;
	    }
	   	.bx-wrapper{ max-width: 100% !important;}
	   	.bx-wrapper .bx-pager{display: none !important;}
  	</style>
		<!-- header -->
		<%@include file="header.jsp"%>
	<body>
			
		<!-- 轮播图-->
		<div id="slider1_container" style="position: relative; margin: 0 auto;  top: 0px; left: 0px; width: 1300px; height: 612px; overflow: hidden;">
	        <div u="slides" style="cursor: move; position: absolute; left: 0px; top: 0px; width: 1300px;height: 612px; overflow: hidden;">
	             <c:forEach var="t" items="${listcarousel}">
	             		    <div>
				                <img u="image" src="${t.imgurl }" />
				            </div>
	              </c:forEach>
	        </div>
	        <div u="navigator" class="jssorb21" style="position: absolute; bottom: 26px; left: 6px;">
	            <!-- bullet navigator item prototype -->
	            <div u="prototype" style="POSITION: absolute; WIDTH: 19px; HEIGHT: 19px; text-align:center; line-height:19px; color:White; font-size:12px;"></div>
	        </div>
	       
	        <!-- Arrow Left -->
	        <span u="arrowleft" class="jssora21l" style="width: 55px; height: 55px; top: 123px; left: 8px;">
	        </span>
	        <!-- Arrow Right -->
	        <span u="arrowright" class="jssora21r" style="width: 55px; height: 55px; top: 123px; right: 8px">
	        </span>
	    </div>
		<div class="content">
			<div class="f2"></div>
			<div class="ggzy">
				<div class="title hs">
						<div class="t">企业介绍</div>
						<!-- <div class="m"><a href="meeting/listMeeting">更多>></a></div> -->
				</div>
				<div  style="padding: 20px;font-size:18px;">
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平遥峰岩集团有限公司位于山西省平遥县中都乡西胡村（峰岩工业园），创建于1996年。由峰岩公司、兴华煤焦公司、鑫盛煤焦公司整合而成，现有职工3000人，年经营总收入超过20亿元，08年上交地方财政“两税”20127万元。是世界文化遗产——平遥古城的重点企业，纳税大户，并通过了ISO9001：2000质量体系认证。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平遥峰岩集团有限公司以诚信、实力和产品质量获得业界的高度认可。欢迎各界朋友莅临参观、指导和业务洽谈。</p>
				</div>
			</div>
			<div class="content-box">
				<div class="news-dy">
					<div class="newsDy-list clearfix" id="slickNewsList">
						<div class="newsDy-left">
							<div class="m-con">
								<div class="title jh">
									<div class="t">内部公告</div>
									<div class="m"><a href="notice/noticeList">更多>></a></div>
								</div>
								<a  class="mcon-b"> <img class="fl"	src="img/jsp/default_news.jpg" width="170px" height="170px"></a>
								<div class="m-conart">
									<a class="ahover" >内部公告：</a>
										<p class="mcdet">
											<span style="font-size:18px;">
											 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;主要用于向员工宣传公司管理制度及相关政策法规，方便员工了解公司规章制度，自觉执行。
											 </span>
										</p>
								</div>
								<div class="clear"></div>
							</div>
							
							<c:forEach var="n" items="${news }" >
								<fmt:formatDate var="ndate" value="${n.update_time }" pattern="yyyy-MM-dd"/>
								<ul class="m01-ul">
									<li><i>●</i><a class="ahover"
									href="notice/noticeDetail/${n.id }">${n.title }</a>
									<span>${ndate }</span></li>
								</ul>
							</c:forEach>
						</div>
						<div class="newsDy-right">
							<div class="m-con">
								<div class="title qs">
									<div class="t">暂无内容</div>
									<div class="m"><a href="specialFunds/list">更多>></a></div>
								</div>
								<a  class="mcon-b"> <img class="fl" src="img/jsp/yuanquxiaoguo.jpg" width="170px" height="170px"></a>
								<div class="m-conart">
									<a class="ahover">暂无内容</a>
									<p class="mcdet">
											<span style="font-size:18px;">
											 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;暂无提示。
											 </span>
										</p>
								</div>
								<div class="clear"></div>
							</div>
							<c:forEach var="sf" items="${sfList }" begin="1" end="5">
								<fmt:formatDate var="sfdate" value="${sf.createTime }" pattern="yyyy-MM-dd"/>
								<ul class="m01-ul">
									<li><i>●</i><a class="ahover"
										href="specialFunds/sq?id=${sf.id }">${sf.title }</a>
										<span>${sfdate }</span></li>
								</ul>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="service">
			<div class="slogan"></div>
			<div class="service-box">
				
				<ul class="service-ul ul-top" style="margin-bottom:0px;">
					<li>
						<div><a href="reservation/business_detail/1" class="ser service1"></a></div>
						<div class="apply"><a href="reservation/business_detail/1" class="ser-name">入职申请</a></div>
						<div><a class="ser-detail">是员工在入职前必须办理的手续</a></div>
					</li>
					<li>
						<div><a href="reservation/business_detail/2" class="ser service2"></a></div>
						<div class="apply"><a href="reservation/business_detail/2" class="ser-name">出差申请</a></div>
						<div><a class="ser-detail">是员工在出差前必须办理的手续</a></div>
					</li>
					<li>
						<div><a href="reservation/business_detail/3" class="ser service3"></a></div>
						<div class="apply"><a href="reservation/business_detail/3" class="ser-name">物品申请</a></div>
						<div><a class="ser-detail">是员工在领取物品前必须办理的手续</a></div>
					</li>
					<li style="margin-right:0px;">
						<div><a href="reservation/business_detail/4" class="ser service4"></a></div>
						<div class="apply"><a href="reservation/business_detail/4" class="ser-name">离职申请</a></div>
						<div><a class="ser-detail">是员工在离职前必须办理的手续</a></div>
					</li>
				</ul>
				<ul class="service-ul" style="margin-top:40px;clear:both;">
					<li>
						<div><a onclick="estateRepair(1);" href="javascript:void(0);" class="ser service7"></a></div>
						<div class="apply"><a onclick="estateRepair(1);" href="javascript:void(0);" class="ser-name">物业报修</a></div>
						<div><a class="ser-detail">员工根据物业维修范围进行物业报修</a></div>
					</li>
					<li>
						<div><a href="estate/complaint" class="ser service8"></a></div>
						<div class="apply"><a href="estate/complaint" class="ser-name">投诉处理</a></div>
						<div><a class="ser-detail">员工提交投诉后，对员工进行的物业投诉进行处理</a></div>
					</li>
					<li>
						<div><a href="reservation/business_detail/12" class="ser service9"></a></div>
						<div class="apply"><a href="reservation/business_detail/4" class="ser-name">合同签订</a></div>
						<div><a class="ser-detail">各个部门签了合同后在此备注</a></div>
					</li>
					<li style="margin-right:0px;">
						<div><a href="reservation/hall" class="ser more"></a></div>
						<div class="apply"><a href="estate/complaint" class="ser-name">查看更多</a></div>
						<div><a class="ser-detail">点击查看更多，进入办事大厅</a></div>
					</li>
				</ul>
			</div>
		</div>
		
		<%-- <div class="big-img">
			<div class="center">
				<c:forEach var="se" items="${settledEnterprises }">
					<a href="admin/settle/info?id=${se.id }" style="${se.position}"><img src="${se.circularLogo }"  /></a>
				</c:forEach>
				<div class="rzqy-list">
					<a href="ipop/settledEnterPrise/list" ><p:configTag mark="rzqy"/></a>
				</div>
				
			</div>
		</div> --%>
		<div class="partners">
			<div class="f3"></div>
			<div class="partners-content">
			    <div class="slider1">
			    	<c:forEach var="settle" items="${cooperationList }">
						<a href="${settle.link_url }" target="_blank">
							<div class="slide"><img src="${settle.dark_img_url }" default_img="${settle.dark_img_url} " hidden_img="${settle.light_img_url }"></div>
						</a>
					</c:forEach>
			    </div>
			</div>
		</div>
		<%@include file="footer.jsp"%>	
		</body>
	<script type="text/javascript" src="js/jsp/lanren.js"></script>
	<script src="js/Swiper-3.3.0/Swiper-3.3.0/dist/js/swiper.jquery.min.js"></script>
    <script type="text/javascript">
	    $(document).ready(function(){
	        $('.slider1').bxSlider({
	      	displaySlideQty: 4,
	      	moveSlideQty: 4,
	          slideWidth: 290,
	          minSlides: 2,
	          maxSlides: 4,
	          slideMargin: 10,
	          infiniteLoop:false
	        });
	      });
    
    	var basePath = "<%=basePath%>";
		$('.slide img').on('mouseover',function(){
			var hightImg = $(this).attr('hidden_img');
			$(this).attr('src',hightImg);
		});
		
		$('.slide img').on('mouseout',function(){
			var defaultImg = $(this).attr('default_img');
			$(this).attr('src',defaultImg);
		});
		
		jQuery(document).ready(function ($) {

            var _CaptionTransitions = [];
            _CaptionTransitions["L"] = { $Duration: 900, $FlyDirection: 1, $Easing: { $Left: $JssorEasing$.$EaseInOutSine }, $ScaleHorizontal: 0.6, $Opacity: 2 };
            _CaptionTransitions["R"] = { $Duration: 900, $FlyDirection: 2, $Easing: { $Left: $JssorEasing$.$EaseInOutSine }, $ScaleHorizontal: 0.6, $Opacity: 2 };
            _CaptionTransitions["T"] = { $Duration: 900, $FlyDirection: 4, $Easing: { $Top: $JssorEasing$.$EaseInOutSine }, $ScaleVertical: 0.6, $Opacity: 2 };
            _CaptionTransitions["B"] = { $Duration: 900, $FlyDirection: 8, $Easing: { $Top: $JssorEasing$.$EaseInOutSine }, $ScaleVertical: 0.6, $Opacity: 2 };
            _CaptionTransitions["ZMF|10"] = { $Duration: 900, $Zoom: 11, $Easing: { $Zoom: $JssorEasing$.$EaseOutQuad, $Opacity: $JssorEasing$.$EaseLinear }, $Opacity: 2 };
            _CaptionTransitions["RTT|10"] = { $Duration: 900, $Zoom: 11, $Rotate: 1, $Easing: { $Zoom: $JssorEasing$.$EaseOutQuad, $Opacity: $JssorEasing$.$EaseLinear, $Rotate: $JssorEasing$.$EaseInExpo }, $Opacity: 2, $Round: { $Rotate: 0.8} };
            _CaptionTransitions["RTT|2"] = { $Duration: 900, $Zoom: 3, $Rotate: 1, $Easing: { $Zoom: $JssorEasing$.$EaseInQuad, $Opacity: $JssorEasing$.$EaseLinear, $Rotate: $JssorEasing$.$EaseInQuad }, $Opacity: 2, $Round: { $Rotate: 0.5} };
            _CaptionTransitions["RTTL|BR"] = { $Duration: 900, $Zoom: 11, $Rotate: 1, $FlyDirection: 10, $Easing: { $Left: $JssorEasing$.$EaseInCubic, $Top: $JssorEasing$.$EaseInCubic, $Zoom: $JssorEasing$.$EaseInCubic, $Opacity: $JssorEasing$.$EaseLinear, $Rotate: $JssorEasing$.$EaseInCubic }, $ScaleHorizontal: 0.6, $ScaleVertical: 0.6, $Opacity: 2, $Round: { $Rotate: 0.8} };
            _CaptionTransitions["CLIP|LR"] = { $Duration: 900, $Clip: 15, $Easing: { $Clip: $JssorEasing$.$EaseInOutCubic }, $Opacity: 2 };
            _CaptionTransitions["MCLIP|L"] = { $Duration: 900, $Clip: 1, $Move: true, $Easing: { $Clip: $JssorEasing$.$EaseInOutCubic} };
            _CaptionTransitions["MCLIP|R"] = { $Duration: 900, $Clip: 2, $Move: true, $Easing: { $Clip: $JssorEasing$.$EaseInOutCubic} };

            var options = {
                $FillMode: 2,                                       //[Optional] The way to fill image in slide, 0 stretch, 1 contain (keep aspect ratio and put all inside slide), 2 cover (keep aspect ratio and cover whole slide), 4 actual size, 5 contain for large image, actual size for small image, default value is 0
                $AutoPlay: true,                                    //[Optional] Whether to auto play, to enable slideshow, this option must be set to true, default value is false
                $AutoPlayInterval: 4000,                            //[Optional] Interval (in milliseconds) to go for next slide since the previous stopped if the slider is auto playing, default value is 3000
                $PauseOnHover: 1,                                   //[Optional] Whether to pause when mouse over if a slider is auto playing, 0 no pause, 1 pause for desktop, 2 pause for touch device, 3 pause for desktop and touch device, default value is 1

                $ArrowKeyNavigation: true,   			            //[Optional] Allows keyboard (arrow key) navigation or not, default value is false
                //$SlideEasing: $JssorEasing$.$EaseOutQuint,          //[Optional] Specifies easing for right to left animation, default value is $JssorEasing$.$EaseOutQuad
                $SlideDuration: 800,                               //[Optional] Specifies default duration (swipe) for slide in milliseconds, default value is 500
                $MinDragOffsetToSlide: 20,                          //[Optional] Minimum drag offset to trigger slide , default value is 20
                //$SlideWidth: 600,                                 //[Optional] Width of every slide in pixels, default value is width of 'slides' container
                //$SlideHeight: 300,                                //[Optional] Height of every slide in pixels, default value is height of 'slides' container
                $SlideSpacing: 0, 					                //[Optional] Space between each slide in pixels, default value is 0
                $DisplayPieces: 1,                                  //[Optional] Number of pieces to display (the slideshow would be disabled if the value is set to greater than 1), the default value is 1
                $ParkingPosition: 0,                                //[Optional] The offset position to park slide (this options applys only when slideshow disabled), default value is 0.
                $UISearchMode: 1,                                   //[Optional] The way (0 parellel, 1 recursive, default value is 1) to search UI components (slides container, loading screen, navigator container, arrow navigator container, thumbnail navigator container etc).
                $PlayOrientation: 1,                                //[Optional] Orientation to play slide (for auto play, navigation), 1 horizental, 2 vertical, 5 horizental reverse, 6 vertical reverse, default value is 1
                $DragOrientation: 1,                                //[Optional] Orientation to drag slide, 0 no drag, 1 horizental, 2 vertical, 3 either, default value is 1 (Note that the $DragOrientation should be the same as $PlayOrientation when $DisplayPieces is greater than 1, or parking position is not 0)

                $CaptionSliderOptions: {                            //[Optional] Options which specifies how to animate caption
                    $Class: $JssorCaptionSlider$,                   //[Required] Class to create instance to animate caption
                    $CaptionTransitions: _CaptionTransitions,       //[Required] An array of caption transitions to play caption, see caption transition section at jssor slideshow transition builder
                    $PlayInMode: 1,                                 //[Optional] 0 None (no play), 1 Chain (goes after main slide), 3 Chain Flatten (goes after main slide and flatten all caption animations), default value is 1
                    $PlayOutMode: 3                                 //[Optional] 0 None (no play), 1 Chain (goes before main slide), 3 Chain Flatten (goes before main slide and flatten all caption animations), default value is 1
                },

                $BulletNavigatorOptions: {                          //[Optional] Options to specify and enable navigator or not
                    $Class: $JssorBulletNavigator$,                 //[Required] Class to create navigator instance
                    $ChanceToShow: 2,                               //[Required] 0 Never, 1 Mouse Over, 2 Always
                    $AutoCenter: 1,                                 //[Optional] Auto center navigator in parent container, 0 None, 1 Horizontal, 2 Vertical, 3 Both, default value is 0
                    $Steps: 1,                                      //[Optional] Steps to go for each navigation request, default value is 1
                    $Lanes: 1,                                      //[Optional] Specify lanes to arrange items, default value is 1
                    $SpacingX: 8,                                   //[Optional] Horizontal space between each item in pixel, default value is 0
                    $SpacingY: 8,                                   //[Optional] Vertical space between each item in pixel, default value is 0
                    $Orientation: 1                                 //[Optional] The orientation of the navigator, 1 horizontal, 2 vertical, default value is 1
                },

                $ArrowNavigatorOptions: {                           //[Optional] Options to specify and enable arrow navigator or not
                    $Class: $JssorArrowNavigator$,                  //[Requried] Class to create arrow navigator instance
                    $ChanceToShow: 1,                               //[Required] 0 Never, 1 Mouse Over, 2 Always
                    $AutoCenter: 2,                                 //[Optional] Auto center arrows in parent container, 0 No, 1 Horizontal, 2 Vertical, 3 Both, default value is 0
                    $Steps: 1                                       //[Optional] Steps to go for each navigation request, default value is 1
                }
            };

            var jssor_slider1 = new $JssorSlider$("slider1_container", options);

            //responsive code begin
            //you can remove responsive code if you don't want the slider scales while window resizes
            function ScaleSlider() {
                var bodyWidth = document.body.clientWidth;
                if (bodyWidth)
                    jssor_slider1.$SetScaleWidth(Math.min(bodyWidth, 1920));
                else
                    window.setTimeout(ScaleSlider, 30);
            }

            ScaleSlider();

            if (!navigator.userAgent.match(/(iPhone|iPod|iPad|BlackBerry|IEMobile)/)) {
                $(window).bind('resize', ScaleSlider);
            }


            //if (navigator.userAgent.match(/(iPhone|iPod|iPad)/)) {
            //    $(window).bind("orientationchange", ScaleSlider);
            //}
            //responsive code end
        });
		function estateRepair(type){
			if(verifyUserLogin("")){
				$.get(basePath+"employee/isOrderRole",function(data){
		    		if(data.code != 0){
		    		window.wxc.xcConfirm(data.msg, window.wxc.xcConfirm.typeEnum.warning);
		    			if(data.code == 2){
		    				window.location.href = basePath+"index/loginredirect?redirect="+window.location.href;
		    			}
		    		}else{
		    			if(type==1){
		    				window.location.href=basePath+'estate/repair';	
		    			}else if(type==2){
		    				window.location.href=basePath+'reservation/business_detail/13';
		    			}    			
		    		}
		    	});
			}
		}
		// true 已登录 false 未登录
		function verifyUserLogin(redirect){
			<%-- var loginFlag = '<%=session.getAttribute(SessionObject.SESSION_KEY) %>'; --%>
			var loginFlag =null;
			if (loginFlag == 'null' || loginFlag == '' || loginFlag == null) {
					var login_page = basePath + "index/loginredirect?redirect="
							+ basePath+redirect;
					location.href = login_page;
				return false;
			} 
			return true;
		}
    </script>
    
    
</html>
