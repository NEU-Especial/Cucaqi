package com.cucaqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cucaqi.entity.Lessee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@Mapper
public interface LesseeMapper extends BaseMapper<Lessee> {
    @Select("select * from t_lessee")
    /**
     * 获取所有租户信息
     */
    public List<Lessee> getLesseeList();


}
