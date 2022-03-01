package com.ruyuan.jiangzh.iot.user.dao.mapper;

import com.ruyuan.jiangzh.iot.user.dao.entity.IotUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiangzh
 * @since 2022-02-28
 */
public interface IotUserMapper extends BaseMapper<IotUser> {

    List<IotUser> getAll();

}
