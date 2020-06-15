/**
 * Description:
 * author:zhen
 * 2016年3月23日 下午5:38:43
 */
package service.carousel;

import java.util.List;

import org.springframework.stereotype.Service;

import util.PageInfo;
import beans.carousel.CarouselBean;


/**
 * @author gyf
 *
 */
@Service
public interface CarouselService {
	
	/**
	 * Description: 查询全部的轮播图
	 */
	List<CarouselBean> queryCarousel(PageInfo pageInfo);
	
	public List<CarouselBean> queryCarouselAll(); //全查询和模糊查询
	 
	void updateCarouselById(CarouselBean carouselBean);
	/**
	 * Description: 添加新的轮播图
	 */
	void insertCarousel(CarouselBean carouselBean);
	/**
	 * Description: 删除轮播图
	 */
	void deleteCarouselById(int carouselId);

}
