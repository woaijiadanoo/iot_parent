package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;

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
