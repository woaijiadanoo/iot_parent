package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;

public interface UserRepository {
    // 根据用户名称获取User信息
    UserEntity describeUserByName(String username);
    // 根据Id查询用户信息
    UserEntity findUserById(UserId userId);
    // 保存用户
    UserEntity saveUser(UserEntity userEntity);
    // 删除用户
    boolean deleteUser(UserId userId);
    // 获取用户列表
    PageDTO<UserEntity> users(PageDTO<UserEntity> pageDTO);
    // 获取tenant管理员列表
    PageDTO<UserEntity> findTenantAdmins(PageDTO<UserEntity> pageDTO);
}
