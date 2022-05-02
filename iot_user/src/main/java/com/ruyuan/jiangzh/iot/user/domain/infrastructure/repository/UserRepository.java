package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;

public interface UserRepository {

    UserEntity describeUserByName(String username);

}
