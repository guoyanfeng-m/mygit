package controller.nav;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vo.ComboBase;
import beans.nav.NavBean;
import service.nav.NavService;
import util.PageInfo;

/**
 * @author gyf
 *
 */
@Controller
@Transactional
@RequestMapping("nav")
public class NavController {
	
	@Autowired
	private NavService navService;
	
		
	/**
	 * Description: 查询全部的轮播图
	 */
	@RequestMapping("/queryNav.do")
    @ResponseBody
	public Map<String,Object>  queryNav(  
			String page,//当前页
			String rows,//一页显示条数
			HttpServletRequest request) {
		int newpage = 1;
		int newrows = 0;
		if(page!=null&&page!=""&&!page.equals("0")){
			newpage = Integer.parseInt(page);
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		int total = navService.queryNavAll().size();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		List<NavBean> NavList = new ArrayList<NavBean>();
		NavList = navService.queryNav(pageInfo);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", NavList);//返回的要显示的数据 
		map.put("total", total); //总条数
		return map;
	}
	
	@RequestMapping( value = "pageAdd" )
	public String pageAdd(){
		return "admin/nav/navAdd";
	}
	
	/**
	 * 查询父级导航数据
	 * @author dean
	 * @date 2016年3月24日下午2:06:50
	 */
	@ResponseBody
	@RequestMapping(value = "navParentList")
	public List<ComboBase> navParentList(){
		List<ComboBase> data = new ArrayList<ComboBase>();
		data.add(new ComboBase(0,"根目录",true));
		try {
			List<NavBean> navBeanList=navService.queryNavPid();
			for(int i=0;i<navBeanList.size();i++){
				ComboBase cb = new ComboBase();
				cb.setId(navBeanList.get(i).getId());
				cb.setText(navBeanList.get(i).getName());
				data.add(cb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	@RequestMapping(value="/insertNav.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object> insertNav(HttpServletRequest request){
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String pid = request.getParameter("pid");
		NavBean navBean=new NavBean();
		navBean.setName(name);
		navBean.setUrl(url);
		navBean.setPid(Integer.parseInt(pid));
		navService.insertNav(navBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	@RequestMapping( value = "pageEdit" )
	public String pageEdit(HttpServletRequest request){
		Map<String, String> map = this.getParametersMap(request);
		NavBean navBean= navService.queryNavById(Integer.parseInt(map.get("id")));
		map.put("pid", String.valueOf(navBean.getPid()));
		request.setAttribute("info", map);
		return "admin/nav/navEdit";
	}
	
	/**
	 * 获取request中的请求参数，封装为Map
	 * @author dean
	 * @date 2016年3月24日下午6:03:46
	 */
	protected Map<String,String> getParametersMap(HttpServletRequest request){
		Map<String,String> map = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		try {
			for (String key : parameterMap.keySet()) {
				String[] strings = parameterMap.get(key);
				if( strings.length == 1 ){
					
						map.put(key,strings[0]);
					
				}else{
					String temp = "";
					for (String str : strings) {
						temp +=str+",";
					}
					
					map.put(key, temp.substring(0,temp.lastIndexOf(",")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value="/updateNav.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object> updateNav(Integer id ,Integer pid ,String name,String url,HttpServletRequest request){
		NavBean navBean=new NavBean();
		navBean.setId(id);
		navBean.setPid(pid);
		navBean.setName(name);
		navBean.setUrl(url);
		navService.updateNavById(navBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	@RequestMapping(value="/delNavByIds.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String, Object> delNavByIds(
			HttpServletRequest request) {
		String[] ids = request.getParameterValues("ids[]");
		for (int i = 0; i < ids.length; i++) {
			navService.deleteNavById(Integer.parseInt(ids[i]));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
}
