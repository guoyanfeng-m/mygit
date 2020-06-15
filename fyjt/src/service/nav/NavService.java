package service.nav;

import java.util.List;

import org.springframework.stereotype.Service;

import util.PageInfo;
import beans.nav.NavBean;


/**
 * @author gyf
 *
 */
@Service
public interface NavService {
	
	
	public List<NavBean> queryNavPid(); 
	public List<NavBean> queryNavByPId(Integer pid);
	public NavBean queryNavById(Integer id);
	List<NavBean> queryNav(PageInfo pageInfo);
	public List<NavBean> queryNavAll(); //全查询和模糊查询
	void updateNavById(NavBean navBean);
	void insertNav(NavBean navBean);
	void deleteNavById(int id);

}
