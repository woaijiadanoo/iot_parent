package com.ruyuan.jiangzh.service.sdk;

import com.ruyuan.jiangzh.service.dto.SecurityUserDTO;

public interface UserServiceAPI {

    SecurityUserDTO describeUserByName(String username);

}
