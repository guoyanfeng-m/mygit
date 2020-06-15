package dao.terminal;

import java.util.List;

import org.springframework.stereotype.Repository;

import beans.terminal.TerminalGroupBean;
@Repository
public interface  TerminalGroupDao {
	 public void insertTerminalGroup(TerminalGroupBean terminalGroupBean);
	 public void deleteTerminalGroupById(TerminalGroupBean terminalGroupBean);
	 public void updateTerminalGroupByTerminalId(TerminalGroupBean terminalGroupBean);
	 public TerminalGroupBean queryTerminalGroupName(TerminalGroupBean terminalGroupBean);
	 public List<TerminalGroupBean> checkTerminalGroupName(TerminalGroupBean terminalGroupBean);
	 public List<String> queryTerminalGroupByParentID(int pid);//wangyulin 2014/12/25
	 public String queryTerminalGroupParentById(String t_id);
	 
	 /**
	  * @Description 通过编辑修改所属终端组的父级终端组(终端组移动)
	  * @Author 	weihuiming
	  * @Date		2017年2月20日上午11:49:38
	  * @param 		sourceId 编辑的终端组
	  * @param 		targetId 要修改的终端组
	  * @return
	 
	 public int updateTerminalGroupIdParent(@Param("sourceId") Integer sourceId, @Param("targetId") Integer targetId);
	  */
}
