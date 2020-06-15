package dao.modulepower;

import java.util.List;

import org.springframework.stereotype.Repository;

import beans.modulepower.ModulepowerParametersBean;
@SuppressWarnings("rawtypes")
@Repository
public interface  ModulePowerDao {
	 public List queryModule(int userid);
	 public List queryModulePower(ModulepowerParametersBean pb);
	 public List queryModulePowerID(int userid);
}
