package dao.role;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.role.ParametersRoleTerminalBean;
import beans.role.ParametersRoleTerminalGroupBean;
import beans.role.RoleBean;
import beans.role.RoleParametersBean;
import beans.role.RoleViewBean;
import beans.terminal.TerminalBean;

@SuppressWarnings("rawtypes")
@Repository
public interface  RoleDao {
	 public List<RoleViewBean> queryRole(Map map);
	 public void updateRoleByRoleId(RoleBean roleBean);
	 public void insertRole(RoleBean roleBean);
	 public void insertRolePower(RoleParametersBean pb);
	 public void insertRoleTerminal(ParametersRoleTerminalBean prtb);
	 public void insertRoleTerminalGroup(ParametersRoleTerminalGroupBean prtgbs);
	 public List<Integer> queryRoleID(Map map);
	 public List queryPowerByRoleID(int roleid);
	 public List queryTerminalByRoleID(int roleid);
	 public void deleteTerminalByRoleId(int roleid);
	 public void deleteTerminalByTerminalId(RoleParametersBean parametersBean);
	 public void deleteTerminalgroupByRoleId(int roleid);
	 public void deletePowerByRoleId(int roleid);
	 public Integer queryRoleCount(Map map);
	 public List queryShowRolePower(int roleid);
	 public List queryShowRolePowernext(int roleid);
	 public void deleteTerminalgroupByTerminalGroupId(int terminalgroup_id);
	 public List queryTerminalGroupByRoleID(int roleid);
	 public Integer checkHave(ParametersRoleTerminalGroupBean roleTerminalGroupBean);
	 //根据角色id查找角色用户关系表
	 public	List<Integer> queryUserRoleByRoid(int roleid);
	 public Integer queryScheduleLevelByRoleID(Integer userid);
	 //终端管理--终端添加--角色终端表入库
	 public void insertRoleTerminals(@Param("tersRoleTerminalList")List<ParametersRoleTerminalBean> tersRoleTerminalList);
	 //终端管理--终端添加--所有有此终端组权限的角色终端表入库
	 public void insertRoleAllTerminals(@Param("tersRoleTerminalList")List<ParametersRoleTerminalBean> tersRoleTerminalList);
	 //删除角色终端组表
	 public void deleteRoleTerminalGroups(@Param("roleTerminalGroupList")List<ParametersRoleTerminalGroupBean> roleTerminalGroupList);
	 //插入角色终端组表
	 public void insertRoleTerminalGroups(@Param("roleTerminalGroupList")List<ParametersRoleTerminalGroupBean> roleTerminalGroupList);
	 //删除角色终端表
	 public void deleteTerminalByTerminalIds(TerminalBean terminalBean);
}
