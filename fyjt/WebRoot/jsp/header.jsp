<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="path" value="${pageContext.request.contextPath }" ></c:set>
<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" /> 
<link rel="stylesheet" href="js/css/ui-dialog.css" />
<link rel="stylesheet" href="js/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="js/Swiper-3.3.0/Swiper-3.3.0/dist/css/swiper.min.css" />
<link rel="stylesheet" href="js/css/jquery.slideBox.css"  type="text/css">
<link rel="stylesheet" href="js/css/jquery.bxslider.css"  type="text/css">
<link rel="stylesheet" href="js/css/xcConfirm.css">
<link rel="stylesheet" href="js/jsp/layer/skin/layer.css" type="text/css">
<style type="text/css">
	body{
	font-family: Cambria, Palatino, "Palatino Linotype", "Palatino LT STD", Georgia, serif;
    /* background: #fff url(../images/bg.png) repeat top left; */
    font-weight: 400;
    font-size: 15px;
    color: #333;}
    .layui-layer-dialog .layui-layer-padding {
	    text-align: center;
	}
	#tel_img1{
		float:left;
		margin-top:22px;
	}
	#tel_img2{
		float:left;
		margin-top:22px;
		margin-left:10px;
	}
	.tel_text{
		line-height: 35px;
	    display: block;
	    float: left;
	    margin-top: 15px;
	    font-size: 18px;
	    margin-left: 5px;
	    color: #667A96;
	}
</style>
<script src="js/jquery-1.11.0.min.js"></script>
<script src="js/jsp/jquery.slideBox.min.js"></script>
<script type="text/javascript" src="js/jsp/jquery.bxslider.js"></script>
<script type="text/javascript" src="js/lb/js/jssor.core.js"></script>
<script type="text/javascript" src="js/lb/js/jssor.utils.js"></script>
<script type="text/javascript" src="js/lb/js/jssor.slider.js"></script>
<script type="text/javascript" src="js/jsp/dialog-min.js"></script>
<script type="text/javascript" src="js/jsp/main.js"></script>
<script type="text/javascript" src="js/jsp/xcConfirm.js"></script>
<script type="text/javascript" src="js/jsp/layer/layer.js"></script>
<%
String pathX = request.getContextPath();
String basePathX = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+pathX+"/";
%>
<script type="text/javascript">
	/* var cname; */
       function displaySubMenu(li) {
			/* cname=li.getElementsByTagName("a")[0].className; */
			var subMenu = li.getElementsByTagName("div")[0];
			if(typeof(subMenu) != "undefined"){
				subMenu.style.display = "block";
			}
			/* li.getElementsByTagName("a")[0].className="on"; */
		}
		
       function hideSubMenu(li) {
				var subMenu = li.getElementsByTagName("div")[0];
				if(typeof(subMenu) != "undefined"){
					subMenu.style.display = "none";
				}
        /*    if (cname=="actived"){
			}
			else{
				var subMenu = li.getElementsByTagName("div")[0];
				subMenu.style.display = "none";
			} */
       }
</script>
<div class="middle_login_bg"></div>
<div class="logo1">
	<a href="#"><img class="logo_img" src="img/jsp/LOGO.png" style="height:66px;width:160px"  /></a>
	<div class="tel" style="width:450px;">
		<a><img src="img/jsp/youxiang-icon.png" id="tel_img1" /><span class="tel_text"><p:configTag mark="contactEmail" /></span></a>
		<a><img src="img/jsp/dianhua-icon.png" id="tel_img2" /><span class="tel_text"><p:configTag mark="contactPhone" /></span></a>
	</div>
</div>
<div class="kw_header">
			<div class="header-content">
				<ul class="ky_menu">
		        	<li>
		            	<a href="#">网站首页</a>
		            </li>
		            <c:forEach var="nav" items="${navlist }" varStatus="s"><!-- or nav.name eq '分园风采'  -->
			            <li <c:if test="${fn:length(nav.listNavvo) !=0  }">class="dropdown"</c:if> onmouseover="displaySubMenu(this)" onmouseout="hideSubMenu(this)">
			            	<c:if test="${fn:length(nav.listNavvo) ==0 }"><!-- and nav.name != '分园风采'  -->
	            				<a href="${nav.url }">${nav.name }</a>
	            			</c:if>
	            			<c:if test="${fn:length(nav.listNavvo)!=0  }"><!-- or nav.name eq '分园风采' -->
	            				<a>${nav.name }</a>
	            			</c:if>
	            			
	            			<c:if test="${fn:length(nav.listNavvo)!=0 }">
		                       <div id="nav" <c:if test="${s.index==0 }">style="width: 500px"</c:if> >
		                       		<div class="nav_title">
				                        <c:forEach var="child" items="${nav.listNavvo }" >
						                	<a href="${child.url }?navName=${child.name }">${child.name }</a>
					                    </c:forEach>
				                    </div>
				                    <c:if test="${s.index==0 }">
					                    <div class="nav_img">
					                    		<img  src="img/jsp/nav_1.png" style="height:200px;">
					               		</div>
				               		</c:if>
			               	   </div>
	                        </c:if>
			            </li>
		            </c:forEach> 
		        </ul>
			</div>
		</div>
		
		<div style="width: 100%; height: 110px; background: white;"></div>
<script type="text/javascript">

      var nav = document.getElementById("nav");
      var spans = nav.getElementsByTagName("span");
      
      console.log(spans)
      for(i=0;i<spans.length;i++){
          spans[i].style.display = "none";
      }
      nav.onclick = tree;
      function tree(e){
          e = e||event;
          var target = e.target||e.srcElement;
          console.log(target);
          var next = nextSibling (target);
          if(target.nodeName == "A" && next.nodeName == "SPAN"){
              if(next.style.display == "none"){
                  var otherSpans = sublingsAsTagName(target,"SPAN");
                  for(var i=0;i<otherSpans.length;i++){
                      otherSpans[i].style.display = "none";
                  }
                  var otherAs = sublingsAsTagName(target,"A");
                  for(var i=0;i<otherAs.length;i++){
                      otherAs[i].className = "";
                  }
                  
                  var a = next.getElementsByTagName("a");
                  for(var i=0;i<a.length;i++){
                      a[i].className = "";
                  }
                  var s = next.getElementsByTagName("span");
                  for(var i=0;i<s.length;i++){
                      s[i].style.display = "none";
                      s[i].className = "";
                  }
                  next.style.display = "block";
                  next.className = "cur";
                  target.className = "cur";
                  console.log(otherSpans);
              }else if(next.style.display == "block"){
                  next.style.display = "none";
                  target.className = "";
              }
          }
      }
        
      
      
      function nextSibling (ele){
          var p = ele.nextSibling;
          while(p){
              if(p.nodeType == 1){
                  return p;
              }
              p =p.nextSibling;
          }
          return null;
      }
      function sublingsAsTagName(ele,tagname){
          var a = [];
          var p = ele.previousSibling;
          while(p){
              if(p && p.nodeName == tagname){
                  a.unshift(p);
              }
              p=p.previousSibling;
          }
          p = ele.nextSibling;
          while(p){
              if(p && p.nodeName == tagname){
                  a.push(p);
              }
              p=p.nextSibling;
          }
          return a;
      }
      
      function msg(icon,msg){
    	  layer.msg(msg,{"icon":icon,time:1000});
      }
      
</script> 