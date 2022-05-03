package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;
import com.ruyuan.jiangzh.iot.user.infrastructure.utils.UserUtils;
import com.ruyuan.jiangzh.iot.user.interfaces.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String PARAMTER_IS_NULL_MSG_ID = "user.resource_is_empty";

    @Autowired
    private UserRepository userRepository;

    /*
        http://localhost:8081/api/v1/user?ruyuan_name=ruyuan_00

        {
            "username":"ruyuan_02",
            "authorityRole":"USER",
            "email":"ruyuan_02@gmail.com",
            "phone":"13822222222"
        }
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public RespDTO saveUser(@RequestBody UserDTO userDTO){
        try {
            UserUtils userUtils = new UserUtils();
            SecurityUser currentUser = userUtils.getCurrentUser();

            boolean newUser = userDTO.getUserId() == null ? true : false;
            // 待保存的Entity
            UserEntity input = userDTO.poToEntity();
            if(currentUser.getAuthorityRole() != AuthorityRole.SYS_ADMIN){
                // 这个用户的tenantId应该与当前登录的用户一致
                input.setTenantId(currentUser.getTenantId());
            }else{
                if(IoTStringUtils.isBlank(userDTO.getTenantId())){
                    // 系统管理员加入的用户必须加入tenantId
                    throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode(), PARAMTER_IS_NULL_MSG_ID);
                }
            }
            UserEntity output = userRepository.saveUser(input);

            UserDTO result = new UserDTO(checkNotNull(output));
            return RespDTO.success(result);
        } catch (Exception e){
            // 报警并记录日志
            logger.error("getTenantById error:[{}]", e.getMessage());
            // 封装成自定义异常
            return RespDTO.systemFailture(e);
        }
    }

    /*
        http://localhost:8081/api/users?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public RespDTO getUsers(){

//        SecurityUser currentUser = currentUser();
//        logger.info("currentUser : [{}]", new Gson().toJson(currentUser));

        UserId userId = new UserId(toUUID("940f0e60-c8a0-11ec-989e-8b76480d43cf"));
        UserEntity userById = userRepository.findUserById(userId);

        return RespDTO.success(userById);
    }

}
