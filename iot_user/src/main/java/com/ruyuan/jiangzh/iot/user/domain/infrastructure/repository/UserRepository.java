package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;

public interface UserRepository {

    /*
        用户名查询用户信息
     */
    UserEntity describeUserByName(String username);

    /*
        根据编号查询用户信息
     */
    UserEntity findUserById(UserId userId);

    /*
        保存用户信息
     */
    UserEntity saveUser(UserEntity userEntity);

    /*
        删除用户信息
     */
    boolean delUser(UserId userId);

    /*
        查询用户列表
     */
    PageDTO<UserEntity> users(PageDTO<UserEntity> pageDTO);

    /*
        查询Tenant管理员列表
     */
    PageDTO<UserEntity> findTenantAdmins(PageDTO<UserEntity> pageDTO);

}
