package com.ruyuan.jiangzh.iot.user.interfaces.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;

public class UserDTO {

    private String userId;
    private String tenantId;
    private String username;
    private String authorityRole;
    private String email;
    private String phone;

    public UserDTO() {}

    public UserDTO(UserEntity entity){
        this.userId = entity.getId().getUuid().toString();
        this.tenantId = entity.getTenantId().getUuid().toString();
        this.authorityRole = entity.getAuthorityRole().name();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.username = entity.getUsername();
    }

    @JsonIgnore
    public UserEntity poToEntity(){
        UserEntity userEntity  =new UserEntity();
        if(userId != null){
            userEntity.setId(new UserId(IoTStringUtils.toUUID(userId)));
        }
        if (tenantId != null) {
            userEntity.setTenantId(new TenantId(IoTStringUtils.toUUID(tenantId)));
        }
        userEntity.setAuthorityRole(AuthorityRole.describeRoleByName(this.authorityRole));
        userEntity.setUsername(this.username);
        userEntity.setEmail(this.email);
        userEntity.setPhone(this.phone);
        return userEntity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthorityRole() {
        return authorityRole;
    }

    public void setAuthorityRole(String authorityRole) {
        this.authorityRole = authorityRole;
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
}
