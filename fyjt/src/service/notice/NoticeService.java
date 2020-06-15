/**
 * Description:
 * author:zhen
 * 2016年3月23日 下午5:38:43
 */
package service.notice;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import util.PageInfo;
import beans.notice.NoticeBean;



/**
 * @author gyf
 *
 */
@Service
public interface NoticeService {
	
	List<NoticeBean> queryNotice(String sName,String sStatus, PageInfo pageInfo);
	@SuppressWarnings("rawtypes")
	public List<NoticeBean> queryNoticeAll(Map map);
	void updateNoticeById(NoticeBean noticeBean);
	void insertNotice(NoticeBean noticeBean);
	NoticeBean queryNoticeById(Integer id);
}
