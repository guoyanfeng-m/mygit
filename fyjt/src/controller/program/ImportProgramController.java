package controller.program;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import service.config.ConfigService;
import service.element.ElementService;
import service.program.ProgramService;
import util.ElementType;
import util.ElementUtil;
import util.FileUtil;
import util.XmlDom4j;
import util.XmlUtil;
import util.ZipUtil;
import beans.element.ElementBean;
import beans.program.ProgramBean;
import beans.sys.SystemConstant;
import beans.user.UserBean;

/**
 * @Description ImportProgramController 导入节目
 * @Author weihuiming
 * @Date 2016年12月9日 上午11:08:54
 */
@SuppressWarnings({"unchecked"})
@Controller
@Transactional
public class ImportProgramController {
	@Autowired
	private ProgramService programService;
	@Autowired
	private ElementService elementService;
	@Autowired
	private ConfigService configService;
	
	private static final Logger logger = LoggerFactory.getLogger(ImportProgramController.class);

	/**
	 * 节目导入数据传入接口 
	 * 
	 * @param pic
	 *            上传的数据
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "program/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(
			@RequestParam("pic") CommonsMultipartFile pic,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, Exception {
		// 返回参数说明
		String succ = "";
		// 获取上传者id
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String id = user.getUser_id().toString();
		System.out.println(id);
		// 获取本地路径
		String imgUrl = request.getSession().getServletContext()
				.getRealPath("");//iisserver\tomcat\webapps\iis1
		// 获取ftpFile路径、
		File ftpFileUrl = new File(new File(imgUrl).getParentFile().getParentFile().getParent() + File.separator + "ftpFile");
		// 设置文件保存的本地路径
		String filePath = request.getSession().getServletContext().getRealPath(
				"\\uploadFiles\\");
		// 文件名
		String fileName = pic.getOriginalFilename();
		// 文件去掉后缀的名称
		String fileNames = fileName.split("[.]")[0];
		String fileType = fileName.split("[.]")[1];
		//后端验证文件格式
		if (!("zip".equals(fileType))) {
			succ = "传入格式不正确！请查看是否是zip格式";
			return succ;
		}
		// 为了避免文件名重复，在文件名前加UUID
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String uuidFileName = uuid + fileName;
		// 将文件保存到服务器
		String fis = FileUtil.upFile(pic.getInputStream(), uuidFileName,
				filePath);

		// 解压文件路径
		String zipFile = filePath + "\\" + fileNames;
		
		// 通过上传的文件名称查询是否存储该节目
		List<ProgramBean> proList = programService
				.queryProgramByName(fileNames);

		// 返回参数不如不等于1
		if (!("1".equals(fis))) {
			// 解压文件
			ZipUtil zip = new ZipUtil();
			zip.unZipFile(fis, zipFile);

			File zipProName = new File(zipFile);
			
			// 获取文件解压文件名称
			File[] array = zipProName.listFiles();
			
			// 读取导出节目素材
			String elmentFile = array[0]
					+ "\\ftpFile\\processed\\";
			// 读取xml文件
			String elmentXmlFile = array[0]
					+ "\\ftpFile\\programXmlFile\\";
			// 获取素材
			logger.info("elmentFile:"+elmentFile);
			List<String> fileNameList = FileUtil.getFile(elmentFile);
			// 读取xml路径
			List<String> fileXmlNameList = FileUtil.getFile(elmentXmlFile);

			// 读取xml参数
			String proXml = array[0]
					+ "\\ftpFile\\programXmlFile\\" + fileXmlNameList.get(0);

			XmlDom4j xmlDom4j = new XmlDom4j();
			Element root = xmlDom4j.DomRoot(proXml);
			xmlDom4j.getNodes(root);// 从根节点开始遍历所有节点
			Map<String, Object> impMap = xmlDom4j.getImpMap();

			// 获取xml文件工具
			XmlUtil xmlUtil = new XmlUtil();

			//文件解压后素材路径
			String fileProNames = "";
			
			for (String fileNamelist : fileNameList) {
				// 节目单数据
				for (String key : impMap.keySet()) {
					if (key.contains(fileNamelist)) {
						fileProNames = elmentFile + fileNamelist;
						File fileProName = new File(fileProNames);
						// 判断文件是否存在
						if (!fileProName.exists()) {
							succ = "传入素材缺失！";
							return succ;
						}
						
						// 判断是否是文件夹 office素材是文件夹
						if (fileProName.isDirectory()) {
							// 获取文件MD5值和前面编号
							String[] pdfName = fileNamelist.split("_");
							// 判断数据库是否有相同数据
							int cout = elementService.queryCountElementByMd5(pdfName[1]);
							if (cout > 0) {
								// 不进行操作
							} else {
								// 进行数据操作
								// 复制office文件夹素材下第一个图片到上级目录
								FileUtils.copyFileToDirectory(new File(
										fileProName + File.separator
												+ pdfName[0] + "_1.jpg"),
										new File(elmentFile));
								// 重名图片为md5名
								File md5Pdf = new File(elmentFile
										+ File.separator + pdfName[0]
										+ "_1.jpg");
								md5Pdf
										.renameTo(new File(elmentFile
												+ File.separator + pdfName[1]
												+ ".jpg"));
								// 获取缩略图
								ElementUtil.thumImg(elmentFile + pdfName[1]
										+ ".jpg", imgUrl);
								
								//office缩略图素材存储路径
								String processedUrl = ftpFileUrl + File.separator + "processed" + File.separator;
								// 移动缩略图
								// 缩略图格式为img+MD5值+.jpg，large_img+MD5值+.jpg，mini_img+MD5值+.jpg
								FileUtils.copyFileToDirectory(new File(elmentFile + File.separator
										+ "img" + pdfName[1] + ".jpg"),  new File(processedUrl));
								FileUtils.copyFileToDirectory(new File(elmentFile + File.separator
										+ "large_img" + pdfName[1] + ".jpg"),  new File(processedUrl));
								FileUtils.copyFileToDirectory(new File(elmentFile + File.separator
										+ "mini_img" + pdfName[1] + ".jpg"),  new File(processedUrl));

								// 数据库存储信息
								String ThumbnailUrl = "ftpFile\\processed\\img"
										+ pdfName[1] + ".jpg";
								// 移动office 文件夹
								FileUtils.copyDirectory(fileProName,  new File(processedUrl));
								// 文件入库
								ElementBean elementBean = new ElementBean();
								elementBean.setAudit_status(1);
								elementBean
										.setCreate_time(new java.sql.Timestamp(
												System.currentTimeMillis()));
								elementBean.setCreator_id(Integer.parseInt(id));
								elementBean
										.setDeleted(SystemConstant.ISDELETED_FALSE);
								elementBean.setElem_name(impMap.get(key)
										.toString());
								elementBean.setElem_path(key);
								elementBean.setMD5(pdfName[1]);
								elementBean.setThumbnailUrl(ThumbnailUrl);
								elementBean.setType(7);
								elementBean
										.setUpdate_time(new java.sql.Timestamp(
												System.currentTimeMillis()));
								// 素材存入数据库
								elementService.addElement(elementBean);
								// 存入中间表
								elementService.insertClassifyType(elementBean
										.getElem_id(), 7);
							}
							//判断是否是文本素材
						} else if (fileProNames.contains(".xml")) {
							// 解析文本
							String xmlReso = key.split("_")[1];// 文本的大小
							// 解析文本xml
							Map<String, Object> mapXml = xmlUtil.xmlToMap(fileProNames);
							Map<String, String> mapText = (Map<String, String>) mapXml.get("text");
							String Textname = fileNamelist.split("\\.")[0];
							// ftp上文本存储路径
							String processedUrl = ftpFileUrl + File.separator + "processed" + File.separator;
							// 移动文件
							FileUtils.copyFileToDirectory(new File(fileProNames), new File(processedUrl));
							// 数据库存储
							ElementBean elementbean = new ElementBean();
							elementbean.setMD5(Double.toString(new Double(Math
									.random() * 10000).longValue()
									+ System.currentTimeMillis()));
							elementbean.setCreate_time(new java.sql.Timestamp(
									System.currentTimeMillis()));
							elementbean.setElem_center(mapText.get("center"));
							elementbean.setResolution(xmlReso);
							elementbean.setElem_name(Textname);
							elementbean.setElem_path("ftpFile\\processed\\"
									+ fileNamelist);
							elementbean.setType(ElementType.text.getValue());
							elementbean
									.setDeleted(SystemConstant.ISDELETED_FALSE);
							elementbean
									.setThumbnailUrl("ftpFile\\processed\\text.jpg");
							elementbean.setAudit_status(1);
							elementbean.setCreator_id(Integer.parseInt(id));
							elementbean.setUpdate_time(new java.sql.Timestamp(System.currentTimeMillis()));
							elementService.insertText(elementbean);
							elementService.insertClassifyType(elementbean.getElem_id(), 1);
						} else {
							// 改变名称
							int splitIndex = fileNamelist.lastIndexOf(".");
							// 获取文件名(没有后缀名)
							String splitName = fileNamelist.substring(0,
									splitIndex);
							// 创建新名称符合监听格式
							splitName = elmentFile + splitName + "&_^"+ impMap.get(key) + "&_@" + id + "&_@";
							// 改变文件名称
							fileProName.renameTo(new File(splitName));
							// 移动到ftp文件夹
							FileUtils.copyFileToDirectory(new File(splitName), ftpFileUrl);
						}
					}
				}
			}
			// 保存节目单
			HashMap<String, String> proMap = (HashMap<String, String>) impMap.get("program");
			ProgramBean programBean = new ProgramBean();
			// 节目类型 0：普通节目 1：互动节目
			programBean.setType(proMap.get("istouch").equals("true") ? 1 : 0);
			programBean.setSchedulelevel(Integer.parseInt(proMap.get("pubpower").toString()));
			programBean.setStartTime(string2Timestamp(proMap.get("startdate")));
			programBean.setEndTime(string2Timestamp(proMap.get("stopdate").toString()));
			programBean.setCreate_time(new Timestamp(System.currentTimeMillis()));
			programBean.setDeleted(SystemConstant.ISDELETED_FALSE);
			programBean.setIsSend(SystemConstant.AUDIT_TRUE);
			programBean.setAudit_status(1);
			programBean.setCreator_id(Integer.parseInt(id));

			// 存储新的xml文件路径
			String xmlUrl = array[0]
					+ "\\ftpFile\\programXmlFile\\";
			// 保存节目获取id并修改保存上传xml
			// 判断节目名称是否重复
			if (proList.size() > 0) {
				fileNames += uuid.substring(0, 4);
			}
			programBean.setUrl("programXmlFile//" + fileNames + ".xml");
			programBean.setName(fileNames);
			programService.insertImpProgram(programBean, proXml, xmlUrl);
			
			// 获取节目xml存储路径
			String programXmlFile = ftpFileUrl + File.separator + "programxmlFile" + File.separator;
			// 移动节目xml
			FileUtils.copyFileToDirectory(new File(xmlUrl + fileNames + ".xml"), new File(programXmlFile));
		} else {
			succ = "文件上传失败！！！";
			return succ;
		}
		// 删除文件
		FileUtil.delFolder(filePath);
		if (proList.size() > 0) {
			succ = "导入节目成功！因节目单名称重复,已经自动修改节目单名称为：" + fileNames;
		} else {
			succ = "导入" + fileNames + "节目成功！";
		}
		return succ;
	}

	/**
	 * 字符串转换时间类型
	 * 
	 * @param timestr
	 * @return
	 */
	private Timestamp string2Timestamp(String timestr) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(timestr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}
	
}
