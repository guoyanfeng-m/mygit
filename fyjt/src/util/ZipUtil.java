package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipUtil {

	public static void main(String[] args) {
		File f = new File("D:\\ftpFile\\systemUpload\\client36.zip");
		try {
			new ZipUtil().unZipFile("D:\\ftpFile\\systemUpload\\client36.zip", "D:\\ftpFile\\systemUpload\\mmsclient") ;
			
			f.delete();
			Thread.sleep(100000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @Description		解压zip文件
	 * @Date			2016年12月9日上午11:17:30
	 * @param unZipFileName	文件全路径名称
	 * @param unZipPath		文件解压路径名称
	 * @throws Exception
	 */
	public  void unZipFile(String unZipFileName, String unZipPath){
		System.out.println("开始解压缩文件");
		Date date = new Date();
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(unZipFileName);
			unZipFileByOpache(zipFile, unZipPath);
		} catch (Exception e) {
			System.out.println("zip文件获取错误！");
		}
		System.out.println("解压缩完成，用时"+(new Date().getTime()-date.getTime())+"毫秒");
	}
	
	/**
	 * @Description		文件进行解压
	 * @Date			2016年12月9日上午11:18:10
	 * @param zipFile	解压的文件
	 * @param unZipRoot	解压的路径
	 * @throws Exception
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	private static void unZipFileByOpache(ZipFile zipFile,
			String unZipRoot) throws Exception, IOException {
		Enumeration e = zipFile.entries();
		ZipEntry zipEntry;
		byte[] b = new byte[1024];
		while (e.hasMoreElements()) {
			zipEntry = (ZipEntry) e.nextElement();
			InputStream fis = zipFile.getInputStream(zipEntry);
			
			if (zipEntry.isDirectory()) {
			} else {
				File file = new File(unZipRoot + File.separator
						+ zipEntry.getName());
				File parentFile = file.getParentFile();
				parentFile.mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				
				int len;
				while ((len = fis.read(b, 0, b.length)) != -1) {
					fos.write(b, 0, len);
				}
				fos.flush();
				fos.close();
				fis.close();
			}
		}
		zipFile.close();
	}

}