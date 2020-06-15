package service.terminal;

import java.util.List;

import beans.terminal.TerminalBean;
import beans.terminal.TerminalTerminalGroupBean;
public interface TerminalTerminalGroupService {
	 void insertTerminalTerminalGroup(TerminalTerminalGroupBean terminalGroupBean);
	 void deleteTerminalTerminalGroupById(TerminalTerminalGroupBean terminalGroupBean);
	 void updateTerminalTerminalGroupByTerminalId(TerminalTerminalGroupBean terminalGroupBean);
	 @SuppressWarnings("rawtypes")
	List queryTerminalTerminalGroupByTerminalId(TerminalTerminalGroupBean terminalGroupBean);//wangyulin
	 //根据终端id查询终端所属终端组
	 String selectTerminalGroupIds(TerminalBean terminalBean);
}
