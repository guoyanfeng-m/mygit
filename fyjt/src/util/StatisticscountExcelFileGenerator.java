/**
 * 系统数据导出Excel 生成器
 * @version 1.0
 */
package util;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import beans.statistics.StatisticsViewBean;


public class StatisticscountExcelFileGenerator {
	private ArrayList<String> fieldName = null; // excel标题数据集

	private List<StatisticsViewBean>   fieldData = null; // excel数据内容,导出统计信息

	private HSSFWorkbook workBook = null;

	/**
	 * 构造器
	 * 
	 * @param fieldName
	 *            结果集的字段名
	 * @param data
	 */
	
	public StatisticscountExcelFileGenerator(ArrayList<String> fieldName,
			List<StatisticsViewBean> fieldData) {

		this.fieldName = fieldName;
		this.fieldData = fieldData;
	}

	public StatisticscountExcelFileGenerator() {
		super();
	}

	/**
	 * 创建HSSFWorkbook对象
	 * 
	 * @return HSSFWorkbook
	 * @throws UnsupportedEncodingException 
	 */
	public HSSFWorkbook createWorkbook() throws UnsupportedEncodingException {
		workBook = new HSSFWorkbook();// POI报表的核心对象，创建一个工作薄对象
		HSSFSheet sheet = workBook.createSheet();// 使用workBook创建sheet对象
		workBook.setSheetName(0,"素材统计信息",HSSFWorkbook.ENCODING_UTF_16);//设置sheet名称的编码
		HSSFRow headRow = sheet.createRow((short) 0);// 使用sheet对象创建row，row的下标从0开始
		for (int i = 0; i < fieldName.size(); i++) {
			HSSFCell cell = headRow.createCell((short) i);// headRow对象创建cell，cell的下标从0开始
			/***************** 对标题添加样式 ********************/
			// 设置列的宽度
			sheet.setColumnWidth((short) i, (short) 6000);
			// 调用样式的对象
			HSSFCellStyle cellStyle = workBook.createCellStyle();
			  // 背景色的设定   
			cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index);   
		    // 前景色的设定   
			cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);   
			// 调用字体对象
			HSSFFont font = workBook.createFont();
			// 设置标题的字体变粗
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("黑体");
			// 设置字体的颜色
			font.setColor(HSSFColor.BLACK.index);
			// 将设置后的Fort对象结果，设置到样式cellStyle对象中
			cellStyle.setFont(font);
			// 添加样式
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (fieldName.get(i) != null) {
				// 将设置后的样式对象cellStyle，设置到单元格中
				cell.setCellStyle(cellStyle);
				cell.setCellValue((String) fieldName.get(i));// 设置单元格的值
			} else {
				cell.setCellStyle(cellStyle);
				cell.setCellValue("-");
			}
		}
		HSSFCell cell0 = null;
		HSSFCell cell1 = null;
		HSSFCell cell2 = null;
		HSSFCell cell3 = null;
		for (int i = 0; i < fieldData.size(); i++) {
			HSSFRow row = sheet.createRow((short) (i + 1));// 使用sheet对象创建row，row的下标从0开始
			StatisticsViewBean count = fieldData.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(Short.valueOf("0"));
			cell1 = row.createCell(Short.valueOf("1"));
			cell2 = row.createCell(Short.valueOf("2"));
			cell3 = row.createCell(Short.valueOf("3"));


			// 设置字符集
			cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);


			cell0.setCellValue(count.getElemName());
			Integer val = count.getElemType();
			String type;
			if(val==1){
				type = "文本";
		   	}else if(val==2){
		   		type = "音频";
		   	}else if(val==3){
		   		type = "图片";
		   	}else if(val==4){
		   		type = "视频";
		   	}else if(val==5){
		   		type = "网页";
		   	}else if(val==6){
		   		type = "Falsh";
		   	}else if(val==7){
		   		type = "Office";
		   	}else if(val==8){
		   		type = "流媒体";
		   	}else{
		   		type = "未知格式文件";
		   	}
			cell1.setCellValue(type);
			cell2.setCellValue(count.getTotaltime());
			cell3.setCellValue(count.getCounts());


			sheet.setColumnWidth((short) 0, (short) 6000);
			sheet.setColumnWidth((short) 1, (short) 4000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 4000);

			
		}
		return workBook;
	}

	public void expordExcel(OutputStream os) throws Exception {
		workBook = createWorkbook();
//		FileOutputStream tempStream = new FileOutputStream("E:\\操作日志jjc.xls");
//		workBook.write(tempStream);
//		tempStream.close();
		workBook.write(os);// 使用excel的格式进行输出
//		os.flush();
		os.close();
	}

}
