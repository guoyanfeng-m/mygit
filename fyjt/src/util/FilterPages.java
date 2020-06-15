package util;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.list.SetUniqueList;
import org.springframework.web.filter.OncePerRequestFilter;

import beans.user.UserBean;
public class FilterPages extends OncePerRequestFilter  {
	@SuppressWarnings("unchecked")
	private List<String> list=SetUniqueList.decorate(new ArrayList<String>());
	
	@Override  
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws IOException, ServletException{  
		    initList();
			request.setCharacterEncoding("UTF-8");
	        // 设置返回请求的字符编码     
	        response.setCharacterEncoding("UTF-8");     
	        // 获取Session     
	        HttpSession session = request.getSession();     
	        // 获取Session中存储的对象     
	        UserBean userBean = (UserBean) session.getAttribute("user");   
	        // 获取当前请求的URI     
	        String url = request.getServletPath(); 
//	        System.out.println("ServletPath:" + request.getServletPath());
//	        System.out.println("ContextPath:" + request.getContextPath());
//	        System.out.println("RequestURI:" + request.getRequestURI());
//	        System.out.println("PathInfo:" + request.getPathInfo());
//	        System.out.println("RequestURL:" + request.getRequestURL());
	        // 判断Session中的对象是否为空；判断请求的URI是否为不允许过滤的URI     
	        if(list!=null&&list.contains(url)){
	        	 filterChain.doFilter(request, response);  
	        	return;
	        }
	        if(userBean!=null){
	        	 filterChain.doFilter(request, response);  
	        	return;
	        }
	        if(url.indexOf(".do")>-1){
	        	response.setContentType("application/json");
	        	PrintWriter out=response.getWriter();
	        	out.print("{\"iislogout\":\"iislogout\"}");
	        	out.flush();
	        	out.close();
	        }else{
	        	response.sendRedirect(request.getContextPath() + "/login/outTime.jsp");
	        }
    }  
	private void initList() {
			//后台页面
			list.add("/redisManage/serviceRedisManage.do");
			list.add("/admin");
			list.add("/admin/login/login.jsp");
			list.add("/admin/main/index.jsp");
			list.add("/admin/login/outTime.jsp");
			list.add("/login/outTime.html");
			list.add("/login/userlogin.do");
			list.add("/cityweather/queryWeather.do");
			list.add("/playlistdown/GetDownloadListAction.action");
			list.add("/element/exists.action");
			list.add("/element/insert.action");

			
			list.add("/jsp");
			list.add("/jsp/show/index.jsp");
	}
}
