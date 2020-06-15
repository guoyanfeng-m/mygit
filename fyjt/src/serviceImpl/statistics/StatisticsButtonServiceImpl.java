package serviceImpl.statistics;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.statistics.StatisticsButtonService;
import util.PageInfo;
import beans.statistics.StatisticsButtonBean;
import beans.statistics.StatisticsButtonView;
import dao.statistics.StatisticsButtonDao;
@Service
@Transactional(readOnly=true)
public class StatisticsButtonServiceImpl implements StatisticsButtonService{
	@Autowired
	private StatisticsButtonDao statisticsButtonDao;
	
    //触摸导出
	@Override
	public List<StatisticsButtonBean> exportData(String sceneNameofButton,String sceneNameofJumpbutton,
		    String buttonType,
			Timestamp starttime, Timestamp endtime, int startCount, int endCount) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		//map.put("buttonName", buttonName);
		map.put("buttonType", buttonType);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("offset", startCount);
		map.put("limit", endCount);
		return statisticsButtonDao.exportData(map);
	}
    //触摸查询
	@Override
	public List<StatisticsButtonBean> queryPage(String sceneNameofButton,String sceneNameofJumpbutton,
			String buttonName, String buttonType,
			Timestamp starttime, Timestamp endtime, PageInfo pageInfo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		map.put("buttonName", buttonName);
		map.put("buttonType", buttonType);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return statisticsButtonDao.queryPage(map);
	}
    //触摸查询总数
	@Override
	public int queryTotal(String sceneNameofButton,String sceneNameofJumpbutton, String buttonName,
			String buttonType,Timestamp starttime,Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		map.put("buttonName", buttonName);
		map.put("buttonType", buttonType);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		return statisticsButtonDao.queryTotal(map);
	}
    //触摸分段导出查询
	@Override
	public int queryStatisticsButtonPartCount(
			String sceneNameofButton, String sceneNameofJumpbutton,String buttonType,
		    Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		map.put("buttonType", buttonType);
	//	map.put("buttonName", buttonName);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		return statisticsButtonDao.queryStatisticsButtonPartCount(map);
	}
    //手动删除统计数据
	@Override
	@Transactional(readOnly=false)
	public void delStatistics(Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		statisticsButtonDao.delStatisticsButton(map);
	}
    //自动删除统计数据
	@Override
	@Transactional(readOnly=false)
	public boolean delstatis(Integer statisticsDelete) {
		return statisticsButtonDao.delstatisButton(statisticsDelete);
	}
    //触摸统计
	@Override
	public List<StatisticsButtonView> queryCMTotal(String sceneNameofButton,String sceneNameofJumpbutton,
			String buttonName, String buttonType, Timestamp starttime,
			Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("buttonType", buttonType);
		map.put("buttonName", buttonName);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		return statisticsButtonDao.queryCMTotal(map);
	}
    //触摸统计总数
	@Override
	public List<StatisticsButtonView> queryCMtj(String sceneNameofButton,String sceneNameofJumpbutton,
			String buttonName, String buttonType, Timestamp starttime,
			Timestamp endtime,PageInfo pageInfo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("buttonType", buttonType);
		map.put("buttonName", buttonName);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getRows());
		return statisticsButtonDao.queryCMtj(map);
	}

	@Override
	public List<StatisticsButtonView> exportCMExcel(String sceneNameofButton,String sceneNameofJumpbutton,
			String buttonType, Timestamp starttime, Timestamp endtime) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sceneNameofButton", sceneNameofButton);
		map.put("sceneNameofJumpbutton", sceneNameofJumpbutton);
		map.put("buttonType", buttonType);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		return statisticsButtonDao.exportCMExcel(map);
	}
}
