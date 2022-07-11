package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.TenantPO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiangzh
 * @since 2022-04-29
 */
public interface TenantMapper extends BaseMapper<TenantPO> {

    List<String> queryAllTenantIds();

}
