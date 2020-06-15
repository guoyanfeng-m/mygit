package dao.scrollingnews;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsBean;
@Repository
public interface ScrollingnewsDao {
	public ScrollingnewsBean queryScroll(ScrollingnewsBean scroll);      //条件查询
	public List<ScrollingnewsBean> queryAll(Map<String,Object> map);         //全查询
	public void insertScroll(ScrollingnewsBean scroll);                        //添加
	public void delScroll(Integer[] ids);                                     //删除
	public void updateScroll(ScrollingnewsBean scroll);                        //修改
	public List<ScrollTerminalView> queryView();                              //关联查询
    @SuppressWarnings("rawtypes")
	public List<ScrollingnewsBean> queryPage(Map map);                       //分页查询
}
