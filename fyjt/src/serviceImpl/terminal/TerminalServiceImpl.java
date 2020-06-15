package serviceImpl.terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.collections.list.SetUniqueList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.terminal.TerminalService;
import util.CreateTree;
import util.PageInfo;
import beans.program.view.ProgramTerminalView;
import beans.role.ParametersRoleTerminalBean;
import beans.roletree.ResultGroupBean;
import beans.sys.TreeBeans;
import beans.terminal.TerminalBean;
import beans.terminal.TerminalGroupBean;
import beans.terminal.TerminalTerminalGroupBean;
import beans.terminal.TerminalViewBean;
import dao.program.ProgramTerminalDao;
import dao.role.RoleDao;
import dao.terminal.TerminalDao;
import dao.terminal.TerminalStatusDao;
import dao.terminal.TerminalTerminalGroupDao;
import dao.user.UserDao;

@SuppressWarnings({"rawtypes","unchecked","static-access"})
@Service
@Transactional(readOnly=true)
public class TerminalServiceImpl implements TerminalService{
	@Autowired
	private TerminalDao terminalDao;
	@Autowired
	private TerminalTerminalGroupDao terminalTerminalGroupDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ProgramTerminalDao programTerminalDao;
	@Autowired
	private TerminalStatusDao terminalStatusDao;

    /*
     * 终端管理--添加终端
     * (non-Javadoc)
     * @see service.terminal.TerminalService#insertTerminal(beans.terminal.TerminalBean, java.util.List, java.util.List)
     */
	@Override
	@Transactional(readOnly=false)
	public void insertTerminal(TerminalBean terminalBean,List<Integer> userlist,List<Integer> terminalgrouplist) {
		List<Integer> rolelist = new ArrayList<Integer>();
		if (null!=userlist&&userlist.size()>0) {
			rolelist.addAll(userDao.queryRoleIDByUserIDs(userlist));
		}
		HashSet<Integer> roleListSet = new HashSet<Integer>(rolelist);
		terminalDao.insertTerminal(terminalBean);
		Integer terminalId = terminalBean.getTerminal_id();
		List<TerminalTerminalGroupBean> terminalTerminalGroupList = new ArrayList<TerminalTerminalGroupBean>();
		for (int i = 0; i < terminalgrouplist.size(); i++) {
			TerminalTerminalGroupBean TerminalTerminalGroupBean=new TerminalTerminalGroupBean();
			TerminalTerminalGroupBean.setTerminalId(terminalId);
			TerminalTerminalGroupBean.setGroupId(terminalgrouplist.get(i));
			terminalTerminalGroupList.add(TerminalTerminalGroupBean);
		}
		//单终端多终端组入库
		terminalTerminalGroupDao.insertTerminalTerminalGroups(terminalTerminalGroupList);
		//入库角色终端表数据提取
		List<ParametersRoleTerminalBean>roleTerminalList=SetUniqueList.decorate(new ArrayList<ParametersRoleTerminalBean>());
		//入库角色终端组表数据提取
//		List<ParametersRoleTerminalGroupBean>roleTerminalGroupList=SetUniqueList.decorate(new ArrayList<ParametersRoleTerminalGroupBean>());
		for(Integer roleId : roleListSet){
			ParametersRoleTerminalBean parametersRoleTerminalBean = new ParametersRoleTerminalBean();
			parametersRoleTerminalBean.setRole_id(roleId);
			parametersRoleTerminalBean.setTerminal_id(terminalId);
			roleTerminalList.add(parametersRoleTerminalBean);
//			for(Integer terminalgroupId:terminalgrouplist){
//				ParametersRoleTerminalGroupBean roleTerminalGroupBean = new ParametersRoleTerminalGroupBean();
//				roleTerminalGroupBean.setRole_id(role_id);
//				roleTerminalGroupBean.setTerminalgroup_id(terminalgroupId);
//				roleTerminalGroupList.add(roleTerminalGroupBean);
//			}
		}
		//删除需要添加的角色终端表数据
		roleDao.deleteTerminalByTerminalIds(terminalBean);
		//插入角色终端表
		roleDao.insertRoleTerminals(roleTerminalList);
//		//删除角色终端组表
//		roleDao.deleteRoleTerminalGroups(roleTerminalGroupList);
//		//插入角色终端组表
//		roleDao.insertRoleTerminalGroups(roleTerminalGroupList);
	}

	@Override
	public List<TerminalViewBean> queryTerminalByGroupId(Integer terminalGroupId,PageInfo pageInfo,List<String> userlist) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupIds = new CreateTree().getGroup(tree, terminalGroupId);
		List<TerminalViewBean> terminalViewBeanList = new ArrayList<TerminalViewBean>();
		Integer[] groupId = new Integer[groupIds.size()];
		for(int i=0;i<groupIds.size();i++){
			groupId[i] = groupIds.get(i);
		}
		terminalViewBeanList=terminalDao.queryTerminalByGroupId(rolelist,groupId,pageInfo.getStart(), pageInfo.getEnd());
		return terminalViewBeanList;
	}

	
	@Override
	public Integer queryTerminalTotleByGroupId(
			Integer terminalGroupId,List<String> userlist) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupIds = new CreateTree().getGroup(tree, terminalGroupId);
		Integer[] groupId = new Integer[groupIds.size()];
		for(int i=0;i<groupIds.size();i++){
			groupId[i] = groupIds.get(i);
		}
		return terminalDao.queryTerminalTotleByGroupId(rolelist,groupId);
	}
	
	
	
	@Override
	public List<TerminalViewBean> queryTerminal(TerminalBean terminalBean,PageInfo pageInfo,List<String> userlist) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("terminalBean", terminalBean);
		map.put("list", rolelist);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		return terminalDao.queryTerminal(map);
	}

	@Override
	public List<TreeBeans> queryTree(TerminalGroupBean terminalGroupBean) {
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		 List<TreeBeans> list = new ArrayList<TreeBeans>(); 
		for(int i=0;i<terminalGroupBeanList.size();i++){
			TreeBeans treeBeans = new TreeBeans();
			treeBeans.setId(terminalGroupBeanList.get(i).getT_id());
			treeBeans.setParentId(terminalGroupBeanList.get(i).getParentId());
			treeBeans.setText(terminalGroupBeanList.get(i).getGroupName());
			list.add(treeBeans);
		}
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
//		treeBeansList = new CreateTree().formatTrees(list);(终端组移动)
		treeBeansList = new CreateTree().formatTree(list);
		return treeBeansList;
	}
	
	/**
	 * @Description 查询该角色下的所有终端组(终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年2月27日上午11:22:53
	 * @param userId
	 * @return
	 
	public List<TerminalGroupBean> selectTerminalTreeByUserIds(Integer userId){
		return terminalDao.selectTerminalTreeByUserId(userId);
	}
	*/
	/**
	 * @Description 通过用户id查询角色下的终端组(终端组移动)
	 * @Author 		weihuiming
	 * @Date		2017年2月22日下午3:51:46
	 * @param userId	用户id
	 * @return
	 */
	/*
	public List<TreeBeans> selectTerminalTreeByUserId(Integer userId){
		// 获取该用户下的所有终端组
		List<TerminalGroupBean> terminalGroupBeanList = selectTerminalTreeByUserIds(userId);
		// 存储数据生成TreeBeans
		List<TreeBeans> list = new ArrayList<>();
		TreeBeans treeBeans = null;
		for (TerminalGroupBean terminalGroup : terminalGroupBeanList) {
			treeBeans = new TreeBeans();
			treeBeans.setId(terminalGroup.getT_id());
			treeBeans.setParentId(terminalGroup.getParentId());
			treeBeans.setText(terminalGroup.getGroupName());
			list.add(treeBeans);
		}
		// 生成树
		List<TreeBeans> treeBeansList = new CreateTree().formatTrees(list);
		return treeBeansList;
	}
	*/
	public List<TerminalGroupBean> queryTerminalGroup(TerminalGroupBean terminalGroupBean) {
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		return terminalGroupBeanList;
	}


	@Override
	@Transactional(readOnly=false)
	public void deleteTerminalByTerminalId(TerminalBean terminalBean) {
		terminalTerminalGroupDao.deleteTerminalTerminalGroupByIds(terminalBean);
		//如果所有的终端组都被删除执行以下删除操作
		List<Integer>terminalIdList=SetUniqueList.decorate(new ArrayList<Integer>());
		terminalIdList=terminalTerminalGroupDao.selectTerminalTerminalGroupTerminalIds(terminalBean);
		List<Integer>Tlist=SetUniqueList.decorate(new ArrayList<Integer>());
		Tlist=terminalBean.getTerminalIdList();
		if (terminalIdList.size()>0) {
			for (Integer terminalId : terminalIdList) {
				Tlist.remove(terminalId);
			}
		}
		if (Tlist.size()>0) {
			//删除终端表记录
			terminalDao.deleteTerminalByTerminalId(terminalBean);
			//删除之前已经判断节目终端表中没有播放记录故跳过删除节目发送终端记录表数据
			//删除角色终端表
			roleDao.deleteTerminalByTerminalIds(terminalBean);
			//删除终端状态数据
			terminalStatusDao.deleteTerminalStatusByTerminalId(terminalBean);
			terminalStatusDao.deletecloseTimeByTerminalIds(terminalBean);
		}
	}
    /*
     * 终端编辑更新
     * (non-Javadoc)
     * @see service.terminal.TerminalService#updateTerminalByTerminalId(beans.terminal.TerminalBean, beans.terminal.TerminalTerminalGroupBean)
     */
	
	@Override
	@Transactional(readOnly=false)
	public void updateTerminalByTerminalId(TerminalBean terminalBean) {
		terminalDao.updateTerminalByTerminalId(terminalBean);
		Integer terminalId = terminalBean.getTerminal_id();
		List<Integer>terminalgrouplist=SetUniqueList.decorate(new ArrayList<Integer>());
		terminalgrouplist=terminalBean.getGroupIdList();
		List<TerminalTerminalGroupBean> terminalTerminalGroupList = new ArrayList<TerminalTerminalGroupBean>();
		for (int i = 0; i < terminalgrouplist.size(); i++) {
			TerminalTerminalGroupBean TerminalTerminalGroupBean=new TerminalTerminalGroupBean();
			TerminalTerminalGroupBean.setTerminalId(terminalId);
			TerminalTerminalGroupBean.setGroupId(terminalgrouplist.get(i));
			terminalTerminalGroupList.add(TerminalTerminalGroupBean);
		}
		//单终端多终端组清除入库数据
		//将终端组集合置空
		terminalBean.setGroupIdList(null);
		terminalTerminalGroupDao.deleteTerminalTerminalGroupByIds(terminalBean);
		//单终端多终端组入库
		terminalTerminalGroupDao.insertTerminalTerminalGroups(terminalTerminalGroupList);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void updateTerminalAuditByTerminalId(TerminalBean terminalBean) {
		terminalDao.updateTerminalByTerminalId(terminalBean);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void updateBdTerminalByTerminalId(TerminalBean terminalBean,List<Integer> userlist) {
		List<Integer> rolelist = new ArrayList<Integer>();
		for(Integer user_id : userlist){
			if(user_id!=null&&user_id!=0){
				rolelist.add(userDao.queryRoleIDByUserID(user_id));
			}
		}
		HashSet<Integer> roleSet = new HashSet<Integer>(rolelist);
		terminalDao.updateTerminalByTerminalId(terminalBean);
		Integer terminalId = terminalBean.getTerminal_id();
		List<Integer>terminalIds=new ArrayList<Integer>();
		terminalIds.add(terminalId);
		terminalBean.setTerminalIdList(terminalIds);
		List<Integer>groupids=SetUniqueList.decorate(new ArrayList<Integer>());
		List<TerminalTerminalGroupBean> terminalTerminalGroupList = new ArrayList<TerminalTerminalGroupBean>();
		groupids.addAll(terminalBean.getGroupIdList());
		for (int i = 0; i < groupids.size(); i++) {
			TerminalTerminalGroupBean TerminalTerminalGroupBean=new TerminalTerminalGroupBean();
			TerminalTerminalGroupBean.setTerminalId(terminalId);
			TerminalTerminalGroupBean.setGroupId(groupids.get(i));
			terminalTerminalGroupList.add(TerminalTerminalGroupBean);
		}
		terminalTerminalGroupDao.deleteTerminalTerminalGroupByIds(terminalBean);
		terminalTerminalGroupDao.insertTerminalTerminalGroups(terminalTerminalGroupList);
		List<ParametersRoleTerminalBean>roleTerminalList=SetUniqueList.decorate(new ArrayList<ParametersRoleTerminalBean>());
		for(Integer roleId : roleSet){
			ParametersRoleTerminalBean parametersRoleTerminalBean = new ParametersRoleTerminalBean();
			parametersRoleTerminalBean.setRole_id(roleId);
			parametersRoleTerminalBean.setTerminal_id(terminalId);
			roleTerminalList.add(parametersRoleTerminalBean);
		}
		roleDao.deleteTerminalByTerminalIds(terminalBean);
		roleDao.insertRoleTerminals(roleTerminalList);
	}

	@Override
	public Integer queryTerminalCount(TerminalBean terminalBean,List<String> userlist) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("terminalBean", terminalBean);
		map.put("list", rolelist);
		return terminalDao.queryTerminalCount(map);
	}

	

	@Override
	public List<TerminalViewBean> queryTerminalWithoutPage(
			TerminalBean terminalBean) {
		return terminalDao.queryTerminalWithoutPage(terminalBean);
	}


	@Override
	public List<TerminalViewBean> queryTerminalByTerminalIds(List<Integer> terminalId) {
		return terminalDao.queryTerminalByTerminalIds(terminalId);
	}

	
	@Override
	public List<TreeBeans> queryTerminalTrees(
			Integer userId) {
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		List<TerminalViewBean> terminalViewBeanList = new ArrayList<TerminalViewBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		terminalViewBeanList = terminalDao.queryTerminalTree(userId);
		List<TreeBeans> list = new ArrayList<TreeBeans>(); 
		for(int i=0;i<terminalGroupBeanList.size();i++){
			TreeBeans treeBeans = new TreeBeans();
			treeBeans.setId(terminalGroupBeanList.get(i).getT_id());
			treeBeans.setParentId(terminalGroupBeanList.get(i).getParentId());
			treeBeans.setText(terminalGroupBeanList.get(i).getGroupName());
			list.add(treeBeans);
		}
		for(TerminalViewBean terminalViewBean:terminalViewBeanList){
			TreeBeans treeBeans = new TreeBeans();
			treeBeans.setId(terminalViewBean.getTerminal_id());
			treeBeans.setParentId(terminalViewBean.getGroupId());
			treeBeans.setText(terminalViewBean.getTerminal_name());
			list.add(treeBeans);
		}
		List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
		treeBeansList = new CreateTree().formatTree(list);
		return treeBeansList;
	}

	@Override
	public List<TerminalViewBean> queryBdTerminal(PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		return terminalDao.queryBdTerminal(map);
	}

	@Override
	public Integer queryBdTerminalCount() {
		return terminalDao.queryBdTerminalCount();
	}

	@Override
	public List<String> queryTerminalMac(TerminalBean terminalBean) {
		return terminalDao.queryTerminalMac(terminalBean);
	}

	@Override
	public List<String> queryTerminalName(TerminalBean terminalBean) {
		return terminalDao.queryTerminalName(terminalBean);
	}

	@Override
	public TerminalViewBean queryTerminalWithoutPageById(
			TerminalBean terminalBean) {
		return terminalDao.queryTerminalWithoutPageById(terminalBean);
	}

	@Override
	public List<TerminalViewBean> queryTerminalNameByGroupId(
			Integer terminalGroupId, PageInfo pageInfo, List<String> userlist,
			String terminalName) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupIds = new CreateTree().getGroup(tree, terminalGroupId);
		List<TerminalViewBean> terminalViewBeanList = new ArrayList<TerminalViewBean>();
		Integer[] groupId = new Integer[groupIds.size()];
		for(int i=0;i<groupIds.size();i++){
			groupId[i] = groupIds.get(i);
		}
		terminalViewBeanList=terminalDao.queryTerminalNameByGroupId(rolelist,groupId,terminalName,pageInfo.getStart(), pageInfo.getEnd());
		return terminalViewBeanList;
	}

	@Override
	public Integer queryTerminalNameTotleByGroupId(Integer terminalGroupId,
			List<String> userlist, String terminalName) {
		List<String> rolelist = new ArrayList<String>();
		for(String user_id : userlist){
			rolelist.add(userDao.queryRoleIDByUserID(Integer.parseInt(user_id))+"");
		}
		TerminalGroupBean terminalGroupBean = new TerminalGroupBean();
		terminalGroupBean.setIsDeleted(0);
		List<TerminalGroupBean> terminalGroupBeanList = new ArrayList<TerminalGroupBean>();
		terminalGroupBeanList = terminalDao.queryTree(terminalGroupBean);
		JSONArray tree = new JSONArray();  
		tree.addAll(terminalGroupBeanList);
		List<Integer> groupIds = new CreateTree().getGroup(tree, terminalGroupId);
		Integer[] groupId = new Integer[groupIds.size()];
		for(int i=0;i<groupIds.size();i++){
			groupId[i] = groupIds.get(i);
		}
		return terminalDao.queryTerminalNameTotleByGroupId(rolelist,groupId,terminalName);
	}

	@Override
	public List<String> queryTerminalMacIp(TerminalBean terminalBean) {
		return terminalDao.queryTerminalMacIp(terminalBean);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteTerminalByIpMac(TerminalBean terminalBean) {
		terminalDao.deleteTerminalByIpMac(terminalBean);
		
	}
	
	@Override
	public List<String> queryTerminalMacById(List terminalidlist){
		return terminalDao.queryTerminalMacById(terminalidlist);
	}
	
	@Override
	public List<ProgramTerminalView> queryTerminalPublishStatus(String[] macs){
		return terminalDao.queryTerminalPublishStatus(macs);
	}
	
	@Override
	public Integer getTerminalStatus(List<String> mac){
		return terminalDao.getTerminalStatus(mac);
	}
    /*
     *查找终端组
     */
	@Override
	public List<TreeBeans> queryTerminalGroups(Integer  userId) {
		List<ResultGroupBean> terminalGroupBeanList = new ArrayList<ResultGroupBean>();
		terminalGroupBeanList=terminalTerminalGroupDao.queryRoleTerminalgroupByUserID(userId);
		 List<TreeBeans> list = new ArrayList<TreeBeans>(); 
			for(int i=0;i<terminalGroupBeanList.size();i++){
				TreeBeans treeBeans = new TreeBeans();
				treeBeans.setId(terminalGroupBeanList.get(i).getT_id());
//				treeBeans.setState("closed");
				treeBeans.setParentId(Integer.parseInt(terminalGroupBeanList.get(i).getParentID()));
				treeBeans.setText(terminalGroupBeanList.get(i).getGroupName());
				list.add(treeBeans);
			}
//			List<TreeBeans> treeBeansList = new ArrayList<TreeBeans>();
//			treeBeansList =CreateTree.formatTree(list);
		return list;
	}
}
