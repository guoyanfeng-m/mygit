package serviceImpl.cooperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import beans.cooperation.CooperationBean;
import dao.cooperation.CooperationDao;
import service.cooperation.CooperationService;
@Service
@Transactional(readOnly=true)
public class CooperationServiceImpl implements CooperationService{
	@Autowired
	private CooperationDao cooperationDao;

	

	

	@Override
	public List<CooperationBean> queryCooperationAll() {
		List<CooperationBean> list = cooperationDao.queryCooperationAll();
		return list;
	}
	
}
