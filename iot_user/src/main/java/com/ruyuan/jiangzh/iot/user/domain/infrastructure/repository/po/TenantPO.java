package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiangzh
 */
@TableName(value = "tenant")
public class TenantPO extends Model<TenantPO> {

    private static final long serialVersionUID = 1L;
    @TableId
    private String uuid;

    private String email;

    private String phone;

    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Tenant{" +
        ", uuid=" + uuid +
        ", email=" + email +
        ", phone=" + phone +
        ", name=" + name +
        "}";
    }
}
