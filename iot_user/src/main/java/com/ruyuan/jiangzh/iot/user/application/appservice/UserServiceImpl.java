package com.ruyuan.jiangzh.iot.user.application.appservice;

import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.service.dto.SecurityUserDTO;
import com.ruyuan.jiangzh.service.sdk.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserServiceAPI {

    @Autowired
    private UserRepository userRepository;

    @Override
    public SecurityUserDTO describeUserByName(String username) {
        UserEntity userEntity = userRepository.describeUserByName(username);

        SecurityUserDTO securityUserDTO = new SecurityUserDTO();

        securityUserDTO.setTenantId(userEntity.getTenantId().getUuid());
        securityUserDTO.setUserId(userEntity.getUserId().getUuid());
        securityUserDTO.setUsername(userEntity.getUsername());
        securityUserDTO.setEmail(userEntity.getEmail());
        securityUserDTO.setPhone(userEntity.getPhone());
        securityUserDTO.setAuthorityRole(userEntity.getAuthorityRole());

        return securityUserDTO;
    }

}
