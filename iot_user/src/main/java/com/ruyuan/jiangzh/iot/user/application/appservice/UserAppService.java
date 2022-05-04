package com.ruyuan.jiangzh.iot.user.application.appservice;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;

public interface UserAppService {

    UserEntity findUserById(UserId userId);

    boolean delUser(UserId userId);

    PageDTO<UserEntity> users(PageDTO<UserEntity> input);

    PageDTO<UserEntity> findTenantAdmins(PageDTO<UserEntity> input);

}
