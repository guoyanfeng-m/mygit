package serviceImpl.scrollingnews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.scrollingnews.ScrollingnewsTerminalService;
import util.PageInfo;
import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsTerminalBean;
import dao.scrollingnews.ScrollingnewsTerminalDao;
@Service
@Transactional(readOnly=true)
public class ScrollingnewsTerminalServiceImpl implements ScrollingnewsTerminalService {
	@Autowired
    private ScrollingnewsTerminalDao scrollTerminalDao;
    

	@Override
	@Transactional(readOnly=false)
	public void delScrollTerminal(ScrollingnewsTerminalBean scrollterminal) {
		scrollTerminalDao.delScrollTerminal(scrollterminal);		
	}

	@Override
	@Transactional(readOnly=false)
	public void insertScrollTerminal(ScrollingnewsTerminalBean scrollterminal) {
		scrollTerminalDao.insertScrollTerminal(scrollterminal);
	}

	@Override
	public List<ScrollingnewsTerminalBean> queryAll() {
		return scrollTerminalDao.queryAll();
	}

	@Override
	public List<ScrollingnewsTerminalBean> queryScrollTerminal(
			ScrollingnewsTerminalBean scrollterminal) {
		return scrollTerminalDao.queryScrollTerminal(scrollterminal);
	}

	@Override
	public List<ScrollTerminalView> queryPage(PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return scrollTerminalDao.queryPage(map);
	}
}
