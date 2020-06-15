package serviceImpl.terminal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.terminal.TaskDownLoadService;
import beans.program.ProgramTerminalBean;
import beans.terminal.Program_terminalBean;
import beans.terminal.TaskDownLoadBean;
import dao.program.ProgramTerminalDao;
import dao.terminal.TaskDownLoadDao;

@Service
@Transactional(readOnly=true)
public class TaskDownLoadServiceImpl implements TaskDownLoadService{
	@Autowired
	private TaskDownLoadDao taskDownLoadDao;
	@Autowired
	private ProgramTerminalDao programTerminalDao;
	

	@Override
	public List<TaskDownLoadBean> queryTaskDownLoadByTerminalId(
			String[] terminalId) {
		return taskDownLoadDao.queryTaskDownLoadByTerminalId(terminalId);
	}


	@Override
	public List<Program_terminalBean> queryProgram_terminalByTerminalId(
			String[] terminalId) {
		return taskDownLoadDao.queryProgram_terminalByTerminalId(terminalId);
	}


	@Override
	@Transactional(readOnly=false)
	public void deleteProgram_terminalByTerminalId(ProgramTerminalBean programTerminalBean) {
		programTerminalDao.deleteProgramTerminal(programTerminalBean);

	}

}
