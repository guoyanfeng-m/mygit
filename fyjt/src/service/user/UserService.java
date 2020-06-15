package service.user;

import java.util.List;

import util.PageInfo;
import beans.user.UserBean;
import beans.user.UserParametersBean;
import beans.user.UserViewBean;
public interface UserService {
	 List<UserViewBean> queryUser(List<String> userlist,UserBean userBean,PageInfo pageInfo, Integer creatorid, List<Integer> userids);
	 void insertUser(UserBean userBean);
	 // List<Integer> queryUserID(String username,int creatid);
	 List<Integer> queryUserID(String username);
	 void insertUserRole(UserParametersBean pb);
	 Integer queryRoleIDByUserID(int userid);
	 void updateUserByUserId(UserBean userBean);
	 void updateUserRoleByUserID(UserParametersBean pb);
	 Integer queryUserCount(List<String> userlist,UserBean userBean, Integer creatorid, List<Integer> userids); 
	 List<Integer> queryLoginUser(String username, String password);
	 List<String> queryChildUserID(String userid);
	 List<String> queryChilds(List<String> list);
	 Integer queryParentUserID(Integer userid);
  
	 public Integer delRoleUser(int user_id);
	 List<Integer> queryUserIdsBySameRole(Integer userId);//查找相同角色的所有用户id
}
