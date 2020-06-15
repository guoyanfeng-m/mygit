package util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;

/**
 * 获取缩略图
 * @author weihuiming
 *
 */
public class ElementUtil {

	/**
	 * 获取扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(splitIndex + 1);
	}

	/**
	 * 截取缩略图大小
	 * @param file		文件.
	 * @param imgPath	图片路径
	 * @param width		要缩小的宽度
	 * @param height	要缩小的高度
	 * @param source	缩小的文件
	 * @param name		缩小后文件名前缀
	 * @throws IOException 
	 */
	public static void createThumb(File file, String imgPath, float width,
			float height, File source, String name ,String imgUrl) throws IOException {
		JNative jnative;
		int vols1 = 0;
		//String paths = path() + File.separator + "libtumbnail.dll";
		//System.out.println(paths);
		try {
			jnative = new JNative(imgUrl  + File.separator + "filestorage\\libtumbnail.dll", "make_image_tumbnail");
			try {
				jnative.setParameter(0, Type.STRING, imgPath);
				jnative.setParameter(1, Type.STRING, source.getParentFile()
						.getAbsolutePath()
						+ "\\" + name + "img" + source.getName());
				jnative.setParameter(2, Type.INT, String.valueOf(width));
				jnative.setParameter(3, Type.INT, String.valueOf(height));
				jnative.setRetVal(Type.INT);
				jnative.invoke();
				vols1 = Integer.parseInt(jnative.getRetVal());
				// 0 false; 1 true;
				if (vols1 == 0) {
					FileUtils.copyFile(new File(source.getParent()
							+ File.separator + "fail.png"), new File(source
							.getParent()
							+ File.separator + "img" + source.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 不同大小的缩略图
	 * @param imgPath
	 * @throws IOException
	 */
	public static void thumImg(String imgPath,String imgUrl) throws IOException {
		args(100.0000, imgPath, "mini_" ,imgUrl);
		args(300.0000, imgPath, "" ,imgUrl);
		args(800.0000, imgPath, "large_" ,imgUrl);

	}
	
	/**
	 * 
	 * @param d				缩略图的尺寸
	 * @param imgPath		图片的路径
	 * @param name			缩略图的后缀名
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void args(double d, String imgPath, String name, String imgUrl)
			throws IOException {
		File source = new File(imgPath);
		File file = new File(".");
		String fileName = source.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());

		JNative jnative;
		int vols = 0;
		int vols1 = 0;
		try {
			jnative = new JNative(imgUrl  + File.separator + "filestorage\\libtumbnail.dll", "make_image_cmyk2rgb");
			try {
				jnative.setParameter(0, Type.STRING, imgPath);
				jnative.setRetVal(Type.INT);
				jnative.invoke();
				vols = Integer.parseInt(jnative.getRetVal());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (fileType.toLowerCase().equals("jpg")) {
			ImageInputStream iis = null;
			try {
				Iterator<ImageReader> readers = ImageIO
						.getImageReadersByFormatName("jpg");
				ImageReader reader = (ImageReader) readers.next();
				iis = ImageIO.createImageInputStream(source);
				reader.setInput(iis, true);
				int width = reader.getWidth(0);
				int height = reader.getHeight(0);
				float arg = 0;
				if (height >= width) {
					arg = (float) (d / height);
					height = (int) d;
					width = new Float(width * arg).intValue();
				} else {

					arg = (float) (d / width);
					width = (int) d;
					height = new Float(arg * height).intValue();
				}
				createThumb(file, imgPath, width, height, source, name ,imgUrl);

			} catch (IOException e) {
				Image srcFile = ImageIO.read(source);
				int width = srcFile.getWidth(null);
				int height = srcFile.getHeight(null);
				float arg = 0;
				if (height >= width) {
					arg = (float) (d / height);
					height = (int) d;
					width = new Float(width * arg).intValue();
				} else {

					arg = (float) (d / width);
					width = (int) d;
					height = new Float(arg * height).intValue();
				}
				createThumb(file, imgPath, width, height, source, name ,imgUrl);

			} finally {
				if (iis != null) {
					iis.close();
				}

			}

		} else {
			// 计算比例
			float width = 0;
			float height = 0;
			try {
				// com.lowagie.text.Image srcFile =
				// com.lowagie.text.Image.getInstance(imgPath);
				String suffix = getFileSuffix(imgPath);
				Iterator<ImageReader> iter = ImageIO
						.getImageReadersBySuffix(suffix);
				if (iter.hasNext()) {
					ImageReader reader = iter.next();
					try {
						ImageInputStream stream = new FileImageInputStream(
								new File(imgPath));
						reader.setInput(stream);
						width = reader.getWidth(reader.getMinIndex());
						height = reader.getHeight(reader.getMinIndex());
						// width = srcFile.getWidth();
						// height = srcFile.getHeight();
						float arg = 0;
						if (height >= width) {
							arg = (float) (d / height);
							height = (int) d;
							width = new Float(width * arg).intValue();
						} else {

							arg = (float) (d / width);
							width = (int) d;
							height = new Float(arg * height).intValue();
						}
						createThumb(file, imgPath, width / 3, height / 3,
								source, name ,imgUrl);
					} finally {
						reader.dispose();
					}
				}
			} catch (java.lang.OutOfMemoryError err) {
				System.out.println(fileName + "内存溢出");
				createThumb(file, imgPath, width, height, source, name ,imgUrl);
				// zipImageFile(imgPath, maxSize, staticscale + 0.5);
			}
			// catch (BadElementException e) {
			// e.printStackTrace();
			// }
		}
	}
	
	public static void main(String [] args){
		System.out.println("2");
//		try {
//			ElementUtil.thumImg("C://Users//weihuiming//Desktop//1_da3be50699be93640bcc4bf8cb98ec40//1_1.jpg");
////			ElementUtil.thumImg("E://iisserver//tomcat//webapps//iis//uploadFiles//范德萨发生//ftpFile//processed//1_da3be50699be93640bcc4bf8cb98ec40//1_1.jpg");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        File directory = new File("");// 参数为空
        String courseFile;
		try {
			courseFile = directory.getCanonicalPath();
			System.out.println(courseFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("2");
	}

}
