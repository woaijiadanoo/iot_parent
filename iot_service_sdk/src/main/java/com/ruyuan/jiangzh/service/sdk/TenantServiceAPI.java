package com.ruyuan.jiangzh.service.sdk;

import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.List;

public interface TenantServiceAPI {

    List<TenantId> describeAllTenans();

}
