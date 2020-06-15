package dao.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.user.UserBean;
import beans.user.UserParametersBean;
import beans.user.UserViewBean;
@SuppressWarnings("rawtypes")
@Repository
public interface  UserDao {
	 public List<UserViewBean> queryUser(Map map);
	 public void insertUser(UserBean userBean);
	 public List queryUserID(Map map);
	 public void insertUserRole( UserParametersBean pb);
	 public Integer queryRoleIDByUserID(int userid);
	 public void updateUserByUserID(UserBean userBean);
	 public void updateUserRoleByUserID(UserParametersBean pb);
//	 public Integer queryUserCount(@Param(value="list") List<String> userlist,@Param(value="userBean") UserBean userBean);
	 public Integer queryUserCount(Map map);
	 public List queryLoginUser(Map map);
	 public List queryChildUserID(String userid);
	 public	Integer queryParentUserID(Integer userid);
	 
	 public Integer delRoleUser(int user_id);
	 public List<Integer> queryUserIdsBySameRole(Map<String, Integer> map);
	 /*
	  * 查找相应用户的所有角色
	  */
	 public Collection<? extends Integer> queryRoleIDByUserIDs(@Param("userlist") List<Integer> userlist);
}
