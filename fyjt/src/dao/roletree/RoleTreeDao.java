package dao.roletree;

import java.util.List;

import org.springframework.stereotype.Repository;

import beans.roletree.RoletreeParametersBean;

@SuppressWarnings("rawtypes")
@Repository
public interface  RoleTreeDao {
	 public List queryRoleTerminalgroup(RoletreeParametersBean pb);
	 public List queryRoleTerminal(RoletreeParametersBean pb);
	 public List queryRoleTerminalgroupByRoleID(int roleid);
	 public List queryRoleTerminalByRoleID(int roleid);
	 
}
