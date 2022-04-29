package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.user.domain.entity.Tenant;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.TenantRepository;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl.mapper.TenantMapper;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.TenantPO;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TenantRepositoryImpl implements TenantRepository {

    @Resource
    private TenantMapper tenantMapper;

    @Override
    public Tenant findTenantById(TenantId tenantId) {
        String id = UUIDHelper.fromTimeUUID(tenantId.getUuid());
        QueryWrapper<TenantPO> wrapper = new QueryWrapper();
        wrapper.eq("uuid", id);

        List<TenantPO> tenants = tenantMapper.selectList(wrapper);
        if(tenants != null && tenants.size() > 0){
            return Tenant.transToEntity(tenants.get(0));
        }
        return null;
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        TenantPO tenantPO = Tenant.transToPo(tenant);
        tenantMapper.insert(tenantPO);

        return tenant;
    }

    @Override
    public boolean delTenant(TenantId tenantId) {
        String id = UUIDHelper.fromTimeUUID(tenantId.getUuid());
        QueryWrapper<TenantPO> wrapper = new QueryWrapper();
        wrapper.eq("uuid", id);

        int delete = tenantMapper.delete(wrapper);

        return delete == 0 ? false : true;
    }

    @Override
    public IPage<TenantPO> tenants(IPage<TenantPO> page, Wrapper<TenantPO> queryWrapper) {
        IPage<TenantPO> iPage = tenantMapper.selectPage(page, queryWrapper);
        return iPage;
    }

}
