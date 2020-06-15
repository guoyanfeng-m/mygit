package serviceImpl.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.role.RoleService;
import util.CreateTree;
import util.PageInfo;
import beans.role.ParametersRoleTerminalBean;
import beans.role.ParametersRoleTerminalGroupBean;
import beans.role.ResultShowRolePowerBean;
import beans.role.ResultShowRolePowernextBean;
import beans.role.RoleBean;
import beans.role.RoleParametersBean;
import beans.role.RoleViewBean;
import beans.sys.TreeBeans;
import dao.role.RoleDao;

@SuppressWarnings({"rawtypes","unchecked","static-access"})
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<RoleViewBean> queryRole(List<String> userlist,RoleBean roleBean,PageInfo pageInfo,Integer creatorid,List<Integer>userids) {
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("list", userlist);
		map.put("roleBean", roleBean);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		List<RoleViewBean> list = roleDao.queryRole(map);
		return list;
	}

	@Override
	@Transactional(readOnly=false)
	public void updateRoleByRoleId(RoleBean roleBean) {
		roleDao.updateRoleByRoleId(roleBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertRole(RoleBean roleBean) {
		roleDao.insertRole(roleBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertRolePower(RoleParametersBean pb) {
		roleDao.insertRolePower(pb);
		
	}

	@Override
	public List<Integer> queryRoleID(String rolename,int creatid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rolename", rolename);
		map.put("creatid", creatid);
		return roleDao.queryRoleID(map);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertRoleTerminal(ParametersRoleTerminalBean prtb) {
		roleDao.insertRoleTerminal(prtb);		
	}

	@Override
	@Transactional(readOnly=false)
	public void insertRoleTerminalGroup(ParametersRoleTerminalGroupBean prtgbs) {
		roleDao.insertRoleTerminalGroup(prtgbs);
	}

	@Override
	public List queryPowerByRoleID(int roleid) {
		return roleDao.queryPowerByRoleID(roleid);
	}

	@Override
	public List queryTerminalByRoleID(int roleid) {
		return roleDao.queryTerminalByRoleID(roleid);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteTerminalByRoleId(int roleid) {
		 roleDao.deleteTerminalByRoleId(roleid);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteTerminalgroupByRoleId(int roleid) {
		roleDao.deleteTerminalgroupByRoleId(roleid);		
	}

	@Override
	@Transactional(readOnly=false)
	public void deletePowerByRoleId(int roleid) {
		roleDao.deletePowerByRoleId(roleid);
	}

	@Override
	public Integer queryRoleCount(List<String> userlist,RoleBean roleBean,Integer creatorid,List<Integer>userids) {
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("list", userlist);
		map.put("roleBean", roleBean);
		map.put("userids", userids);
		map.put("creatorid", creatorid);
		return roleDao.queryRoleCount(map);
	}

	@Override
	public List showrolepower(int roleid) {
		List<ResultShowRolePowerBean> RolePowerBeanList = new ArrayList<ResultShowRolePowerBean>();
		RolePowerBeanList=roleDao.queryShowRolePower(roleid);
		List<ResultShowRolePowernextBean> RolePowerBeanListnext = new ArrayList<ResultShowRolePowernextBean>();
		RolePowerBeanListnext=roleDao.queryShowRolePowernext(roleid);
		
		 List<TreeBeans> list = new ArrayList<TreeBeans>(); 
			for(int i=0;i<RolePowerBeanList.size();i++){
				TreeBeans treeBeans = new TreeBeans();
				treeBeans.setId(RolePowerBeanList.get(i).getModule_id());
				treeBeans.setParentId(RolePowerBeanList.get(i).getParentID());
				treeBeans.setText(RolePowerBeanList.get(i).getModule_name());
				list.add(treeBeans);
			}
			for(int i=0;i<RolePowerBeanListnext.size();i++){
				TreeBeans terminalBeans = new TreeBeans();
				terminalBeans.setId(RolePowerBeanListnext.get(i).getT_id()+100);
				terminalBeans.setParentId(RolePowerBeanListnext.get(i).getModuleID());
				terminalBeans.setText(RolePowerBeanListnext.get(i).getPowerName());
				list.add(terminalBeans);
			}
			List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
			treeBeansList = new CreateTree().formatTree(list);
		return treeBeansList;
	}

	@Override
	public List queryTerminalGroupByRoleID(int roleid) {
		return roleDao.queryTerminalGroupByRoleID(roleid);
	}

	@Override
	public List queryUserRoleByRoid(int roleid) {
		return roleDao.queryUserRoleByRoid(roleid);
	}
	
	@Override
	public Integer queryScheduleLevelByRoleID(Integer userid) {
		return roleDao.queryScheduleLevelByRoleID(userid);
	}
}
