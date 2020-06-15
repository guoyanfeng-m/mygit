package util;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import service.config.ConfigService;
import service.operationlog.OperationlogService;
import service.statistics.StatisticsButtonService;
import service.statistics.StatisticsService;
import beans.operationlog.OperationlogView;
@Component
public class LogListener{
	@Autowired
	private ConfigService configService;
	@Autowired
	private OperationlogService operationlogService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private StatisticsButtonService statisticsServiceButton;
	public LogListener() {
	}
	/****
	 * 自动删除操作日志
	 * @throws IOException
	 */
   //@Scheduled(cron="3 * * * * ?")   //每天中午12点触发
     @Scheduled(cron="0 0 8,14,23 * * ?")   //每天上午10点，下午14点，23点
	 public void  truncateUserLog(){
	 System.out.println("自动删除操作开始执行！");
     String logDelete=configService.queryConfig("logDelete");
     List<OperationlogView> list=operationlogService.findOperationlog(Integer.parseInt(logDelete));
     if(list.size()>0){
	     @SuppressWarnings("unused")
		boolean flag1=operationlogService.delOperationlog(Integer.parseInt(logDelete));
     }
     //自动删除统计日志
     String statisticsDelete=configService.queryConfig("statisticsDelete");
     @SuppressWarnings("unused")
	boolean flag2=statisticsService.delstatis(Integer.parseInt(statisticsDelete));
     
     //自动删除触摸统计数据
     @SuppressWarnings("unused")
	boolean flag3=statisticsServiceButton.delstatis(Integer.parseInt(statisticsDelete));
     
     System.out.println("自动删除统计执行成功！");
  }
}
