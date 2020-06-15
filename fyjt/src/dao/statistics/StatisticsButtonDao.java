package dao.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.statistics.StatisticsButtonBean;
import beans.statistics.StatisticsButtonView;
@SuppressWarnings("rawtypes")
@Repository
public interface StatisticsButtonDao {
	//触摸条件查询
     public List<StatisticsButtonBean> queryPage(Map map);
     //触摸导出
     public List<StatisticsButtonBean> exportData(Map map);
     public int queryTotal(Map map);
     //触摸分段查询
     public int queryStatisticsButtonPartCount(Map map);
     
     //触摸统计
     public List<StatisticsButtonView> queryCMTotal(Map map);
     public List<StatisticsButtonView> queryCMtj(Map map);
     //触摸统计导出
     public List<StatisticsButtonView> exportCMExcel(Map map);
     
     public boolean delstatisButton(Integer statisticsDelete);  //自动删除统计
     public void delStatisticsButton(Map map); //手动删除统计
}
