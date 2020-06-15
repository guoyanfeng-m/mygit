package controller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.model.ModelService;
import beans.model.ModelBean;


@Controller
@Transactional
public class ModelController {
	@Autowired
	private ModelService modelService;

	/****
	 * 根据key查询key-value
	 * @return
	 */
	@RequestMapping("model/queryAll.do")
	@ResponseBody
	public Map<String,Object>  queryConfigWEB(
			HttpServletRequest request) {
		List<ModelBean> modelList = new ArrayList<ModelBean>();
		modelList = modelService.queryAll();
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("modelList", modelList);    
		return map;
	}
	/****
	 * 修改
	 * @return
	 */
	@RequestMapping("/model/updateModel.do")
	@ResponseBody
	public Map<String,Object> updateModel(HttpServletRequest request){
		List<String> allModuleId=new ArrayList<String>();  //存放所有的MouledId
		String [] moduleIds=request.getParameterValues("moduleIds[]");  //的到要修改的ModuleId
		List<ModelBean> modelList=new ArrayList<ModelBean>();
		modelList=modelService.queryAll();  //查询数据库中所有数据
		for(int n=0;n<modelList.size();n++){
			allModuleId.add(modelList.get(n).getModule_id().toString()); //将查询的所有ModuleId放入集合中
		}
		String id=request.getParameter("id");
		if(id.equals("1")){
			for(int n=0;n<allModuleId.size();n++){
				ModelBean module=new ModelBean();
				module.setModule_id(Integer.parseInt(allModuleId.get(n)));
				module.setIs_auditing(1);
			    modelService.updateModel(module);
			}
			if(moduleIds==null){
				for(int n=0;n<allModuleId.size();n++){
					ModelBean module=new ModelBean();
					module.setModule_id(Integer.parseInt(allModuleId.get(n)));
					module.setIs_auditing(1);
				    modelService.updateModel(module);
				}
			}else{
				for(int m=0;m<moduleIds.length;m++){
					ModelBean module=new ModelBean();
					module.setModule_id(Integer.parseInt(moduleIds[m]));
					module.setIs_auditing(0);
					modelService.updateModel(module);
				}
			}
		}else{
			for(int n=0;n<allModuleId.size();n++){
				ModelBean module=new ModelBean();
				module.setModule_id(Integer.parseInt(allModuleId.get(n)));
				module.setIs_log(1);
			    modelService.updateModel(module);
			}
			if(moduleIds==null){
				for(int n=0;n<allModuleId.size();n++){
					ModelBean module=new ModelBean();
					module.setModule_id(Integer.parseInt(allModuleId.get(n)));
					module.setIs_log(1);
				    modelService.updateModel(module);
				}
			}else{
				for(int m=0;m<moduleIds.length;m++){
					ModelBean module=new ModelBean();
					module.setModule_id(Integer.parseInt(moduleIds[m]));
					module.setIs_log(0);
					modelService.updateModel(module);
				}
			}
	    }
		Map<String,Object> map = new HashMap<String,Object>();  
		map.put("success", "success"); 
		return map;
	}
	
}
    