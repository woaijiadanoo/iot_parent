package com.ruyuan.jiangzh.iot.user.controller;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.user.dao.entity.IotUser;
import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     *  http://localhost:8081/api/user/getall
     * @return
     */
    @RequestMapping(value = "/getall",method = RequestMethod.GET)
    public RespDTO getUser(){
        List<IotUser> iotUsers = userService.showXml();

        // 假装有传入一个username
        String msgId = "user.username.ugly";
        String username = "Allen";
        Object[] params = new Object[]{username};
        if(username.trim().length() >  0){
            throw new AppException(RespCodeEnum.RESOURCE_NOT_EXISTS.getCode(), msgId,params);
        }

        return RespDTO.success(iotUsers);
    }

    /*
        创建用户， 之所以不用POST是为了演示方便
        http://localhost:8081/api/user/saveUser?username=allen
     */
    @RequestMapping(value = "/saveUser",method = RequestMethod.GET)
    public RespDTO saveUser(String username){
        // 加入一个新的UUID生成规则
        UserId userId = new UserId(UUIDHelper.genUuid());

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setUsername(username);

        userService.insert(userDTO);

        return RespDTO.success(userDTO);
    }

}
