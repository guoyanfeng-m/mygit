package controller.carousel;



import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import beans.carousel.CarouselBean;
import beans.user.UserBean;
import service.carousel.CarouselService;
import util.FileUtil;
import util.PageInfo;

/**
 * @author gyf
 *
 */
@Controller
@Transactional
@RequestMapping("carousel")
public class CarouselController {

	@Autowired
	private CarouselService carouselService;
	
		
	/**
	 * Description: 查询全部的轮播图
	 */
	@RequestMapping("/queryCarousel.do")
    @ResponseBody
	public Map<String,Object>  queryCarousel( 
			String page,//当前页
			String rows,//一页显示条数
			HttpServletRequest request) {
		//////////////////////////////////////////////////////////////
		
		int newpage = 1;
		int newrows = 0;
		if(page!=null&&page!=""&&!page.equals("0")){
			newpage = Integer.parseInt(page);
		}
		if(rows!=null&&rows!=""){
			newrows = Integer.parseInt(rows);
		}
		int total = carouselService.queryCarouselAll().size();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(newrows);
		pageInfo.setPage(newpage);
		List<CarouselBean> carouselViewBeanList = new ArrayList<CarouselBean>();
		carouselViewBeanList = carouselService.queryCarousel(pageInfo);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("rows", carouselViewBeanList);//返回的要显示的数据 
		map.put("total", total); //总条数
		return map;
	}
	
	@RequestMapping( value = "showImg" )
	public String showImg(){
		return "admin/carousel/showImg";
	}
	
	/**
	 * 上传图片
	 * @throws IOException 
	 */
	@RequestMapping(value="/fileUpload.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object>  fileUpload(@RequestParam(value = "imgurl",required = false) CommonsMultipartFile file,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>(); 
		if (file != null){
        	String fileName=file.getOriginalFilename();
        	
        	// 为了避免文件名重复，在文件名前加UUID
    		String uuid = UUID.randomUUID().toString().replace("-", "");
    		String uuidFileName = uuid + fileName;
    		// 设置文件保存的本地路径
    		String filePath = request.getSession().getServletContext().getRealPath("\\uploadFiles\\");
    		// 将文件保存到服务器
			String imgPath="";
			try {
				imgPath = FileUtil.upFile(file.getInputStream(), uuidFileName,filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("state", "error");
				result.put("message",e.getMessage() );
			}
			if(imgPath!=null){
				result.put("state", "success");
				result.put("url","\\fyjt//uploadFiles//"+uuidFileName );
				result.put("message", imgPath);
			}
        	
			
        }else{
        	result.put("state", "error");
			result.put("message","文件为空");
        }
        
        
        
		return result;
	}
	
	/**
	 * Description:添加新的轮播图
	 */
	@RequestMapping(value="/insertCarousel.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object> insertCarousel(String carouselname,HttpServletRequest request,String imgurl,Integer isort){
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		CarouselBean carouselBean=new CarouselBean();
		carouselBean.setImgurl(imgurl);
		carouselBean.setIsort(isort);
		Timestamp time = new Timestamp(new Date().getTime());
		carouselBean.setCreate_time(time);
		carouselBean.setUpdate_time(time);
		carouselBean.setCreator_id(ub.getUser_id());
		carouselService.insertCarousel(carouselBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	/**
	 * Description:图片轮播修改
	 */
	@RequestMapping(value="/updateCarousel.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String,Object> updateCarousel(String carousel_id ,String carouselame,HttpServletRequest request,String imgurl,Integer isort){
		CarouselBean carouselBean=new CarouselBean();
		UserBean ub=(UserBean) request.getSession().getAttribute("user");
		carouselBean.setCarousel_id(Integer.parseInt(carousel_id));
		carouselBean.setImgurl(imgurl);
		carouselBean.setIsort(isort);
		Timestamp time = new Timestamp(new Date().getTime());
		carouselBean.setUpdate_time(time);
		carouselBean.setCreator_id(ub.getUser_id());
		carouselService.updateCarouselById(carouselBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", true);  
		return map;
	}
	
	/**
	 * Description:图片轮播删除
	 */
	@RequestMapping(value="/delCarouselByIds.do",method=RequestMethod.POST)  
	@ResponseBody
	public Map<String, Object> delCarouselByIds(
			HttpServletRequest request) {
		String[] ids = request.getParameterValues("ids[]");
		for (int i = 0; i < ids.length; i++) {
			carouselService.deleteCarouselById(Integer.parseInt(ids[i]));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
}
