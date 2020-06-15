package serviceImpl.roletree;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.roletree.RoleTreeService;
import util.CreateTree;
import beans.roletree.ResultGroupBean;
import beans.roletree.ResultTerminalBean;
import beans.roletree.RoletreeParametersBean;
import beans.sys.TreeBeans;
import dao.roletree.RoleTreeDao;

@SuppressWarnings({"unchecked","static-access"})
@Service
@Transactional(readOnly=true)
public class RoleTreeServiceImpl implements RoleTreeService{
	@Autowired
	private RoleTreeDao roleTreeDao;

	@Override
	public List<TreeBeans> queryRoleTerminalgroup(RoletreeParametersBean pb) {
		List<ResultGroupBean> terminalGroupBeanList = new ArrayList<ResultGroupBean>();
		terminalGroupBeanList=roleTreeDao.queryRoleTerminalgroup(pb);
		List<ResultTerminalBean> terminalBeanList = new ArrayList<ResultTerminalBean>();
		terminalBeanList=roleTreeDao.queryRoleTerminal(pb);
		 List<TreeBeans> list = new ArrayList<TreeBeans>(); 
			for(int i=0;i<terminalGroupBeanList.size();i++){
				TreeBeans treeBeans = new TreeBeans();
				treeBeans.setId(terminalGroupBeanList.get(i).getT_id());
//				treeBeans.setState("closed");
				treeBeans.setParentId(Integer.parseInt(terminalGroupBeanList.get(i).getParentID()));
				treeBeans.setText(terminalGroupBeanList.get(i).getGroupName());
				list.add(treeBeans);
			}
			for(int i=0;i<terminalBeanList.size();i++){
				TreeBeans terminalBeans = new TreeBeans();
				terminalBeans.setId(terminalBeanList.get(i).getTerminal_id());
//				treeBeans.setState("closed");
				terminalBeans.setParentId(Integer.parseInt(terminalBeanList.get(i).getT_id()));
				terminalBeans.setText(terminalBeanList.get(i).getTerminal_name());
				list.add(terminalBeans);
			}
			List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
			treeBeansList = new CreateTree().formatTree(list);
		return treeBeansList;
	}

	public List<TreeBeans> queryRoleTerminalByRoleID(int roleid) {
		List<ResultGroupBean> terminalGroupBeanList = new ArrayList<ResultGroupBean>();
		terminalGroupBeanList=roleTreeDao.queryRoleTerminalgroupByRoleID(roleid);
		List<ResultTerminalBean> terminalBeanList = new ArrayList<ResultTerminalBean>();
		terminalBeanList=roleTreeDao.queryRoleTerminalByRoleID(roleid);
		 List<TreeBeans> list = new ArrayList<TreeBeans>(); 
			for(int i=0;i<terminalGroupBeanList.size();i++){
				TreeBeans treeBeans = new TreeBeans();
				treeBeans.setId(terminalGroupBeanList.get(i).getT_id());
//				treeBeans.setState("closed");
				treeBeans.setParentId(Integer.parseInt(terminalGroupBeanList.get(i).getParentID()));
				treeBeans.setText(terminalGroupBeanList.get(i).getGroupName());
				list.add(treeBeans);
			}
			for(int i=0;i<terminalBeanList.size();i++){
				TreeBeans terminalBeans = new TreeBeans();
				terminalBeans.setId(terminalBeanList.get(i).getTerminal_id());
//				treeBeans.setState("closed");
				terminalBeans.setParentId(Integer.parseInt(terminalBeanList.get(i).getT_id()));
				terminalBeans.setText(terminalBeanList.get(i).getTerminal_name());
				list.add(terminalBeans);
			}
			List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
			treeBeansList = new CreateTree().formatTree(list);
		return treeBeansList;
	}
}