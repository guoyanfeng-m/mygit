package serviceImpl.scrollingnews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.scrollingnews.ScrollingnewsService;
import util.PageInfo;
import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsBean;
import dao.scrollingnews.ScrollingnewsDao;
@Service
@Transactional(readOnly=true)
public class ScrollingnewsServiceImpl implements ScrollingnewsService {
	@Autowired
    private ScrollingnewsDao scrollDao;
    
	@Override
	@Transactional(readOnly=false)
	public void delScroll(Integer[] ids) {
		scrollDao.delScroll(ids);
	}

	@Override
	@Transactional(readOnly=false)
	public void insertScroll(ScrollingnewsBean scroll) {
		scrollDao.insertScroll(scroll);		
	}

	@Override
	public List<ScrollingnewsBean> queryAll(ScrollingnewsBean scroll,Integer creatorid ,List<Integer>userids) {
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("scroll", scroll);
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		return scrollDao.queryAll(map);
	}

	@Override
	public ScrollingnewsBean queryScroll(ScrollingnewsBean scroll) {
		return scrollDao.queryScroll(scroll);
	}

	@Override
	public void updateScroll(ScrollingnewsBean scroll) {
		scrollDao.updateScroll(scroll);		
	}
	@Override
	public List<ScrollTerminalView> queryView() {
		return scrollDao.queryView();
	}

	@Override
	public List<ScrollingnewsBean> queryPage(ScrollingnewsBean sBean, PageInfo pageInfo,Integer creatorid,List<Integer>userids) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("sBean", sBean);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		return scrollDao.queryPage(map);
	}
}
