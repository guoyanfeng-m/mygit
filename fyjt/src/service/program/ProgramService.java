package service.program;

import java.util.List;
import java.util.Map;
import java.util.Set;

import util.DownLoadList;
import util.PageInfo;
import beans.program.ProgramBean;
import beans.program.ProgramTerminalBean;
import beans.program.ProgramViewBean;
import beans.program.view.ProgramTerminalView;

@SuppressWarnings("rawtypes")
public interface ProgramService {
	void insertProgram(ProgramBean programBean,Map<String,Object> programMap);
	void deleteProgramById(Integer[] program_id);
	Integer queryProgramCount(ProgramBean programBean, Integer creatorid, List<Integer> userids);
	List<ProgramViewBean> queryProgram(ProgramBean programBean,PageInfo pageinfo, Integer creatorid, List<Integer> userids);
	void send(Integer[] ids,ProgramBean programBean);
	List<ProgramBean> queryProgramByIds(List<Integer> program_id);
	void updateProgram(ProgramBean programBean,Map<String,Object> programMap);
	void auditProgram(ProgramBean programBean);
	void insertProgramTerminal(Integer[] ids,int program_id);
	public List<ProgramTerminalView> queryProgramTerminal(ProgramTerminalBean programTerminalBean);
	void stopProgram(Integer[] program_ids);
	public List<ProgramBean> queryProgramByName(String name);
	public void checkProgramPublish(Integer program_id);
	boolean stopProgramTerminal(Integer program_id, Integer terminal_id);

	boolean queryElementCount(Set<String> urllist); // wangyulin 2014/12/18
	DownLoadList findPlaylist(List<Integer> idList);// wangyulin 2014/12/19
	Map<String,Object> checkMap(Map<String, Object> programMap,boolean isMainScene);
	boolean stopProgramTerminals(Integer program_id, Integer[] terminal_id);
	
	public List<ProgramTerminalView> queryByTerminalId(ProgramTerminalBean bean);
	List<ProgramBean> queryProgramTimeByTerminalId(String terminalId, Integer program_id);
	String queryTerminalNameByTerminalId(int terminalId);
	List queryTerminalIdByProgramId(int programId);
	List<ProgramTerminalBean> queryTerminalIdByMacs(String[] macList);

	/**
	 * 导入节目存储修改
	 * @param programBean 节目bean
	 * @param xmlContext  原节目xml文件
	 * @param xmlUrl	xml存储路径
	 * @return
	 */
	boolean insertImpProgram(ProgramBean programBean ,String xmlContext ,String xmlUrl);
	//通过节目id查找节目的xml 协议信息
	String selectProgramXmlById(ProgramBean program);
}
