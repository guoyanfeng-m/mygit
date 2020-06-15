package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static void main(String[] args) {

		String s = "D:/ftpFile/processed/1_f6354cc90bb9a47a37083055980c348f";
		File file = new File(s);
		System.out.println(file.getParent());
		// FileUtil.delFolder(s);
	}

	/**
	 * @Description 单个文件上传写入功能
	 * @Date 2016年12月9日上午10:55:31
	 * @param is
	 *            传入的流
	 * @param fileName
	 *            存储文件名称
	 * @param filePath
	 *            文件名保存路径
	 * @return 失败返回1，成功返回路径
	 */
	public static String upFile(InputStream is, String fileName, String filePath) {
		// 初始化参数来判断是否写入成功
		String files = "1";
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		// 判断文件夹是否存在
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 获取写入文件位置
		File f = new File(filePath + "/" + fileName);
		try {
			bis = new BufferedInputStream(is);
			fos = new FileOutputStream(f);
			bos = new BufferedOutputStream(fos);
			byte[] bt = new byte[4096];
			int len;
			while ((len = bis.read(bt)) > 0) {
				bos.write(bt, 0, len);
			}
			// 写入成功把路径赋值
			files = f.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (null != bos) {
					bos.close();
					bos = null;
				}
				if (null != fos) {
					fos.close();
					fos = null;
				}
				if (null != is) {
					is.close();
					is = null;
				}
				if (null != bis) {
					bis.close();
					bis = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return files;
	}

	/**
	 * 遍历文件获取文件下所有文件的名称（包括文件夹名称 素材中存储文件夹的是office文件截取的图片不用获取其中的文件）
	 * 
	 * @param path
	 *            遍历的文件
	 */
	public static List<String> getFile(String path) {
		// 获取文件名称
		List<String> fileNameList = new ArrayList<String>();
		// 过去文件路径
		File file = new File(path);
		// 获取文件路径下的所有文件
		File[] array = file.listFiles();
		// 存储文件名
		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				// 文件名
				fileNameList.add(array[i].getName());
				// 获取文件路径名称
			} else if (array[i].isDirectory()) {
				// 文件夹名称
				fileNameList.add(array[i].getName());
			}
		}
		return fileNameList;
	}

}
