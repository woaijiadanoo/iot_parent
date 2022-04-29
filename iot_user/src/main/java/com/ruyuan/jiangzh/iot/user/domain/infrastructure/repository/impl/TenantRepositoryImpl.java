package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruyuan.jiangzh.iot.user.domain.entity.Tenant;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.TenantRepository;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;

public class TenantRepositoryImpl implements TenantRepository {

    @Override
    public Tenant findTenantById(TenantId tenantId) {
        return null;
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        return null;
    }

    @Override
    public boolean delTenant(TenantId tenantId) {
        return false;
    }

    @Override
    public IPage<Tenant> tenants(IPage<Tenant> page, Wrapper<Tenant> queryWrapper) {
        return null;
    }

}
