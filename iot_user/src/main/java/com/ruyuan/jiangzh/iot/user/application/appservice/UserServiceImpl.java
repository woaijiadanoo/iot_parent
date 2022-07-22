package com.ruyuan.jiangzh.iot.user.application.appservice;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.user.infrastructure.utils.UserUtils;
import com.ruyuan.jiangzh.service.dto.SecurityUserDTO;
import com.ruyuan.jiangzh.service.sdk.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserServiceAPI, UserAppService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public SecurityUserDTO describeUserByName(String username) {
        UserEntity userEntity = userRepository.describeUserByName(username);

        SecurityUserDTO securityUserDTO = new SecurityUserDTO();

        securityUserDTO.setTenantId(userEntity.getTenantId());
        securityUserDTO.setUserId(userEntity.getId());
        securityUserDTO.setUsername(userEntity.getUsername());
        securityUserDTO.setAuthorityRole(userEntity.getAuthorityRole());
        securityUserDTO.setEmail(userEntity.getEmail());
        securityUserDTO.setPhone(userEntity.getPhone());

        return securityUserDTO;
    }

    @Override
    public UserEntity findUserById(UserId userId){
        // 获取当前用户
        SecurityUser currentUser = currentUser();
        // 根据ID查询用户
        UserEntity userEntity = userRepository.findUserById(userId);
        // 比较tenantId是否一致
        if(userEntity.getTenantId().equals(currentUser.getTenantId())){
            return userEntity;
        }else{
            // 可以加报警，为什么会有跨tenant的查询
            return null;
        }
    }

    @Override
    public boolean delUser(UserId userId) {
        // 获取当前用户
        SecurityUser currentUser = currentUser();
        // 根据ID查询用户
        UserEntity userEntity = userRepository.findUserById(userId);
        if(userEntity == null){
            // 可以加报警，为什么会有跨tenant的查询,有可能是脏读，有可能是外部攻击
            return true;
        }
        // 比较tenantId是否一致
        if(userEntity.getTenantId().equals(currentUser.getTenantId())){
            return userRepository.delUser(userId);
        }else{
            // 可以加报警，为什么会有跨tenant的查询
            return false;
        }
    }

    @Override
    public PageDTO<UserEntity> users(PageDTO<UserEntity> input) {
        // 获取当前用户
        SecurityUser currentUser = currentUser();
        input.spellCondition("tenantId", currentUser.getTenantId().getUuid().toString());

        PageDTO<UserEntity> users = userRepository.users(input);
        return users;
    }

    @Override
    public PageDTO<UserEntity> findTenantAdmins(PageDTO<UserEntity> input) {
        // 获取当前用户
        SecurityUser currentUser = currentUser();
        input.spellCondition("tenantId", currentUser.getTenantId().getUuid().toString());

        PageDTO<UserEntity> users = userRepository.findTenantAdmins(input);
        return users;
    }

    // 获取当前用户
    private SecurityUser currentUser(){
        return new UserUtils().getCurrentUser();
    }

}
