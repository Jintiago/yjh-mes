package cn.sourceplan.system.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.system.mapper.CarouselMapper;
import cn.sourceplan.system.domain.Carousel;
import cn.sourceplan.system.service.ICarouselService;

/**
 * 轮播图Service业务层处理
 *
 * @author jinzhong
 * @date 2023-04-19
 */
@Service
public class CarouselServiceImpl implements ICarouselService
{
    @Autowired
    private CarouselMapper carouselMapper;

    /**
     * 查询轮播图
     *
     * @param id 轮播图主键
     * @return 轮播图
     */
    @Override
    public Carousel selectCarouselById(Long id)
    {
        return carouselMapper.selectById(id);
    }

    /**
     * 查询轮播图列表
     *
     * @param carousel 轮播图
     * @return 轮播图
     */
    @Override
    public List<Carousel> selectCarouselList(Carousel carousel)
    {
        QueryWrapper< Carousel> qw = new QueryWrapper<>(carousel);
        return carouselMapper.selectList(qw);
    }

    /**
     * 新增轮播图
     *
     * @param carousel 轮播图
     * @return 结果
     */
    @Override
    public int insertCarousel(Carousel carousel)
    {
        return carouselMapper.insert(carousel);
    }

    /**
     * 修改轮播图
     *
     * @param carousel 轮播图
     * @return 结果
     */
    @Override
    public int updateCarousel(Carousel carousel)
    {
        return carouselMapper.updateById(carousel);
    }

    /**
     * 批量删除轮播图
     *
     * @param ids 需要删除的轮播图主键
     * @return 结果
     */
    @Override
    public int deleteCarouselByIds(Long[] ids)
    {
        return carouselMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除轮播图信息
     *
     * @param id 轮播图主键
     * @return 结果
     */
    @Override
    public int deleteCarouselById(Long id)
    {
        return carouselMapper.deleteById(id);
    }
}
