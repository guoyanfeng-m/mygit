package service.terminal;

import java.util.List;

import beans.terminal.TerminalGroupBean;
public interface TerminalGroupService {
	 void insertTerminalGroup(TerminalGroupBean terminalGroupBean);
	 boolean deleteTerminalGroupById(TerminalGroupBean terminalGroupBean);
	 void updateTerminalGroupByTerminalId(TerminalGroupBean terminalGroupBean);
	 TerminalGroupBean queryTerminalGroupName(TerminalGroupBean terminalGroupBean);
	 List<TerminalGroupBean> checkTerminalGroupName(TerminalGroupBean terminalGroupBean);
	 List<String> queryTerminalGroupByParentID(int pid);//wangyulin 2014/12/25
	 public String queryTerminalGroupParentById(String t_id);
	 /**
	 * @Description 通过开始拖拽的终端组id来修改其父级id 修改值为 拖拽后目标节点的终端组id 数据传输层(终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年2月20日下午2:16:29
	 * @param 		sourceId	编辑的终端组
	 * @param 		targetId	更改所属终端组
	 * @return
	
	public int updateTerminalGroupIdParent(Integer sourceId, Integer targetId);
		 */
}
