package serviceImpl.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.user.UserService;
import util.PageInfo;
import beans.user.UserBean;
import beans.user.UserParametersBean;
import beans.user.UserViewBean;
import dao.user.UserDao;

@SuppressWarnings({"rawtypes","unchecked"})
@Service
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;


	@Override
	@Transactional(readOnly=false)
	public void insertUser(UserBean userBean) {
		userDao.insertUser(userBean);
	}

	@Override
	public List<UserViewBean> queryUser(List<String> userlist,UserBean userBean,PageInfo pageInfo,Integer creatorid,List<Integer>userids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userlist);
		map.put("userBean", userBean);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		List<UserViewBean> list = userDao.queryUser(map);
		return list;
	}

	@Override
	public List<Integer> queryUserID(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
	//	map.put("creatid", creatid);
		return userDao.queryUserID(map);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertUserRole(UserParametersBean pb) {
		userDao.insertUserRole(pb);
	}

	@Override
	public Integer queryRoleIDByUserID(int userid) {
		return userDao.queryRoleIDByUserID(userid);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateUserByUserId(UserBean userBean) {
		userDao.updateUserByUserID(userBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateUserRoleByUserID(UserParametersBean pb) {
		userDao.updateUserRoleByUserID(pb);
	}

	@Override
	public Integer queryUserCount(List<String> userlist,UserBean userBean,Integer creatorid,List<Integer>userids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userlist);
		map.put("userBean", userBean);
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		return userDao.queryUserCount(map);
	}

	@Override
	public List<Integer> queryLoginUser(String username, String password) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("username", username);
		map.put("password", password);
		return userDao.queryLoginUser(map);
	}

	@Override
	public List<String> queryChildUserID(String userid) {
		return userDao.queryChildUserID(userid);
	}

	@Override
	public List<String> queryChilds(List<String> list) {
		List<String> alluser = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			List<String> rv = new ArrayList<String>();
			rv=userDao.queryChildUserID(list.get(i));
			alluser.addAll(rv);
		}
		return alluser;
	}

	@Override
	public Integer queryParentUserID(Integer userid) {
		return userDao.queryParentUserID(userid);
	}

	@Override
	@Transactional(readOnly=false)
	public Integer delRoleUser(int user_id) {
		return userDao.delRoleUser(user_id);
	}

	@Override
	public List<Integer> queryUserIdsBySameRole(Integer userId) {
		Map<String,Integer>map=new HashMap();
		map.put("userid", userId);
		return userDao.queryUserIdsBySameRole(map);
	}

	
}
