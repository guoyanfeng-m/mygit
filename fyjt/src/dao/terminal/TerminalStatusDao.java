package dao.terminal;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.terminal.TerminalBean;
import beans.terminal.TerminalCloseTime;
import beans.terminal.TerminalDownloadTime;
import beans.terminal.TerminalStatusBean;
import beans.terminal.TerminalStatusViewBean;
import beans.view.TerminalMac;
@SuppressWarnings("rawtypes")
@Repository
public interface  TerminalStatusDao {
	 public List<TerminalStatusViewBean> queryTerminalStatus(List<Integer> terminal_id);
	 public void insertTerminalStatus(TerminalStatusBean terminalStatusBean);
	 public List<TerminalStatusViewBean> queryTerminalStatusById(Integer[] terminal_id);
	 public void updateTerminalStatusByTerminalId(TerminalStatusBean erminalStatusBean);
	 public void deleteTerminalStatusByTerminalId(TerminalBean terminalBean);
	 public List<TerminalStatusViewBean> queryTerminalStatusByUserId(Map map);
	 public Integer queryTerminalStatusByUserIdCount(@Param(value="list")List<String> userlist, @Param(value="filter")Integer filter, @Param(value="system")Integer system);
	 public List<TerminalStatusViewBean> queryTerminalStatusByGroupId(@Param(value="list") List<Integer> groupId,@Param(value="array") List<String> userlist,@Param(value="offset") Integer offset,@Param(value="limit") Integer limit, @Param(value="filter")Integer filter, @Param(value="system")Integer system);
	 public Integer queryTerminalStatusByGroupIdCount(@Param(value="list") List<Integer> groupId,@Param(value="array") List<String> userlist, @Param(value="filter")Integer filter, @Param(value="system")Integer system);
	 //定时关机
	 public List<TerminalCloseTime> querycloseTimeByTerminalId(String[] terminalId);
	 public Integer querycloseTimeCountByTerminalId(String[] terminalid);
	 public void insertcloseTimeByTerminalId(List<Map<String, Object>> dataMap);
	 public void updatecloseTimeByTerminalId(List<Map<String, Object>> dataMap);
	 public void deletecloseTimeByTerminalId(String[] terminalid);
	 public List selectDiskUsage();
	 public List<TerminalMac> queryMacAndTime();
	 public void deletecloseTimeByTerminalIds(TerminalBean terminalBean);
	 public List<TerminalDownloadTime> queryTerminalTimingSet(String[] terminalId);
	 public Integer queryDownloadCountByTerminalId(String[] delterminal);
	 public void deleteDownloadByTerminalId(String[] delterminal);
	 public void insertDownloadByTerminalId(List<Map<String, Object>> dataMap);
}
