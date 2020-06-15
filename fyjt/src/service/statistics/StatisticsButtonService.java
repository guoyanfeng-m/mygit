package service.statistics;

import java.sql.Timestamp;
import java.util.List;

import util.PageInfo;
import beans.statistics.StatisticsButtonBean;
import beans.statistics.StatisticsButtonView;
public interface StatisticsButtonService {
	  public List<StatisticsButtonBean> queryPage(String sceneNameofButton,String sceneNameofJumpbutton,String buttonName,String buttonType,Timestamp starttime,Timestamp endtime,PageInfo pageInfo);
	  public List<StatisticsButtonBean> exportData(String sceneNameofButton,String sceneNameofJumpbutton,String buttonType,Timestamp starttime,Timestamp endtime,int startCount,int endCount);
	  public int queryTotal(String sceneNameofButton,String sceneNameofJumpbutton,String buttonName,String buttonType,Timestamp starttime,Timestamp endtime);
	  public int queryStatisticsButtonPartCount(String sceneNameofButton,String sceneNameofJumpbutton,String buttonType,Timestamp starttime,Timestamp endtime);
	  public boolean delstatis(Integer statisticsDelete);  //自动删除统计
	  public void delStatistics(Timestamp starttime,Timestamp endtime); //手动删除统计
	  //触摸统计
	  public List<StatisticsButtonView> queryCMTotal(String sceneNameofButton,String sceneNameofJumpbutton,String buttonName,String buttonType,Timestamp starttime,Timestamp endtime);
	  public List<StatisticsButtonView> queryCMtj(String sceneNameofButton,String sceneNameofJumpbutton,String buttonName,String buttonType,Timestamp starttime,Timestamp endtime,PageInfo pageInfo);
	  //触摸统计导出
	  public List<StatisticsButtonView> exportCMExcel(String sceneNameofButton,String sceneNameofJumpbutton,String buttonType,Timestamp starttime,Timestamp endtime);
}
