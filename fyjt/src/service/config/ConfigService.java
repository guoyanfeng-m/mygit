package service.config;

import java.util.List;

import beans.config.ConfigBean;

@SuppressWarnings("rawtypes")
public interface ConfigService {
	public String queryConfig(String key);
	List queryConfigWEB();
	List queryConfigFTP();
	List queryConfigContact();
	void insertConfig(ConfigBean configBean);
	public void updateConfig(ConfigBean configBean);
	String queryFtpUrl(String key);
	public List<ConfigBean> selectConfig();
	public String queryElementPower();
	public void updateElementPower(int elementPower);
}
