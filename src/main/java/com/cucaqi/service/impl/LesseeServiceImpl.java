package com.cucaqi.service.impl;

import com.cucaqi.entity.Lessee;
import com.cucaqi.mapper.LesseeMapper;
import com.cucaqi.service.ILesseeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
@Service
public class LesseeServiceImpl extends ServiceImpl<LesseeMapper, Lessee> implements ILesseeService {
    @Autowired
    private LesseeMapper lesseeMapper;
    @Override
    /**
     * 获取所有租户信息
     */
    public List<Lessee> getLesseeList(){
        return lesseeMapper.getLesseeList();
    }
@Override
    public Integer addLessee(Lessee lessee){
        return lesseeMapper.addLessee(lessee);
    }
    @Override
    public Integer deleteLessee(int id){
        return  lesseeMapper.deleteLessee(id);
    }
}
