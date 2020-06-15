package serviceImpl.carousel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.carousel.CarouselService;
import util.PageInfo;
import beans.carousel.CarouselBean;
import dao.carousel.CarouselDao;
@Service
@Transactional(readOnly=true)
public class CarouselServiceImpl implements CarouselService{
	@Autowired
	private CarouselDao carouselDao;

	@Override
	public List<CarouselBean> queryCarousel(PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("offset", pageInfo.getStart());
		map.put("limit", pageInfo.getEnd());
		List<CarouselBean> list = carouselDao.queryCarousel(map);
		return list;
	}

	@Override
	@Transactional(readOnly=false)
	public void updateCarouselById(CarouselBean carouselBean) {
		carouselDao.updateCarouselById(carouselBean);
		
	}

	@Override
	@Transactional(readOnly=false)
	public void insertCarousel(CarouselBean carouselBean) {
		carouselDao.insertCarousel(carouselBean);
		
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteCarouselById(int carouselId) {
		carouselDao.deleteCarouselById(carouselId);
	}

	@Override
	public List<CarouselBean> queryCarouselAll() {
		List<CarouselBean> list = carouselDao.queryCarouselAll();
		return list;
	}
	
}
