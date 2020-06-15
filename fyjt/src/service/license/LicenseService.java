package service.license;

import java.util.List;

import beans.license.LicenseBean;
public interface LicenseService {
	  public void insertLicense(LicenseBean license);
	  public void updateLicense(LicenseBean license);
	  public List<LicenseBean> queryByLicense(String license);
	  public List<String> queryMac();
	  public Integer queryLicenseCount();
}
