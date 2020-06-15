package util;

import java.io.File;
import java.util.Date;

public class GetScreenViewPath {
	private String mac;
	private String ftpLocalPath;
	public GetScreenViewPath(String mac,String ftpLocalPath){
		this.mac = mac;
		this.ftpLocalPath = ftpLocalPath+"screen";
	}
	
	public String getPath(){
		
		String imgFilePath = ftpLocalPath+"/"+mac;
		String path="0";
		File imgFile = new File(imgFilePath);
		long nowTime = new Date().getTime();
		if(imgFile.exists()){//判断文件夹是否存在
			File[] imgFileList = imgFile.listFiles();
			if(imgFileList.length>0){//判断文件夹中是否有文件
					path = mac+".jpg?"+nowTime;
			}
		}else{
			path = "0";
		}
		
		return path;
	}
	
	public long getLarge(long[] dateTime){
		for (int i = 0; i < dateTime.length -1; i++){    //最多做n-1趟排序
			for(int j = 0 ;j < dateTime.length - i - 1; j++){    //对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)
				if(dateTime[j] < dateTime[j + 1]){    //把小的值交换到后面
				long temp = dateTime[j];
				dateTime[j] = dateTime[j + 1];
				dateTime[j + 1] = temp;
				}
			} 
		}
		return dateTime[0];
	}
	
}
