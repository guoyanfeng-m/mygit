package dao.program;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.program.ProgramBean;
import beans.program.ProgramViewBean;

@Repository
public interface  ProgramDao {
	public void insertProgram(ProgramBean programBean);
	public void deleteProgramByIds(List<Integer> idList);
	public Integer queryProgramCount(@Param(value="programBean")ProgramBean programBean, @Param(value="creatorid")Integer creatorid, @Param(value="userids")List<Integer> userids);
	public List<ProgramViewBean> queryProgram(Map<String,Object> map);
	public void updateProgram(ProgramBean programBean);
	public List<ProgramBean> queryProgramByIds(List<Integer> idList);
	public void insertProgramTerminal(Integer id);
	public List<ProgramBean> queryProgramByName(String name);
	public void auditProgram(ProgramBean programBean);
	@SuppressWarnings("rawtypes")
	public Integer queryElementCount(Map map);//wangyulin 2014/12/18
	//通过节目的id查找节目xml协议信息
	public String selectProgramXmlById(ProgramBean program);
}
