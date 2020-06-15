package dao.element;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.config.ConfigBean;
import beans.element.ElementBean;
import beans.element.ElemtypeBean;
@Repository
public interface ElementDao {

	public void addElement(ElementBean elementBean);

	@SuppressWarnings("rawtypes")
	public List<ElementBean> queryElement(Map map);

	public void deleteElementById(ElementBean elementBean);

	public String getElementNameById(Integer id);

	public String findLocation(Integer id);

	public void updateElementById(ElementBean elementBean);

	public void auditElementById(ElementBean elementBean);

	public Integer queryTerminalCount(@Param(value="elementBean")ElementBean elementBean,@Param(value="creatorid") Integer creatorid,@Param(value="userids") List<Integer> userids,@Param(value="typeidList")  List<Integer> typeidList);

	public List<ConfigBean> queryFtpHttpMessage(ConfigBean configbean);

	public void saveHtml(ElementBean elementbean);

	public String findFtpLocation();

	public Integer findElementbeanid(ElementBean elementbean);

	public void insertText(ElementBean elementbean);

	public String findWebLocation(Integer id);

	public List<ElementBean> sameNameTest(String eleName);

	public String queryCreatorById(String userId);

	public void insertClassify(@Param(value="typeName") String typeName, @Param(value="parseInt") Integer  parseInt, @Param(value="creatorid")Integer creatorid, @Param(value="isprivate")int isprivate);

	public void deleteClassifyElement(@Param(value="parseInt") Integer parseInt);

	public List<ElemtypeBean> queryClassify(ElemtypeBean elemtypeBean);

	public List<ElemtypeBean> queryClassifyName(ElemtypeBean elemtypeBean);

	public void addToClassify(@Param(value="elemId") Integer elemId, @Param(value="typeId") Integer typeId);

	public Integer queryClassifyTotalCount(ElementBean elementBean);

	public Integer querySameToClassify(@Param(value="elemId") Integer elemId, @Param(value="typeId") Integer typeId);

	public void deleteClassifyById(int typeid);

	public void deleteTypeById(int typeid);

	public List<ElemtypeBean> classifySameNameTest(String typeName);

	public void editClassifyName(ElemtypeBean typeBean);

	public void insertClassifyType(@Param(value="elemId") Integer elemId, @Param(value="type") int type);

	public List<ElementBean> exists(ElementBean elementBean);

	public List<ElementBean> findEleByMD5(String md5);

	public void subMoveFromClassify(@Param(value="elemId") Integer elemId, @Param(value="typeId") Integer typeId);

	public List<ElementBean> sameRecordTest(String path);
	
	public ElementBean queryElementById(@Param(value="elem_id") Integer elem_id);

	public void updateElementByNameAndMD5(ElementBean elementBean);

	public void saveStream(ElementBean elementbean);

	public List<ElementBean> findEleByMd5AndCreatorID(ElementBean elementBean);

	public Integer queryCount(@Param(value="elemId") Integer elemId,@Param(value="typeid") Integer typeid);

	public List<Integer> queryTypeIdByIsprivate();

	public List<ElementBean> FindElementIdsWithElementPath(@Param(value="elemId")List<String> elemId);

	public String[] queryElementIdByClassify(int typeid);

	public Integer queryClassifyByTypeID(Integer typeValue);

	public String getClassifyNameById(Integer id);

	/**
	 * 通过MD5来查找数据库中是否有相同数据
	 * @param MD5
	 * @return
	 */
	public Integer queryCountElementByMd5(String MD5);

	public List<ElementBean> selectElementByMD5s(List<String> elemmd5s);
}
