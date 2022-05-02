package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Override
    public UserEntity describeUserByName(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setAuthorityRole(AuthorityRole.TENANT_ADMIN);

        return userEntity;
    }

    @Override
    public UserEntity findUserById(UserId userId) {
        return null;
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return null;
    }

    @Override
    public boolean deleteUser(UserId userId) {
        return false;
    }

    @Override
    public PageDTO<UserEntity> users(PageDTO<UserEntity> pageDTO) {
        return null;
    }

    @Override
    public PageDTO<UserEntity> findTenantAdmins(PageDTO<UserEntity> pageDTO) {
        return null;
    }
}
