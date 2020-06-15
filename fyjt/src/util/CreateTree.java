package util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import beans.sys.TreeBeans;

public class CreateTree {
	 public List<Integer> groupIdLists = new ArrayList<Integer>();
	 
	 // 此生成树方法为以前方法，在拖拽树结构在刷新重新加载后会出现丢失数据 ，怕更新以前项目的代码出问题，特此保留 ----开始-----
	 // 如以后确定不需要此方法时，可以删除
	public static List<TreeBeans> formatTree(List<TreeBeans> list){
		TreeBeans root = new TreeBeans();
		TreeBeans node = new TreeBeans();
	    List<TreeBeans> treelist = new ArrayList<TreeBeans>();// 拼凑好的json格式的数据
	    List<TreeBeans> parentnodes = new ArrayList<TreeBeans>();// parentnodes存放所有的父节点
	        if (list != null && list.size() > 0) {
	            root = list.get(0) ;
	            //循环遍历oracle树查询的所有节点
	            for (int i = 1; i < list.size(); i++) {
	                node = list.get(i);
	                if(node.getParentId()==root.getId()){
	                    //为tree root 增加子节点
	                    parentnodes.add(node) ;
	                    TreeBeans nodes = new TreeBeans();
	                    nodes = node;
	                    root.getChildren().add(nodes) ;
	                }else{//获取root子节点的孩子节点
	                    getChildrenNodes(parentnodes, node);
	                    parentnodes.add(node) ;
	                }
	            }    
	        }
	        treelist.add(root) ;
	
	return treelist;
	}
	
	 private static void getChildrenNodes(List<TreeBeans> parentnodes, TreeBeans node) {
		  //循环遍历所有父节点和node进行匹配，确定父子关系
	        for (int i = parentnodes.size() - 1; i >= 0; i--) {
	            
	        	TreeBeans pnode = parentnodes.get(i);
	            //如果是父子关系，为父节点增加子节点，退出for循环
	            if (pnode.getId()==node.getParentId()) {
	                pnode.setState("closed") ;//关闭二级树
	                pnode.getChildren().add(node) ;
	                return ;
	            } 
	        }
	}
	//-------生成树方法结束-----
	 
	 /**
	  * @Description 	两层循环实现建树（此方法为终端组移动）
	  * @Author 		weihuiming
	  * @Date		2017年2月20日下午5:42:50
	  * @param list		传入的数据
	  * @return
	  */
	 public static List<TreeBeans> formatTreesss(List<TreeBeans> list){
		 // 进行数据判断
		 if (list == null || list.size() < 0) {
			 return null;
		 }
		 
		 List<TreeBeans> trees = new ArrayList<>();
		 
		 // 循环数据
		 for (TreeBeans treeNode : list) {
			 // 获取根节点
			if ("0".equals(treeNode.getParentId()+"")) {
				trees.add(treeNode);
			}
		 
			// 循环节点进行对比
			for (TreeBeans it : list) {
				// 查找父节点
				if (it.getParentId() == treeNode.getId()) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<TreeBeans>());
					}
					// 关闭二级节点下的所有节点
					if (it.getParentId() != 1){
						treeNode.setState("closed") ;//关闭二级树
					}
					treeNode.getChildren().add(it);
				}
			}
		 }
		 return trees;
	 }
	 public JSONArray treeMenuList(JSONArray menuList, int parentId) {  
	       JSONArray childMenu = new JSONArray();  
	       for (Object object : menuList) {  
	           JSONObject jsonMenu = JSONObject.fromObject(object);  
	           int menuId = jsonMenu.getInt("t_id");  
	           int pid = jsonMenu.getInt("parentId");  
	           if (parentId == pid) {  
	               JSONArray c_node = treeMenuList(menuList, menuId);  
	               jsonMenu.put("childNode", c_node);  
	               childMenu.add(jsonMenu);  
	           }
	       }  
	       return childMenu;  
	   } 
	 
	 @SuppressWarnings("unused")
	public List<Integer> getGroup(JSONArray menuList, int parentId){
		 JSONArray childMenu = new JSONArray();  
	       for (Object object : menuList) {  
	           JSONObject jsonMenu = JSONObject.fromObject(object);  
	           int menuId = jsonMenu.getInt("t_id");  
	           int pid = jsonMenu.getInt("parentId");  
	           if (parentId == menuId) {  
	               JSONArray c_node = treeMenuList(menuList, menuId);  
	               jsonMenu.put("childNode", c_node);  
	               childMenu.add(jsonMenu);  
	           }
	       }  
	       getIdList(childMenu);
	       
	       
	       return groupIdLists;  
	 }
	 
	 public void getIdList(JSONArray menuList){
		 JSONArray childMenu = new JSONArray();  
	       for (Object object : menuList) {  
	           JSONObject jsonMenu = JSONObject.fromObject(object);  
	           int menuId = jsonMenu.getInt("t_id");  
	           childMenu = jsonMenu.getJSONArray("childNode");
	           groupIdLists.add(menuId);
	           getIdList(childMenu);     
	       }  
		 
	 }
}
