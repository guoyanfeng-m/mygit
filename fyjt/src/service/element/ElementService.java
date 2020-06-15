package service.element;

import java.util.List;

import util.PageInfo;
import beans.config.ConfigBean;
import beans.element.ElementBean;
import beans.element.ElemtypeBean;
public interface ElementService {

	void addElement(ElementBean elementBean);

	List<ElementBean> queryElement(ElementBean elementBean, PageInfo pageInfo, Integer creatorid, List<Integer> userids, List<Integer> typeidList);

	void deleteElementById(ElementBean elementBean);

	String getElementNameById(Integer id);

	String findLocation(Integer id);

	void updateElementById(ElementBean elementBean);

	void auditElementById(ElementBean elementBean);
	
	Integer queryTerminalCount(ElementBean elementBean, Integer creatorid, List<Integer> userids, List<Integer> typeidList);

	List<ConfigBean> queryFtpHttpMessage(ConfigBean configbean);

	void saveHtml(ElementBean elementbean);

	String findFtpLocation();

	Integer findElementbeanid(ElementBean elementbean);

	void insertText(ElementBean elementbean);

	String findWebLocation(Integer id);

	List<ElementBean> sameNameTest(String eleName);

	String queryCreatorById(String userId);

	void insertClassify(String typeName, int parseInt, Integer creatorid, int isprivate);

	void deleteClassifyElement(int parseInt);

	List<ElemtypeBean> queryClassify(ElemtypeBean elemtypeBean);

	List<ElemtypeBean> queryClassifyName(ElemtypeBean elemtypeBean);

	void addToClassify(int elemId, int typeId);

	Integer queryClassifyTotalCount(ElementBean elementBean);

	Integer querySameToClassify(int elemId, int typeId);

	String[] deleteTypeById(int typeid);

	void deleteClassifyById(int typeid);

	List<ElemtypeBean> classifySameNameTest(String typeName);

	void editClassifyName(ElemtypeBean typeBean);

	void insertClassifyType(Integer elemId, int type);

	List<ElementBean> exists(ElementBean elementBean);

	List<ElementBean> findEleByMD5(String md5);

	void subMoveFromClassify(int elemId, int typeId);

	List<ElementBean> sameRecordTest(String path);
	
	ElementBean queryElementById(Integer elemid);

	void updateElementByNameAndMD5(ElementBean elementBean);

	void saveStream(ElementBean elementbean);

	List<ElementBean> findEleByMd5AndCreatorID(ElementBean elementBean);

	Integer queryCount(Integer elemId, int typeid);

	List<Integer> queryTypeIdByIsprivate();

	List<ElementBean> FindElementIdsWithElementPath(List<String> elemId);//redis操作通过素材文件路径查找素材的id

	Integer queryClassifyByTypeID(Integer typeValue);

	String getClassifyNameById(Integer id);
	
	/**
	 * 通过MD5来查找数据库中是否有相同数据
	 * @param MD5
	 * @return
	 */
	public Integer queryCountElementByMd5(String MD5);

	public List<ElementBean> selectElementByMD5s(List<String> elemmd5s);

}
