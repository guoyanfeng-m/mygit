package dao.license;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import beans.license.LicenseBean;
@Repository
public interface LicenseDao {
   public void insertLicense(LicenseBean license);       //插入
   public void updateLicense(LicenseBean license);      //修改
   public List<LicenseBean> queryByLicense(@Param(value="license") String license); //根据license is null 查询数据
   public List<String> queryMac();        //查询所有mac
   public Integer queryLicenseCount();     //查询总数
}
