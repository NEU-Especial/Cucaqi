package com.cucaqi.service.impl;

import com.cucaqi.entity.Lessee;
import com.cucaqi.mapper.LesseeMapper;
import com.cucaqi.mapper.UserMapper;
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
    @Autowired
    private UserMapper userMapper;
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

    /**
     * 删除租户
     * @param id
     * @return
     */
    @Override
    public Integer deleteLessee(int id){

        Integer count = userMapper.tellLessee(id);
        //判断用户表中是否有关联的该租户
        if(count==0){
            //无关联租户，即可删除该租户
            return  lesseeMapper.deleteLessee(id);
        }
        else{
            //有关联租户，即不可删除该租户
            return -100;
        }
    }

    /**
     * 查询是否有该用户名的租户，若有，返回true
     * @param username
     * @return
     */
    @Override
    public boolean searchLessee(String username){
        Lessee searchLessee = lesseeMapper.searchLessee(username);
        if(searchLessee==null){
            return  false;
        }
        return true;

    }

    @Override
    public List<Lessee> getDeletedLesseeList() {
        return lesseeMapper.getDeletedLesseeList();
    }

}
