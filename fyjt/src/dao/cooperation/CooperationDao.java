package dao.cooperation;

import java.util.List;

import org.springframework.stereotype.Repository;

import beans.cooperation.CooperationBean;;;
@Repository
public interface CooperationDao {
	
	public List<CooperationBean> queryCooperationAll();
}
