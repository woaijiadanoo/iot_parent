package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.TenantPO;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.io.Serializable;
import java.util.Objects;

public class Tenant extends CreateTimeIdBase implements Serializable {

    private String email;
    private String phone;
    private String name;

    public Tenant(){}

    public Tenant(TenantId id){
        super(id);
    }

    public Tenant(TenantPO tenantPO){
        super(new TenantId(UUIDHelper.fromStringId(tenantPO.getUuid())));
        this.name = tenantPO.getName();
        this.phone = tenantPO.getPhone();
        this.email = tenantPO.getEmail();
    }

    public static TenantPO transToPo(Tenant tenant){
        TenantPO tenantPO = new TenantPO();
        tenantPO.setUuid(UUIDHelper.fromTimeUUID(tenant.getId().getUuid()));
        tenantPO.setName(tenant.getName());
        tenantPO.setEmail(tenant.getEmail());
        tenantPO.setPhone(tenant.getPhone());
        return tenantPO;
    }

    public static Tenant transToEntity(TenantPO tenantPO){
        TenantId tenantId = new TenantId(UUIDHelper.fromStringId(tenantPO.getUuid()));
        Tenant tenant = new Tenant(tenantId);
        tenant.setName(tenantPO.getName());
        tenant.setPhone(tenantPO.getPhone());
        tenant.setEmail(tenantPO.getEmail());
        return tenant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tenant)) return false;
        if (!super.equals(o)) return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(getEmail(), tenant.getEmail()) && Objects.equals(getPhone(), tenant.getPhone()) && Objects.equals(getName(), tenant.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getPhone(), getName());
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
