package com.cucaqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cucaqi.entity.Lessee;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
public interface ILesseeService extends IService<Lessee> {
    /**
     * 获取所有租户信息
     */
    public List<Lessee> getLesseeList();

    /**
     * 添加租户
     * @param lessee
     * @return
     */
    public Integer addLessee(Lessee lessee);

    /**
     * 删除租户
     * @param lessee
     * @return
     */
    public Integer deleteLessee(int id);

}
