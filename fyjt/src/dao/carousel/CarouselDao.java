package dao.carousel;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import beans.carousel.CarouselBean;
@Repository
public interface CarouselDao {
	
	@SuppressWarnings("rawtypes")
	public List<CarouselBean> queryCarousel(Map map);
	public List<CarouselBean> queryCarouselAll();
	public void updateCarouselById(CarouselBean carouselBean);
	public void insertCarousel(CarouselBean carousel);
	public void deleteCarouselById(int carouselId);
}
