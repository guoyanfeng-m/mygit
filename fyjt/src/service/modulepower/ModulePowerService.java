package service.modulepower;

import java.util.List;

import beans.modulepower.ModulepowerParametersBean;

@SuppressWarnings("rawtypes")
public interface ModulePowerService {
	 List queryModule(int userid);
	 List queryModulePower(ModulepowerParametersBean pb);
	 List queryModulePowerID(int userid);
}
