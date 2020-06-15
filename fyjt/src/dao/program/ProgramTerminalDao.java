package dao.program;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.program.ProgramBean;
import beans.program.ProgramTerminalBean;
import beans.program.view.ProgramTerminalView;

@Repository
public interface  ProgramTerminalDao {
	 public void insertProgramTerminal(ProgramTerminalBean programTerminalBean);
	 public void updateProgramTerminal(ProgramTerminalBean programTerminalBean);
	 public List<ProgramTerminalView> queryProgramTerminal(ProgramTerminalBean programTerminalBean);
	 void deleteProgramTerminal(ProgramTerminalBean programTerminalBean);
	 public List<ProgramTerminalView> queryByTerminalId(ProgramTerminalBean bean);
	 public List<Integer> queryProgramIdByTerminalId(Map<String,Object> param);
	 public String queryTerminalNameByTerminalId(int terminalId);
	 public List<ProgramBean> queryPubProgramByIds(List<Integer> programIdList);
	 @SuppressWarnings("rawtypes")
	 public List queryTerminalIdByProgramId(int programId);
	 public List<ProgramTerminalBean> queryTerminalIdByMacs(String[] macList);
}
