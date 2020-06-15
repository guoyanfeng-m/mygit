package dao.terminal;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.program.view.ProgramTerminalView;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalGroupBean;
import beans.terminal.TerminalViewBean;
import beans.terminal.TerminalViewsBean;

@SuppressWarnings("rawtypes")
@Repository
public interface  TerminalDao {
	 public List<TerminalViewBean> queryTerminal(Map<String, Object> map);
	 public Integer queryTerminalCount(Map map);
	 public List<TerminalViewBean> queryBdTerminal(Map map);
	 public Integer queryBdTerminalCount();
	 public List<TerminalViewBean> queryTerminalWithoutPage(TerminalBean terminalBean);
	 public TerminalViewBean queryTerminalWithoutPageById(TerminalBean terminalBean);
	 public void insertTerminal(TerminalBean terminalBean);
	 public List<TerminalViewsBean> queryAll();
	 public List<TerminalGroupBean> queryTree(TerminalGroupBean terminalGroupBean);
	 public void deleteTerminalByTerminalId(TerminalBean terminalBean);
	 public List<TerminalViewBean> queryTerminalByGroupId(@Param(value="list") List<String> userlist,@Param(value="array") Integer[] groupId,@Param(value="offset") Integer offset,@Param(value="limit") Integer limit);
	 public Integer queryTerminalTotleByGroupId(@Param(value="list") List<String> userlist,@Param(value="array") Integer[] groupId);
	 public List<TerminalViewBean> queryTerminalNameByGroupId(@Param(value="list") List<String> userlist,@Param(value="array") Integer[] groupId,@Param(value="terminal_name") String terminal_name,@Param(value="offset") Integer offset,@Param(value="limit") Integer limit);
	 public Integer queryTerminalNameTotleByGroupId(@Param(value="list") List<String> userlist,@Param(value="array") Integer[] groupId,@Param(value="terminal_name") String terminal_name);
	 public List<TerminalViewBean> queryTerminalByGroupIdWithoutPage(List<Integer> groupId);
	 public List<TerminalViewBean> queryTerminalByTerminalIds(@Param(value="list") List<Integer> terminalId);
	 public List<TerminalViewBean>  queryTerminalTree(Integer roleId);
	 public List<String> queryTerminalMac(TerminalBean terminalBean);
	 public List<String> queryTerminalMacIp(TerminalBean terminalBean);
	 public void deleteTerminalByIpMac(TerminalBean terminalBean);
	 public List<String> queryTerminalName(TerminalBean terminalBean);
	 public List<String> queryTerminalMacById(List terminalidlist);
	 public List<ProgramTerminalView> queryTerminalPublishStatus(String[] macs);
	 public Integer getTerminalStatus(List<String> mac);
	 public void updateTerminalByTerminalId(TerminalBean terminalBean);
	 
	 /**
	  * @Description 通过用户id查找用户角色下的终端组(终端组移动)
	  * @Author 	weihuiming
	  * @Date		2017年2月22日下午3:40:46
	  * @param 		userId
	  * @return
	  
	 public List<TerminalGroupBean> selectTerminalTreeByUserId(@Param(value = "userId")Integer userId);
	 */
}
