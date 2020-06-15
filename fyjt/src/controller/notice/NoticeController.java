package controller.notice;



import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import beans.notice.NoticeBean;
import beans.user.UserBean;
import service.notice.NoticeService;
import util.FileUtil;
import util.PageInfo;
import vo.PageEnt;

/**
 * @author gyf
 *
 */
@Controller
@Transactional
@RequestMapping("notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
		
	/**
	 * 上传图片
	 * @throws IOException 
	 */
	@RequestMapping(value="/fileUpload.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object>  fileUpload(@RequestParam(value = "imgFile",required = false) CommonsMultipartFile file,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>(); 
		if (file != null){
        	String fileName=file.getOriginalFilename();
        	
        	// 为了避免文件名重复，在文件名前加UUID
    		String uuid = UUID.randomUUID().toString().replace("-", "");
    		String uuidFileName = uuid + fileName;
    		// 设置文件保存的本地路径
    		String savePath = request.getSession().getServletContext().getRealPath("\\uploadFiles\\notice\\");
    		//文件保存目录URL
    		String saveUrl  =request.getContextPath() + "\\uploadFiles\\notice\\";;
    		// 将文件保存到服务器
			String imgPath="";
			try {
				imgPath = FileUtil.upFile(file.getInputStream(), uuidFileName,savePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("error", 1);
				result.put("message", e.getMessage());
			}
			System.out.println(imgPath);
			result.put("error", 0);
			result.put("url", saveUrl + uuidFileName);
			
        }else{
        	result.put("error", 1);
			result.put("message","文件为空");
        }
        
		return result;
	}
	
	
	/**
	 * Description: 查询全部的公告
	 */
	@RequestMapping("/queryNotice.do")
    @ResponseBody
	public Map<String,Object>  queryNotice( 
			String sname,
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
		Map<String,Object> map1 = new HashMap<String,Object>();  
		map1.put("sName", sname);
		int total =noticeService.queryNoticeAll(map1).size();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		List<NoticeBean> noticeViewBeanList = new ArrayList<NoticeBean>();
		noticeViewBeanList = noticeService.queryNotice(sname,null,pageInfo);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", noticeViewBeanList);//返回的要显示的数据 
		map.put("total", total); //总条数
		return map;
	}
	
	/**
	 * Description:添加新的公告
	 */
	@RequestMapping(value="/insertNotice.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object> insertNotice(String title,String description,HttpServletRequest request){
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		NoticeBean noticeBean=new NoticeBean();
		noticeBean.setTitle(title);
		noticeBean.setDescription(description);
		noticeBean.setStatus("C");
		Timestamp time = new Timestamp(new Date().getTime());
		noticeBean.setCreate_time(time);
		noticeBean.setUpdate_time(time);
		noticeBean.setCreator_id(ub.getUser_id());
		noticeBean.setUpdate_id(ub.getUser_id());
		noticeService.insertNotice(noticeBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	/**
	 * Description:公告修改
	 */
	@RequestMapping(value="/updateNotice.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object> updateNotice(String id ,String title,String description,HttpServletRequest request){
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		NoticeBean noticeBean=new NoticeBean();
		noticeBean.setId(Integer.parseInt(id));
		noticeBean.setTitle(title);
		noticeBean.setDescription(description);
		Timestamp time = new Timestamp(new Date().getTime());
		noticeBean.setUpdate_time(time);
		noticeBean.setUpdate_id(ub.getUser_id());
		noticeService.updateNoticeById(noticeBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	/**
	 * Description:删除公告
	 */
	@RequestMapping(value="/delNoticeByIds.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String, Object> delNoticeByIds(
			HttpServletRequest request) {
		String[] ids = request.getParameterValues("ids[]");
		NoticeBean noticeBean=new NoticeBean();
		for (int i = 0; i < ids.length; i++) {
			noticeBean.setId(Integer.parseInt(ids[i]));
			noticeBean.setStatus("D");
			noticeService.updateNoticeById(noticeBean);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * Description:审核公告
	 */
	@RequestMapping(value="/editNoticeById.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String, Object> editNoticeById(
		String id,String status, HttpServletRequest request) {
		NoticeBean noticeBean=new NoticeBean();
		noticeBean.setId(Integer.parseInt(id));
		noticeBean.setStatus(status);
		noticeService.updateNoticeById(noticeBean);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	
	
	
	//******************************************************************************************************************************
	/*************************************************************************************************************************************
	 * 前端页面
	 ***********************************************************************************************************************************/
	
	@RequestMapping(value="noticeList") 
	public String NoticeList(HttpServletResponse response,HttpServletRequest request,Model model,Integer pageNo) throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("sStatus", "Y");
		List<NoticeBean> page = noticeService.queryNoticeAll(map);
		Integer total=page.size();
		model.addAttribute("pageEnt",new PageEnt<NoticeBean>(page,pageNo==null?1: pageNo,20,total));
		
		return "jsp/notice/noticeList";
	}
	
	@RequestMapping(value="noticeDetail/{id}") 
	public String NoticeDetail(HttpServletResponse response,HttpServletRequest request,Model model,@PathVariable Integer id) throws IOException {
		NoticeBean ent=noticeService.queryNoticeById(id);
		model.addAttribute("ent",ent);
		return "jsp/notice/noticeDetail";
	}
	
	
	
	
	
}
