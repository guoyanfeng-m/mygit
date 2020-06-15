package dao.notice;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.notice.NoticeBean;



@Repository
public interface NoticeDao {
	@SuppressWarnings("rawtypes")
	public List<NoticeBean> queryNotice(Map map);
	@SuppressWarnings("rawtypes")
	public List<NoticeBean> queryNoticeAll(Map map);
	public void updateNoticeById(NoticeBean noticeBean);
	public void insertNotice(NoticeBean noticeBean);
	public NoticeBean queryNoticeById(Integer id);
}
