package serviceImpl.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.config.ConfigService;
import beans.config.ConfigBean;
import dao.config.ConfigDao;

@SuppressWarnings("rawtypes")
@Service
@Transactional(readOnly=true)
public class ConfigServiceImpl implements ConfigService{
	@Autowired
	private ConfigDao configDao;
	
	@Transactional(readOnly=false)
	public void insertConfig(ConfigBean configBean) {
		configDao.insertConfig(configBean);
	}
	@Override
	public List queryConfigWEB() {
		return configDao.queryConfigWEB();
	}
	@Transactional(readOnly=false)
	public void updateConfig(ConfigBean configBean){
		 configDao.updateConfig(configBean);
	}

	@Override
	public List queryConfigFTP() {
		return configDao.queryConfigFTP();
	}
	@Override
	public List queryConfigContact() {
		return configDao.queryConfigContact();
	}
	@Override
	public String queryConfig(String key) {
		return configDao.queryConfig(key);
	}
	@Override
	public String queryFtpUrl(String key) {
		return configDao.queryFtpUrl(key);
	}
	@Override
	public List<ConfigBean> selectConfig() {
		return configDao.selectConfig();
	}
	@Override
	public String queryElementPower() {
		return configDao.queryElementPower();
	}
	@Override
	@Transactional(readOnly=false)
	public void updateElementPower(int elementPower) {
		configDao.updateElementPower(elementPower);
	}
	
}
