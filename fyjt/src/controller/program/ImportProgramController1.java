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
@SuppressWarnings({"unused"})
@Controller
@Transactional
public class ImportProgramController1 {
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
	@RequestMapping(value = "czc/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadFile(
			@RequestParam("pic") CommonsMultipartFile pic,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		String md5 = "";
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
//		if (!("zip".equals(fileType))) {
//			succ = "传入格式不正确！请查看是否是zip格式";
//			map.put("msg", succ);
//			return map;
//		}
		// 为了避免文件名重复，在文件名前加UUID
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String uuidFileName = uuid + fileName;
		// 将文件保存到服务器
		String fis = FileUtil.upFile(pic.getInputStream(), uuidFileName,
				filePath);

		//文件解压后素材路径
		String fileProNames = fis;
		// 改变名称
		
		// 获取文件名(没有后缀名)
		int lastslash = fileProNames.lastIndexOf("\\");
		String splitName = fileProNames.substring(lastslash+1,lastslash+33);
		md5 = splitName + "." + fileName.split("\\.")[1];
		// 创建新名称符合监听格式
		splitName = splitName + "&_^"+ fileName + "&_@" + id + "&_@";
		// 改变文件名称
		File oldfile = new File(fileProNames);
		oldfile.renameTo(new File(splitName));
		// 移动到ftp文件夹
		FileUtils.copyFileToDirectory(new File(splitName), ftpFileUrl);
		// 删除文件
		FileUtil.delFolder(filePath);
		
		map.put("msg",succ);
		map.put("md5", md5);
		return map;
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
