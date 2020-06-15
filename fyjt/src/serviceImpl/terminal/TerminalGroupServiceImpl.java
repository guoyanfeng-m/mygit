package serviceImpl.terminal;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.terminal.TerminalGroupService;
import util.CreateTree;
import beans.program.ProgramTerminalBean;
import beans.role.ParametersRoleTerminalGroupBean;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalGroupBean;
import beans.terminal.TerminalTerminalGroupBean;
import beans.terminal.TerminalViewBean;
import dao.program.ProgramTerminalDao;
import dao.role.RoleDao;
import dao.terminal.TerminalDao;
import dao.terminal.TerminalGroupDao;
import dao.terminal.TerminalTerminalGroupDao;
import dao.user.UserDao;

@Service
@Transactional(readOnly=true)
public class TerminalGroupServiceImpl implements TerminalGroupService{
	@Autowired
	private TerminalDao terminalDao;
	@Autowired
	private TerminalGroupDao terminalGroupDao;
	@Autowired
	private TerminalTerminalGroupDao terminalTerminalGroupDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ProgramTerminalDao programTerminalDao;


	@Override
	@Transactional(readOnly=false)
	public boolean deleteTerminalGroupById(TerminalGroupBean terminalGroupBean) {
		boolean  success=true;
		TerminalGroupBean terminalGroupBean1 = new TerminalGroupBean();
		terminalGroupBean1.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean1);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupId = new CreateTree().getGroup(tree, terminalGroupBean.getT_id());
		TerminalTerminalGroupBean terminalTerminalGroupBean = new TerminalTerminalGroupBean();
		terminalTerminalGroupBean.setGroupId(terminalGroupBean.getT_id());
		List<TerminalViewBean> terminalViewBeanList = terminalDao.queryTerminalByGroupIdWithoutPage(groupId);
		for(TerminalViewBean terminalViewBean : terminalViewBeanList){
			ProgramTerminalBean bean=new ProgramTerminalBean();
			bean.setProgram_id(null);
			bean.setTerminal_id(terminalViewBean.getTerminal_id());
			if(programTerminalDao.queryByTerminalId(bean).size()>0){
				    success=false;
				    break;
			} else{
			TerminalBean terminalBean = new TerminalBean();
			terminalBean.setTerminal_id(terminalViewBean.getTerminal_id());
			terminalBean.setDeleted("1");
			terminalDao.deleteTerminalByTerminalId(terminalBean);
			}
		}
		if (success) {
		for(Integer groupIds :groupId){
			TerminalGroupBean terminalGroupBean2 = new TerminalGroupBean();
			terminalGroupBean2.setT_id(groupIds);
			terminalGroupDao.deleteTerminalGroupById(terminalGroupBean2);
		}
		terminalTerminalGroupDao.deleteTerminalTerminalGroupById(terminalTerminalGroupBean);
		roleDao.deleteTerminalgroupByTerminalGroupId(terminalGroupBean.getT_id());
		}
		return success;
	}

	@Override
	@Transactional(readOnly=false)
	public void insertTerminalGroup(TerminalGroupBean terminalGroupBean) {
		terminalGroupDao.insertTerminalGroup(terminalGroupBean);
		Integer role_id = userDao.queryRoleIDByUserID(terminalGroupBean.getCreatorID());
		ParametersRoleTerminalGroupBean parametersRoleTerminalGroupBean = new ParametersRoleTerminalGroupBean();
		parametersRoleTerminalGroupBean.setRole_id(role_id);
		parametersRoleTerminalGroupBean.setTerminalgroup_id(terminalGroupBean.getT_id());
		roleDao.insertRoleTerminalGroup(parametersRoleTerminalGroupBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateTerminalGroupByTerminalId(
			TerminalGroupBean terminalGroupBean) {
		terminalGroupDao.updateTerminalGroupByTerminalId(terminalGroupBean);
	}

	@Override
	public TerminalGroupBean queryTerminalGroupName(
			TerminalGroupBean terminalGroupBean) {
		return terminalGroupDao.queryTerminalGroupName(terminalGroupBean);
	}

	@Override
	public List<String> queryTerminalGroupByParentID(int pid) {
		return terminalGroupDao.queryTerminalGroupByParentID(pid);
	}

	@Override
	public List<TerminalGroupBean> checkTerminalGroupName(
			TerminalGroupBean terminalGroupBean) {
		return terminalGroupDao.checkTerminalGroupName(terminalGroupBean);
	}

	@Override
	public String queryTerminalGroupParentById(String t_Id) {
		return terminalGroupDao.queryTerminalGroupParentById(t_Id);
	}
	
	/**
	 * @Description 通过开始拖拽的终端组id来修改其父级id 修改值为 拖拽后目标节点的终端组id 数据传输层(终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年2月20日下午2:16:29
	 * @param 		sourceId	编辑的终端组
	 * @param 		targetId	更改所属终端组
	 * @return
	
	public int updateTerminalGroupIdParent(Integer sourceId, Integer targetId){
		return terminalGroupDao.updateTerminalGroupIdParent(sourceId, targetId);
	}
	 */
}
