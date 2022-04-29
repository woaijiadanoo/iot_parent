package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruyuan.jiangzh.iot.user.domain.entity.Tenant;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;

public interface TenantRepository {

    // 根据TenantId获取Tenant对象
    Tenant findTenantById(TenantId tenantId);

    // 保存Tenant对象
    Tenant saveTenant(Tenant tenant);

    // 删除Tenant对象
    boolean delTenant(TenantId tenantId);

    // 列表查询
    IPage<Tenant> tenants(IPage<Tenant> page, Wrapper<Tenant> queryWrapper);

}
