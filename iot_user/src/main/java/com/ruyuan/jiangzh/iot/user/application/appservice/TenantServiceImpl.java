package com.ruyuan.jiangzh.iot.user.application.appservice;

import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.TenantRepository;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component(value = "tenantServiceImpl")
public class TenantServiceImpl implements TenantServiceAPI {

    @Resource
    private TenantRepository tenantRepository;

    @Override
    public List<TenantId> describeAllTenans() {
        return tenantRepository.queryTenantIds();
    }
}
