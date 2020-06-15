package service.scrollingnews;

import java.util.List;

import util.PageInfo;
import beans.scrollingnews.ScrollTerminalView;
import beans.scrollingnews.ScrollingnewsTerminalBean;
public interface ScrollingnewsTerminalService {
    public List<ScrollingnewsTerminalBean> queryScrollTerminal(ScrollingnewsTerminalBean scrollterminal);      //条件查询
	public List<ScrollingnewsTerminalBean> queryAll();                                 //全查询
	public void insertScrollTerminal(ScrollingnewsTerminalBean scrollterminal);        //添加
	public void delScrollTerminal(ScrollingnewsTerminalBean scrollterminal);                                     //删除
	public List<ScrollTerminalView> queryPage(PageInfo pageInfo);
}
