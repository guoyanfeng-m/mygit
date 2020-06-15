package serviceImpl.terminal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.terminal.TerminalTerminalGroupService;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalTerminalGroupBean;
import dao.terminal.TerminalTerminalGroupDao;

@Service
@Transactional(readOnly=true)
public class TerminalTerminalGroupServiceImpl implements TerminalTerminalGroupService{
	@Autowired
	private TerminalTerminalGroupDao terminalTerminalGroupDao;

	@Override
	@Transactional(readOnly=false)
	public void deleteTerminalTerminalGroupById(
			TerminalTerminalGroupBean terminalGroupBean) {
		terminalTerminalGroupDao.deleteTerminalTerminalGroupById(terminalGroupBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertTerminalTerminalGroup(
			TerminalTerminalGroupBean terminalGroupBean) {
		terminalTerminalGroupDao.insertTerminalTerminalGroup(terminalGroupBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateTerminalTerminalGroupByTerminalId(
			TerminalTerminalGroupBean terminalGroupBean) {
		terminalTerminalGroupDao.updateTerminalTerminalGroupByTerminalId(terminalGroupBean);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryTerminalTerminalGroupByTerminalId(
			TerminalTerminalGroupBean terminalGroupBean) {
		return terminalTerminalGroupDao.queryTerminalTerminalGroupByTerminalId(terminalGroupBean);
	}
    /*
     * 根据终端id查询终端所属终端组
     */
	@Override
	public String selectTerminalGroupIds(TerminalBean terminalBean) {
		return terminalTerminalGroupDao.selectTerminalGroupIds(terminalBean);
	}
}
