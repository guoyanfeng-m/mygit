package serviceImpl.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.element.ElementService;
import util.PageInfo;
import beans.config.ConfigBean;
import beans.element.ElementBean;
import beans.element.ElemtypeBean;
import dao.element.ElementDao;

@Service
@Transactional(readOnly=true)
public class ElementServiceImpl implements ElementService {
	@Autowired
	private ElementDao elementDao;
	

	@Override
	@Transactional(readOnly=false)
	public void addElement(ElementBean elementBean) {
		elementDao.addElement(elementBean);
	}

	@Override
	public List<ElementBean> queryElement(ElementBean elementBean, PageInfo pageInfo, Integer creatorid, List<Integer> userids,List<Integer> typeidList){
		Map<String, Object> map = new HashMap<String, Object>();  
		map.put("elementBean", elementBean);
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		map.put("creatorid", creatorid);
		map.put("userids", userids);
		map.put("typeidList", typeidList);
		return elementDao.queryElement(map);
	}


	@Override
	@Transactional(readOnly=false)
	public void deleteElementById(ElementBean elementBean) {
		elementDao.deleteElementById(elementBean);
	}


	@Override
	public String getElementNameById(Integer id) {
		return elementDao.getElementNameById(id);
	}
	
	@Override
	public String getClassifyNameById(Integer id){
		return elementDao.getClassifyNameById(id);
	}

	@Override
	public String findLocation(Integer id) {
		return elementDao.findLocation(id);
	}


	@Override
	@Transactional(readOnly=false)
	public void updateElementById(ElementBean elementBean) {
		elementDao.updateElementById(elementBean);
	}


	@Override
	public void auditElementById(ElementBean elementBean) {
		elementDao.auditElementById(elementBean);		
	}


	@Override
	public Integer queryTerminalCount(ElementBean elementBean, Integer creatorid, List<Integer> userids,List<Integer> typeidList) {
		return elementDao.queryTerminalCount(elementBean,creatorid,userids,typeidList);
	}


	@Override
	public List<ConfigBean> queryFtpHttpMessage(ConfigBean configbean) {
		return elementDao.queryFtpHttpMessage(configbean);
	}


	@Override
	@Transactional(readOnly=false)
	public void saveHtml(ElementBean elementbean) {
		elementDao.saveHtml(elementbean);
	}


	@Override
	public String findFtpLocation() {
		return elementDao.findFtpLocation();
	}


	@Override
	public Integer findElementbeanid(ElementBean elementbean) {
		return elementDao.findElementbeanid(elementbean);
	}


	@Override
	@Transactional(readOnly=false)
	public void insertText(ElementBean elementbean) {
		elementDao.insertText(elementbean);
	}


	@Override
	public String findWebLocation(Integer id) {
		return elementDao.findWebLocation(id);
	}


	@Override
	public List<ElementBean> sameNameTest(String eleName) {
		return elementDao.sameNameTest(eleName);
	}


	@Override
	public String queryCreatorById(String userId) {
		return elementDao.queryCreatorById(userId);
	}


	@Override
	@Transactional(readOnly=false)
	public void insertClassify(String typeName, int parseInt,Integer creatorid, int isprivate) {
		elementDao.insertClassify(typeName,parseInt,creatorid,isprivate);
	}


	@Override
	@Transactional(readOnly=false)
	public void deleteClassifyElement(int parseInt) {
		elementDao.deleteClassifyElement(parseInt);
	}


	@Override
	public List<ElemtypeBean> queryClassify(ElemtypeBean elemtypeBean) {
		return elementDao.queryClassify(elemtypeBean);
	}


	@Override
	public List<ElemtypeBean> queryClassifyName(ElemtypeBean elemtypeBean) {
		List<ElemtypeBean> typeBeanList = new ArrayList<ElemtypeBean>();
		typeBeanList = elementDao.queryClassifyName(elemtypeBean);
		return typeBeanList;
	}


	@Override
	@Transactional(readOnly=false)
	public void addToClassify(int elemId, int typeId) {
		elementDao.addToClassify(elemId,typeId);
	}


	@Override
	public Integer queryClassifyTotalCount(ElementBean elementBean) {
		return elementDao.queryClassifyTotalCount(elementBean);
	}


	@Override
	public Integer querySameToClassify(int elemId, int typeId) {
		return elementDao.querySameToClassify(elemId,typeId);
	}


	@Override
	@Transactional(readOnly=false)
	public void deleteClassifyById(int typeid) {
		elementDao.deleteClassifyById(typeid);
	}


	@Override
	@Transactional(readOnly=false)
	public String[] deleteTypeById(int typeid) {
		String[] elems = elementDao.queryElementIdByClassify(typeid);
		elementDao.deleteTypeById(typeid);
		return elems;
	}


	@Override
	public List<ElemtypeBean> classifySameNameTest(String typeName) {
		return elementDao.classifySameNameTest(typeName);
	}


	@Override
	@Transactional(readOnly=false)
	public void editClassifyName(ElemtypeBean typeBean) {
		elementDao.editClassifyName(typeBean);
	}


	@Override
	@Transactional(readOnly=false)
	public void insertClassifyType(Integer elemId, int type) {
		elementDao.insertClassifyType(elemId,type);
	}


	@Override
	public List<ElementBean> exists(ElementBean elementBean) {
		return elementDao.exists(elementBean);
	}


	@Override
	public List<ElementBean> findEleByMD5(String md5) {
		return elementDao.findEleByMD5(md5);
	}


	@Override
	@Transactional(readOnly=false)
	public void subMoveFromClassify(int elemId, int typeId) {
		elementDao.subMoveFromClassify(elemId,typeId);
	}


	@Override
	public List<ElementBean> sameRecordTest(String path) {
		return elementDao.sameRecordTest(path);
	}


	@Override
	@Transactional(readOnly=false)
	public void updateElementByNameAndMD5(ElementBean elementBean) {
		elementDao.updateElementByNameAndMD5(elementBean);
	}


	@Override
	public void saveStream(ElementBean elementbean) {
		elementDao.saveStream(elementbean);
	}


	@Override
	public List<ElementBean> findEleByMd5AndCreatorID(ElementBean elementBean) {
		return elementDao.findEleByMd5AndCreatorID(elementBean);
	}


	@Override
	public Integer queryCount(Integer elemId, int typeid) {
		return elementDao.queryCount(elemId,typeid);
	}


	@Override
	public List<Integer> queryTypeIdByIsprivate() {
		return elementDao.queryTypeIdByIsprivate();
	}


	@Override
	public List<ElementBean> FindElementIdsWithElementPath(List<String> elemId) {
		return elementDao.FindElementIdsWithElementPath(elemId);
	}

	@Override
	public Integer queryClassifyByTypeID(Integer typeValue){
		return elementDao.queryClassifyByTypeID(typeValue);
	}


	@Override
	public ElementBean queryElementById(Integer elemid) {
		return elementDao.queryElementById(elemid);
	}

	@Override
	public Integer queryCountElementByMd5(String MD5) {
		return elementDao.queryCountElementByMd5(MD5);
	}

	@Override
	public List<ElementBean> selectElementByMD5s(List<String> elemmd5s){
		return elementDao.selectElementByMD5s(elemmd5s);
	}
}
