package com.cucaqi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cucaqi.entity.Answerer;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GaoSong Xu
 * @since 2022-10-31
 */
public interface IAnswererService extends IService<Answerer> {
    public List<Answerer> listAnswererByGroupId(Integer groupId);
}
