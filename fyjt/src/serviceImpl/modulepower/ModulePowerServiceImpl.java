package serviceImpl.modulepower;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.modulepower.ModulePowerService;
import beans.modulepower.ModulepowerParametersBean;
import dao.modulepower.ModulePowerDao;

@SuppressWarnings("rawtypes")
@Service
@Transactional(readOnly=true)
public class ModulePowerServiceImpl implements ModulePowerService{
	@Autowired
	private ModulePowerDao modulePowerDao;


	@Override
	public List queryModule(int userid) {
		return modulePowerDao.queryModule(userid);
	}


	@Override
	public List queryModulePower(ModulepowerParametersBean pb) {
		return modulePowerDao.queryModulePower(pb);
	}


	@Override
	public List queryModulePowerID(int userid) {
		return modulePowerDao.queryModulePowerID(userid);
	}
	
}