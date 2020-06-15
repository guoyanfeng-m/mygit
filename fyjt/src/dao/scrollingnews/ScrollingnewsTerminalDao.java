package dao.scrollingnews;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsTerminalBean;
@Repository
public interface ScrollingnewsTerminalDao {
	public List<ScrollingnewsTerminalBean> queryScrollTerminal(ScrollingnewsTerminalBean scrollterminal);      //条件查询
	public List<ScrollingnewsTerminalBean> queryAll();                                 //全查询
	public void insertScrollTerminal(ScrollingnewsTerminalBean scrollterminal);        //添加
	public void delScrollTerminal(ScrollingnewsTerminalBean scrollterminal);                                     //删除
	@SuppressWarnings("rawtypes")
	public List<ScrollTerminalView> queryPage(Map map);   //分页查询
}
