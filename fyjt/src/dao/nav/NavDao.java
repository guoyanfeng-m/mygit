package dao.nav;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.nav.NavBean;
@Repository
public interface NavDao {
	
	public List<NavBean> queryNavPid();
	public List<NavBean> queryNavByPId(Integer pid);
	public NavBean queryNavById(Integer pid);
	@SuppressWarnings("rawtypes")
	public List<NavBean> queryNav(Map map);
	public List<NavBean> queryNavAll();
	public void updateNavById(NavBean navBean);
	public void insertNav(NavBean nav);
	public void deleteNavById(int id);
}
