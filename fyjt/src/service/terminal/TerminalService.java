package service.terminal;

import java.util.List;

import util.PageInfo;
import beans.program.view.ProgramTerminalView;
import beans.sys.TreeBeans;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalGroupBean;
import beans.terminal.TerminalViewBean;
@SuppressWarnings("rawtypes")
public interface TerminalService {
	 
	 public List<TerminalViewBean> queryTerminalWithoutPage(TerminalBean terminalBean);
	 TerminalViewBean  queryTerminalWithoutPageById(TerminalBean terminalBean);
	 void insertTerminal(TerminalBean terminalBean, List<Integer> userlist, List<Integer> terminalgrouplist);
	 List<TerminalViewBean> queryTerminalByGroupId(Integer terminalGroupId,PageInfo pageInfo,List<String> userlist);
	 Integer queryTerminalTotleByGroupId(Integer terminalGroupId,List<String> userlist);
	 List<TerminalViewBean> queryBdTerminal(PageInfo pageInfo);
	 List<TerminalViewBean> queryTerminalNameByGroupId(Integer terminalGroupId,PageInfo pageInfo,List<String> userlist,String terminal_name);
	 Integer queryTerminalNameTotleByGroupId(Integer terminalGroupId,List<String> userlist,String terminal_name);
	 Integer queryBdTerminalCount();
	 List<TreeBeans> queryTree(TerminalGroupBean terminalGroupBean);
	 void deleteTerminalByTerminalId(TerminalBean terminalBean);
	 void updateTerminalAuditByTerminalId(TerminalBean terminalBean);
	 void updateTerminalByTerminalId(TerminalBean terminalBean);
	 List<TerminalGroupBean> queryTerminalGroup(TerminalGroupBean terminalGroupBean);
	 List<TerminalViewBean> queryTerminal(TerminalBean terminalBean,PageInfo pageInfo,List<String> userlist);
	 Integer queryTerminalCount(TerminalBean terminalBean,List<String> userlist);
	 List<TerminalViewBean> queryTerminalByTerminalIds(List<Integer> terminalId);
	 List<TreeBeans> queryTerminalTrees(Integer userId);
	 void updateBdTerminalByTerminalId(TerminalBean terminalBean,List<Integer> userlist);
	 List<String> queryTerminalMac(TerminalBean terminalBean);
	 public List<String> queryTerminalMacIp(TerminalBean terminalBean);
	 public void deleteTerminalByIpMac(TerminalBean terminalBean);
	 List<String> queryTerminalName(TerminalBean terminalBean);
	 public List<String> queryTerminalMacById(List terminalidlist);
	 public List<ProgramTerminalView> queryTerminalPublishStatus(String[] macs);
	 public Integer getTerminalStatus(List<String> mac);
	 public List<TreeBeans> queryTerminalGroups(Integer userId);
	/** (终端组移动)
	 * @Description 通过用户id查询角色下的终端组
	 * @Author 		weihuiming
	 * @Date		2017年2月22日下午3:51:46
	 * @param userId	用户id
	 * @return
	 
	public List<TreeBeans> selectTerminalTreeByUserId(Integer userId);
	
	 * @Description 查询该角色下的所有终端组
	 * @Author 		weihuiming
	 * @Date		2017年2月27日上午11:22:53
	 * @param userId
	 * @return
	 
	public List<TerminalGroupBean> selectTerminalTreeByUserIds(Integer userId);*/
}
