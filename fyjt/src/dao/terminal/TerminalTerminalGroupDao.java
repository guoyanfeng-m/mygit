package dao.terminal;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.roletree.ResultGroupBean;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalTerminalGroupBean;
@Repository
public interface  TerminalTerminalGroupDao {
	 // 单终端多终端组入库
	 public void insertTerminalTerminalGroup(TerminalTerminalGroupBean terminalTerminalGroupList);
	 //单终端多终端组入库
	 public void insertTerminalTerminalGroups(@Param("terminalTerminalGroupList")List<TerminalTerminalGroupBean> terminalTerminalGroupList);
	 public void deleteTerminalTerminalGroupById(TerminalTerminalGroupBean terminalTerminalGroupBean);
	 public void updateTerminalTerminalGroupByTerminalId(TerminalTerminalGroupBean terminalTerminalGroupBean);
	 public List<TerminalTerminalGroupBean> queryTerminalTerminalGroupByTerminalId(TerminalTerminalGroupBean terminalTerminalGroupBean);
	 //查找终端组
	 public List<ResultGroupBean> queryRoleTerminalgroupByUserID(@Param("userId")Integer userId);
	 //删除终端终端组表中的数据
	 public void deleteTerminalTerminalGroupByIds(TerminalBean terminalBean);
	 //查找终端终端组表中数据
	 public List<Integer> selectTerminalTerminalGroupTerminalIds(TerminalBean terminalBean);
	 //根据终端id查询终端所属终端组
	 public String selectTerminalGroupIds(TerminalBean terminalBean);
}
