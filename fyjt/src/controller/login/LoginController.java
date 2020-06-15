package controller.login;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import service.carousel.CarouselService;
import service.config.ConfigService;
import service.cooperation.CooperationService;
import service.nav.NavService;
import service.notice.NoticeService;
import util.PageInfo;
import beans.carousel.CarouselBean;
import beans.cooperation.CooperationBean;
import beans.nav.NavBean;
import beans.notice.NoticeBean;


@Controller
@Transactional
public class LoginController {
	
	@Autowired
	private NavService navService;
	@Autowired
	private CarouselService carouselService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private CooperationService cooperationService;
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping(value="") 
	public String Index(HttpServletResponse response,HttpServletRequest request,Model model) throws IOException {
		idnex_0(model); // 导航条
		idnex_1(model); // 轮播图
		idnex_2(model); // 合作伙伴
		idnex_3(model);//内部公告
		
		
		return "jsp/index";
	}
	
	public void idnex_0(Model model) {
		List<NavBean> list = navService.queryNavPid();
		List<NavBean> listNavVo = null;
		for(int i=0;i<list.size();i++){
			listNavVo=navService.queryNavByPId(list.get(i).getId());
			list.get(i).setListNavvo(listNavVo);
		}
		model.addAttribute("navlist", list);
	}
	
	
	public void idnex_1(Model model) {
		List<CarouselBean> listcarousel = carouselService.queryCarouselAll();
		model.addAttribute("listcarousel", listcarousel);
	}
	
	public void idnex_2(Model model) {
		List<CooperationBean> cooperationList = cooperationService.queryCooperationAll();
		model.addAttribute("cooperationList", cooperationList);
	}
	
	public void idnex_3(Model model) {
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(5);
		pageInfo.setPage(1);
		List<NoticeBean> noticeList = noticeService.queryNotice(null, "Y", pageInfo);
		model.addAttribute("news", noticeList);
	}
	
}