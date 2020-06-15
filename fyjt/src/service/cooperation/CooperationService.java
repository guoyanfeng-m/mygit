/**
 * Description:
 * author:zhen
 * 2016年3月23日 下午5:38:43
 */
package service.cooperation;

import java.util.List;

import org.springframework.stereotype.Service;

import beans.cooperation.CooperationBean;



/**
 * @author gyf
 *
 */
@Service
public interface CooperationService {
	
	public List<CooperationBean> queryCooperationAll(); //全查询和模糊查询
	 

}
