package service.role;

import java.util.List;

import util.PageInfo;
import beans.role.ParametersRoleTerminalBean;
import beans.role.ParametersRoleTerminalGroupBean;
import beans.role.RoleBean;
import beans.role.RoleParametersBean;
import beans.role.RoleViewBean;
@SuppressWarnings("rawtypes")
public interface RoleService {
	 List<RoleViewBean> queryRole(List<String> userlist,RoleBean roleBean,PageInfo pageInfo, Integer creatorid, List<Integer> userids);
	 void updateRoleByRoleId(RoleBean roleBean);
	 void insertRole(RoleBean roleBean);
	 void insertRolePower(RoleParametersBean pb);
	 List<Integer> queryRoleID(String rolename,int creatid);
	 void insertRoleTerminal(ParametersRoleTerminalBean prtb);
	 void insertRoleTerminalGroup(ParametersRoleTerminalGroupBean prtgbs);
	 List queryPowerByRoleID (int roleid);
	 List queryTerminalByRoleID(int roleid);
	 void deleteTerminalByRoleId(int roleid);
	 void deleteTerminalgroupByRoleId(int roleid);
	 void deletePowerByRoleId(int roleid);
	 Integer queryRoleCount(List<String> userlist,RoleBean roleBean, Integer creatorid, List<Integer> userids);
	 List showrolepower(int roleid);
	 List queryTerminalGroupByRoleID(int roleid);
	 Integer queryScheduleLevelByRoleID(Integer userid);
	 
	 //根据角色id查找角色用户关系表
	 public	List queryUserRoleByRoid(int roleid);
	 
}
