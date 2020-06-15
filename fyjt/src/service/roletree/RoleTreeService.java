package service.roletree;

import java.util.List;

import beans.roletree.RoletreeParametersBean;

@SuppressWarnings("rawtypes")
public interface RoleTreeService {
	 List queryRoleTerminalgroup(RoletreeParametersBean pb);
	 List queryRoleTerminalByRoleID(int roleid);
}
