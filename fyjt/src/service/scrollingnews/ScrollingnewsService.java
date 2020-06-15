package service.scrollingnews;

import java.util.List;

import util.PageInfo;
import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsBean;
public interface ScrollingnewsService {
	public ScrollingnewsBean queryScroll(ScrollingnewsBean scroll);  //条件查询
	public List<ScrollingnewsBean> queryAll(ScrollingnewsBean scroll, Integer creatorid, List<Integer> userids); //全查询和模糊查询
	public void insertScroll(ScrollingnewsBean scroll);
	public void delScroll(Integer[] ids);
	public void updateScroll(ScrollingnewsBean scroll);
	public List<ScrollTerminalView> queryView();
    public List<ScrollingnewsBean> queryPage(ScrollingnewsBean sBean, PageInfo pageInfo,Integer creatorid, List<Integer> userids);
}
