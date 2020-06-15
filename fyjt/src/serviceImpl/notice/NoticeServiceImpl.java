package serviceImpl.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import beans.notice.NoticeBean;
import dao.notice.NoticeDao;
import service.notice.NoticeService;
import util.PageInfo;

@Service
@Transactional(readOnly=true)
public class NoticeServiceImpl implements NoticeService{
	@Autowired
	private NoticeDao noticeDao;

	@Override
	public List<NoticeBean> queryNotice(String sName,String sStatus, PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		map.put("sName", sName);
		map.put("sStatus", sStatus);
		List<NoticeBean> list = noticeDao.queryNotice(map);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NoticeBean> queryNoticeAll(Map map) {
		List<NoticeBean> list = noticeDao.queryNoticeAll(map);
		return list;
	}
	
	@Override
	public NoticeBean queryNoticeById(Integer id){
		NoticeBean notice=noticeDao.queryNoticeById(id);
		return notice;
	}

	@Override
	public void updateNoticeById(NoticeBean noticeBean) {
		noticeDao.updateNoticeById(noticeBean);
		
	}

	@Override
	public void insertNotice(NoticeBean noticeBean) {
		noticeDao.insertNotice(noticeBean);
		
	}

	
}
