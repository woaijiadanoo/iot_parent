package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.Tenant;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.TenantRepository;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl.mapper.TenantMapper;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.TenantPO;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        // 生成TenantId
        TenantId tenantId = new TenantId(UUIDHelper.genUuid());
        tenant.setId(tenantId);

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
    public PageDTO<Tenant> tenants(PageDTO<Tenant> page) {
       // 要组织mybatis-plus需要的请求数据
        IPage<TenantPO> input = new Page<>(page.getNowPage(), page.getPageSize());
        QueryWrapper queryWrapper = null;
        if(page.getConditions().size() > 0){
            queryWrapper = new QueryWrapper();
            Set<String> keys = page.getConditions().keySet();
            for(String key : keys){
                // 给queryWrapper填充条件信息
                spellCondition(queryWrapper, key, page.getConditions().get(key));
            }
        }

        IPage output = tenantMapper.selectPage(input, queryWrapper);
        List<TenantPO> records = output.getRecords();

        // 转换为领域实体
        List<Tenant> result = records.stream().map(po -> new Tenant(po)).collect(Collectors.toList());

        // 组织返回对象
        page.setResult(output.getTotal(), output.getPages(), result);

        return page;
    }

    @Override
    public List<TenantId> queryTenantIds() {
        List<String> dataIds = tenantMapper.queryAllTenantIds();
        List<TenantId> result =
                dataIds.stream().map(dataId -> {
                    UUID uuid = UUIDHelper.fromStringId(dataId);
                    return new TenantId(uuid);
                }).collect(Collectors.toList());
        return result;
    }

    private void spellCondition(QueryWrapper queryWrapper, String fieldName, Object fieldValue){
        // 也可以用switch，自行决定就好
        if("email".equalsIgnoreCase(fieldName)){
            if(fieldValue != null){
                queryWrapper.like("email", fieldValue);
            }
        }else if("phone".equalsIgnoreCase(fieldName)){
            queryWrapper.eq("phone", fieldValue);
        }
    }


}
