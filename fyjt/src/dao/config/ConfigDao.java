package dao.config;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.config.ConfigBean;
@SuppressWarnings("rawtypes")
@Repository
public interface  ConfigDao {
	public String queryConfig(String key);
	public List queryConfigWEB();
	public List queryConfigFTP();
	public List queryConfigContact();
	public void insertConfig(ConfigBean configBean);
	public void updateConfig(ConfigBean configBean);
	public String queryFtpUrl(String key);
	public List<ConfigBean> selectConfig();
	public String queryElementPower();
	public void updateElementPower(@Param(value="elementPower")int elementPower);
}
