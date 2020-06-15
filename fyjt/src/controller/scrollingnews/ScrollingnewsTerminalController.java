package controller.scrollingnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.scrollingnews.ScrollingnewsTerminalService;
import service.terminal.TerminalService;
import beans.scrollingnews.ScrollingnewsTerminalBean;
import beans.scrollingnews.TerminalTerminalGroupView;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalViewBean;
@Controller
@Transactional
public class ScrollingnewsTerminalController {
	@Autowired
	private ScrollingnewsTerminalService sService;
	@Autowired
	private TerminalService terminalService;
	/****
	 * ���
	 * @param terminal_id
	 * @param scrollingNews_id
	 * @return
	 */
	@RequestMapping("insertScrollTerminal.do")
	@ResponseBody
	 public Map<String,Object> insertScrollTerminal(
			 @RequestParam(value="terminal_id")Integer terminal_id,
			 @RequestParam(value="scrollingNews_id")Integer scrollingNews_id,
			 HttpServletRequest request) {
		ScrollingnewsTerminalBean sBean = new ScrollingnewsTerminalBean();
		sBean.setTerminal_id(terminal_id);
		sBean.setScrollingNews_id(scrollingNews_id);
		sService.insertScrollTerminal(sBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "true");  
		return map;
	}
	/****
	 * ɾ��
	 * @param new_id
	 * @param request
	 * @return
	 */
	@RequestMapping("/delScrollTerminal.do")
	@ResponseBody
	public Map<String,Object>  delScrollTerminal(Integer terminal_id,Integer scrollingNews_id, 
			HttpServletRequest request) {
		ScrollingnewsTerminalBean sBean=new ScrollingnewsTerminalBean();
		sBean.setTerminal_id(terminal_id);
		sBean.setScrollingNews_id(scrollingNews_id);
		sService.delScrollTerminal(sBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success");    
		return map;
	}
	/***
	 * ������ѯ
	 * @param new_id
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryScrollTerminal.do")
	@ResponseBody
	public Map<String,Object>  queryScrollTerminal(ScrollingnewsTerminalBean scrollterminal, 
			HttpServletRequest request) {
		ScrollingnewsTerminalBean sBean = new ScrollingnewsTerminalBean();
	    sBean.setTerminal_id(scrollterminal.getTerminal_id());
	    sBean.setScrollingNews_id(scrollterminal.getScrollingNews_id());
		List<ScrollingnewsTerminalBean> sBeanList = new ArrayList<ScrollingnewsTerminalBean>();
		sBeanList = sService.queryScrollTerminal(sBean);
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("sBeanList", sBeanList);    
		return map;
	}
	/***
	 * ��ѯ����
	 * @return
	 */
	@RequestMapping("/queryScrollTerminalAll.do")
    @ResponseBody
	public Map<String,Object> queryAll(){
		List<ScrollingnewsTerminalBean> sBeanList = new ArrayList<ScrollingnewsTerminalBean>();
		sBeanList = sService.queryAll();
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("sBeanList", sBeanList); 
		return map;
	}
	/***
	 * ��ݹ�����Ϣ��ѯ��Ӧ�ն����ն���
	 * @return
	 */
	@RequestMapping("/queryTerminalGroup.do")
    @ResponseBody
	public Map<String,Object> queryTerminalGroup(
			String page,//��ǰҳ
			String rows,//һҳ��ʾ����
			HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		List<TerminalViewBean> terminalViewBean=new ArrayList<TerminalViewBean>();
		List<TerminalTerminalGroupView> viewList=new ArrayList<TerminalTerminalGroupView>();
		ScrollingnewsTerminalBean sBean=new ScrollingnewsTerminalBean();
		sBean.setScrollingNews_id(Integer.parseInt(request.getParameter("new_id")));
		List<Integer>  terminalId = new ArrayList<Integer>();
		List<ScrollingnewsTerminalBean> terminalIds=sService.queryScrollTerminal(sBean);
		for(int i=0;i<terminalIds.size();i++){
			 if(terminalId.contains(terminalIds.get(i).getTerminal_id())){
				 continue;
			 }else{
				 terminalId.add(terminalIds.get(i).getTerminal_id());
			 } 
		}
		for(int n=0;n<terminalId.size();n++){
			TerminalBean terBean=new TerminalBean();
			 terBean.setTerminal_id(terminalId.get(n));
			 terminalViewBean=terminalService.queryTerminalWithoutPage(terBean);
			 for(int m=0;m<terminalViewBean.size();m++){
				 TerminalTerminalGroupView view=new TerminalTerminalGroupView();
				 view.setTerminal_id(terminalViewBean.get(m).getTerminal_id());
				 view.setTerminal_name(terminalViewBean.get(m).getTerminal_name());
				 view.setIp(terminalViewBean.get(m).getIp());
				 view.setMac(terminalViewBean.get(m).getMac());
				 view.setGroupName(terminalViewBean.get(m).getGroupName());
				 viewList.add(view);
			 }
		}
		map.put("rows", viewList); 
		map.put("page",page); //��ǰҳ��
		map.put("total", viewList.size());//���������
		return map;
	}
}
    