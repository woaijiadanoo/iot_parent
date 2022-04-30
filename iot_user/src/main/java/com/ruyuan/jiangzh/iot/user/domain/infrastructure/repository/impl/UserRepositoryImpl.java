package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
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
}
