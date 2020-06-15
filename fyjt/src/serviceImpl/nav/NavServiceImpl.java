package serviceImpl.nav;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.nav.NavService;
import util.PageInfo;
import beans.nav.NavBean;
import dao.nav.NavDao;
@Service
@Transactional(readOnly=true)
public class NavServiceImpl implements NavService{
	@Autowired
	private NavDao navDao;

	@Override
	public List<NavBean> queryNavPid() {
		List<NavBean> list = navDao.queryNavPid();
		return list;
	}
	
	@Override
	public List<NavBean> queryNavByPId(Integer pid){
		List<NavBean> list = navDao.queryNavByPId(pid);
		return list;
	}
	
	@Override
	public NavBean queryNavById(Integer id){
		NavBean navBean = navDao.queryNavById(id);
		return navBean;
	}
	
	@Override
	public List<NavBean> queryNav(PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		List<NavBean> list = navDao.queryNav(map);
		return list;
	}

	@Override
	public List<NavBean> queryNavAll() {
		List<NavBean> list = navDao.queryNavAll();
		return list;
	}

	@Override
	@Transactional(readOnly=false)
	public void insertNav(NavBean navBean) {
		navDao.insertNav(navBean);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void updateNavById(NavBean navBean) {
		navDao.updateNavById(navBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteNavById(int id) {
		navDao.deleteNavById(id);
	}

	
}
