package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.user.application.appservice.UserAppService;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.user.infrastructure.utils.UserUtils;
import com.ruyuan.jiangzh.iot.user.interfaces.dto.UserDTO;
import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;
import com.ruyuan.jiangzh.service.sdk.DeviceServiceAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/*
    TODO 将返回值里的UserEntity 转换为 UserDTO做返回，体会一下非贫血模型的转换，并且相互讨论
 */
@RestController
@RequestMapping(value = "/api/v1")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String PARAMTER_IS_NULL_MSG_ID = "user.resource_is_empty";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAppService userAppService;
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
        http://localhost:8081/api/v1/user/940f0e60-c8a0-11ec-989e-8b76480d43cf?ruyuan_name=ruyuan_00
     */
    /*
            {
                "code": 404,
                "requestId": "a1da472947864b11b5d26503516d378b",
                "message": "resource is empty",
                "state": "resource_not_exists",
                "data": null
            }
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public RespDTO findUserById(@PathVariable("userId") String userIdStr){
        UserId  userId = new UserId(toUUID(userIdStr));

        UserEntity userById = userAppService.findUserById(userId);

        return RespDTO.success(checkNotNull(userById));
    }

    /*
        http://localhost:8081/api/v1/user/940f0e60-c8a0-11ec-989e-8b76480d43cf?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    public RespDTO delUser(@PathVariable("userId") String userIdStr){
        UserId  userId = new UserId(toUUID(userIdStr));

        userAppService.delUser(userId);

        return RespDTO.success();
    }

    /*
        http://localhost:8081/api/v1/users?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public RespDTO getUsers(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") long nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") long pageSize,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone){

        PageDTO<UserEntity> pageDTO = new PageDTO<>(nowPage, pageSize);
        spellCondition(pageDTO,"email", email);
        spellCondition(pageDTO,"phone", phone);

        PageDTO<UserEntity> users = userAppService.users(pageDTO);

        return RespDTO.success(users);
    }

    /*
    http://localhost:8081/api/v1/admins?ruyuan_name=ruyuan_00
 */
    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public RespDTO tenantAdmins(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") long nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") long pageSize,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone){

        PageDTO<UserEntity> pageDTO = new PageDTO<>(nowPage, pageSize);
        spellCondition(pageDTO,"email", email);
        spellCondition(pageDTO,"phone", phone);

        PageDTO<UserEntity> users = userAppService.findTenantAdmins(pageDTO);

        return RespDTO.success(users);
    }


    @Resource
    private DeviceServiceAPI deviceServiceAPI;

    /*
        http://localhost:8081/api/v1/user/device?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "/user/device", method = RequestMethod.GET)
    public RespDTO getDevice(){
        String productKey = "i4g423najn";
        String deviceName = "ry_device_02";
        String deviceSercet = "vvu317";

        DeviceSercetDTO deviceBySercet = deviceServiceAPI.findDeviceBySercet(productKey, deviceName, deviceSercet);

        System.out.println("deviceBySercet = " + deviceBySercet);

        return RespDTO.success(deviceBySercet);
    }


}
