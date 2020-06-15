package serviceImpl.license;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.license.LicenseService;
import beans.license.LicenseBean;
import dao.license.LicenseDao;
@Service
@Transactional(readOnly=true)
public class LicenseServiceImpl implements LicenseService{
	@Autowired
    private LicenseDao licenseDao;
	
	@Override
	@Transactional(readOnly=false)
	public void insertLicense(LicenseBean license) {
	    licenseDao.insertLicense(license);
	}

	@Override
	public List<LicenseBean> queryByLicense(String license) {
		return licenseDao.queryByLicense(license);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateLicense(LicenseBean license) {
		licenseDao.updateLicense(license);
	}
	@Override
	public List<String> queryMac() {
		return licenseDao.queryMac();
	}
	@Override
	public Integer queryLicenseCount() {
		return licenseDao.queryLicenseCount();
	}
}
