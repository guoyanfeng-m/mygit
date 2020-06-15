package dao.terminal;

import java.util.List;

import org.springframework.stereotype.Repository;

import beans.terminal.Program_terminalBean;
import beans.terminal.TaskDownLoadBean;
@Repository
public interface  TaskDownLoadDao {
	 public List<TaskDownLoadBean> queryTaskDownLoadByTerminalId(String[] terminalId);
	 public List<Program_terminalBean> queryProgram_terminalByTerminalId(String[] terminalId);
}
