package service.terminal;

import java.util.List;

import beans.program.ProgramTerminalBean;
import beans.terminal.Program_terminalBean;
import beans.terminal.TaskDownLoadBean;
public interface TaskDownLoadService {
	List<TaskDownLoadBean> queryTaskDownLoadByTerminalId(String[] terminal_id);
	List<Program_terminalBean> queryProgram_terminalByTerminalId(String[] terminalId);
	void deleteProgram_terminalByTerminalId(ProgramTerminalBean programTerminalBean);
}
