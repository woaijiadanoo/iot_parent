package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.TenantPO;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.UserPO;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;

import java.io.Serializable;

public class UserEntity extends CreateTimeIdBase<UserId> implements Serializable {

    private TenantId tenantId;
    private String username;
    private AuthorityRole authorityRole;
    private String email;
    private String phone;

    public UserEntity(){}

    public UserEntity(UserId id){
        super(id);
    }

    public UserEntity(UserPO userPO){
        super(new UserId(UUIDHelper.fromStringId(userPO.getUuid())));
        this.tenantId = new TenantId(UUIDHelper.fromStringId(userPO.getTenantId()));
        this.username = userPO.getUserName();
        this.email = userPO.getEmail();
        this.phone = userPO.getPhone();
        this.authorityRole = AuthorityRole.describeRoleByName(userPO.getAuthorityRole());
    }

    public static UserPO transToPo(UserEntity userEntity){
       UserPO userPO = new UserPO();
        userPO.setUuid(UUIDHelper.fromTimeUUID(userEntity.getId().getUuid()));
        userPO.setTenantId(UUIDHelper.fromTimeUUID(userEntity.getTenantId().getUuid()));
        userPO.setUserName(userEntity.getUsername());
        userPO.setEmail(userEntity.getEmail());
        userPO.setPhone(userEntity.getPhone());
        userPO.setAuthorityRole(userEntity.getAuthorityRole().name());

        return userPO;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthorityRole getAuthorityRole() {
        return authorityRole;
    }

    public void setAuthorityRole(AuthorityRole authorityRole) {
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
