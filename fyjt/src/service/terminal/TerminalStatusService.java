package service.terminal;

import java.util.List;
import java.util.Map;

import util.PageInfo;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalCloseTime;
import beans.terminal.TerminalDownloadTime;
import beans.terminal.TerminalStatusBean;
import beans.terminal.TerminalStatusViewBean;
import beans.view.TerminalMac;
public interface TerminalStatusService {
	 List<TerminalStatusViewBean> queryTerminalStatus(List<Integer> terminalId);
	 void insertTerminalStatus(TerminalStatusBean terminalStatusBean);
	 List<TerminalStatusViewBean> queryTerminalStatusById(Integer[] terminal_id);
	 void updateTerminalStatusByTerminalId(TerminalStatusBean erminalStatusBean);
	 void deleteTerminalStatusByTerminalId(TerminalBean terminalBean);
	 List<TerminalStatusViewBean> queryTerminalStatusByUserId(List<String> userId,PageInfo pageInfo, Integer filter, Integer system);
	 Integer queryTerminalStatusByUserIdCount(List<String> role_id, Integer filter, Integer system);
	 public List<TerminalStatusViewBean> queryTerminalStatusByGroupId(Integer terminalGroupId,List<String> role_id,PageInfo pageInfo, Integer filter, Integer system);
	 public Integer queryTerminalStatusByGroupIdCount(Integer terminalGroupId,List<String> role_id, Integer filter, Integer system);
	 @SuppressWarnings("rawtypes")
	public List selectDiskUsage();
	 //定时关机
	 List<TerminalCloseTime> querycloseTimeByTerminalId(String[] terminalId);
	 void insertcloseTimeByTerminalId(TerminalCloseTime terminalCloseTime);
	 void updatecloseTimeByTerminalId(List<Map<String, Object>> dataMap, String[] delterminal);
	 void deletecloseTimeByTerminalId(Integer terminalid);
	 List<TerminalMac> queryMacAndTime();
	 void updatecloseTimeXml(List<Map<String, Object>> timeAndMacList, String[] delterminalmac);
	 List<TerminalDownloadTime> queryTerminalTimingSet(String[] terminalId);
	 void updateDownloadByTerminalId(List<Map<String, Object>> dataMap, String[] delterminal);
	void updateDownloadXml(List<Map<String, Object>> timeAndMacList, String[] delmac);
	 

}
