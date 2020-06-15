package beans.sys;

public class SystemConstant {
	/**
	 * 审批状态-待审批
	 */
	public static int APPROVE_STATUS_WAIT = 0;
	/**
	 * 审批状态-审批通过
	 */
	public static int APPROVE_STATUS_SUCCESS = 1;
	/**
	 * 审批状态-审批不通过
	 */
	public static int APPROVE_STATUS_FAILURE = 2;
	/**
	 * 是否刪除-已刪除
	 */
	public static int ISDELETED_TRUE = 1;
	/**
	 * 是否刪除-未刪除
	 */
	public static int ISDELETED_FALSE = 0;
	/**
	 * 用户管理模块在系统中的表示（2）
	 */
	public static int USER_ID = 2;
	/**
	 * 角色管理模块在系统中的表示（3）
	 */
	public static int ROLE_ID = 3;
	/**
	 * 素材模块在系统中的表示（4）
	 */
	public static int ELEM_ID = 4;
	/**
	 * 节目模块在系统中的表示（5）
	 */
	public static int PROGRAM_ID = 5;
	/**
	 * 滚动消息模块在系统中的表示（6）
	 */
	public static int SCROLINGNEWS_ID = 6;
	/**
	 * 日志模块在系统中的表示（7）
	 */
	public static int LOG_ID = 7;
	/**
	 * 终端管理模块在系统中的表示（8）
	 */
	public static int TERMINAL_ID = 8;
	/**
	 * 终端监控模块在系统中的表示（9）
	 */
	public static int MONITOR_ID = 9;
	/**
	 * 终端监控模块在系统中的表示（10）
	 */
	public static int SYSTEM_ID = 10;
	/**
	 * 是否发布-已发布
	 */
	public static int ISSEND_TRUE = 1;
	/**
	 *  是否发布-未发布
	 */
	public static int ISSEND_FALSE = 0;
	/**
	 * 是否需要审核-是
	 */
	public static int AUDIT_TRUE = 0;
	/**
	 *  是否需要审核-否
	 */
	public static int AUDIT_FALSE = 1;
	/**
	 *  天气组件xml文件存放路径
	 */
	public static String WEATHER_PATH="plugXmlFile/weather/";
	/**
	 *  天气组件效果图存放路径
	 */
	public static String WEATHER_IMG_PATH="ftpFile/plugXmlFile/img/weather/";
	/**
	 *  倒计时组件xml文件存放路径
	 */
	public static String COUNT_PATH="plugXmlFile/count/";
	/**
	 *  倒计时组件效果图存放路径
	 */
	public static String COUNT_IMG_PATH="ftpFile/plugXmlFile/img/count/";
	/**
	 *  时钟组件xml文件存放路径
	 */
	public static String TIME_PATH="plugXmlFile/time/";
	/**
	 *  时钟组件效果图存放路径
	 */
	public static String TIME_IMG_PATH="ftpFile/plugXmlFile/img/time/";
	/**
	 *  按钮样式xml文件存放路径
	 */
	public static String BUTTONSTYLE_PATH="plugXmlFile/buttonStyle/buttonStyle.xml";
	/**
	 *  按钮样式效果图存放路径
	 */
	public static String BUTTONSTYLE_IMG_PATH="ftpFile/plugXmlFile/img/button/";
	
   /**
    * 
    */
   public static int INSERT_OPERATION=1;     //增加操作
   public static int DELETE_OPERATION=2;     //删除操作
   public static int UPDATE_OPERATION=3;     //修改操作
   public static int AUDTI_OPERATION=4;      //审核操作
   public static int PUBLISH_OPERATION=5;    //发布操作
   public static int DEL_STOP_OPERATION=6;   //停止/删除命令操作
   public static int CLOSE_OPERATION=7;      //关闭终端操作
   public static int RESET_OPERATION=8;      //重启终端操作
   public static int RENEW_OPERATION=9;      //更新终端操作
   public static int IMPORT_LICNESE_OPERATION=10;  //导入Licens操作
   public static int EXPORT_OPERATION=11;          //导出操作
   public static int VOICE_OPERATION=12;           //音量设置操作
   public static int LOG_OPERATION=13;             //登陆
   public static int OPEN_OPERATION=14;           //开机
   public static int TIMING_CONTROL_OPERATION=15;           //定时自动开关机
}
