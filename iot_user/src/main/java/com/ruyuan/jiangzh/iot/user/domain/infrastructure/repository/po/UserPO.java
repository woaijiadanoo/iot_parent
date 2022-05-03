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
@TableName(value = "user")
public class UserPO extends Model<UserPO> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    private String tenantId;

    private String userName;

    private String phone;

    private String email;

    private String authorityRole;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthorityRole() {
        return authorityRole;
    }

    public void setAuthorityRole(String authorityRole) {
        this.authorityRole = authorityRole;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
        ", uuid=" + uuid +
        ", tenantId=" + tenantId +
        ", userName=" + userName +
        ", phone=" + phone +
        ", email=" + email +
        ", authorityRole=" + authorityRole +
        "}";
    }
}
