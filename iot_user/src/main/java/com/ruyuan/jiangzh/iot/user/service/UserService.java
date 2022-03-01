package com.ruyuan.jiangzh.iot.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.user.dao.entity.IotUser;
import com.ruyuan.jiangzh.iot.user.dao.mapper.IotUserMapper;
import com.ruyuan.jiangzh.service.sdk.UserServiceAPI;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService implements UserServiceAPI {

    @Resource
    private IotUserMapper iotUserMapper;

    public List<IotUser> showXml(){
        List<IotUser> users = iotUserMapper.getAll();
        users.stream().forEach(
                System.out::println
        );

        return users;
    }

    public void insert(){
        IotUser user = new IotUser();

        user.setUuid("222");
        user.setUserName("Allen");

        iotUserMapper.insert(user);

    }

    public IotUser getById(String uuid){
        IotUser user = iotUserMapper.selectById(uuid);
        System.out.println("getById user = " + user);
        return user;
    }

    public void update(){
        IotUser user = new IotUser();

        user.setUuid("222");
        user.setUserName("天涯");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("user_name", "Allen");

        iotUserMapper.update(user, queryWrapper);
    }

    public void delete(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("user_name", "天");

        iotUserMapper.delete(queryWrapper);
    }

    public void selectCondition(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("user_name", "天");

        List<IotUser> list = iotUserMapper.selectList(queryWrapper);
        list.stream().forEach(
                System.out::println
        );
    }

    public void selectByPage(){

        Page<IotUser> page = new Page<>();
        page.setCurrent(1);
        page.setSize(1);

        IPage<IotUser> iotUserIPage = iotUserMapper.selectPage(page, null);
        System.out.println(iotUserIPage.getTotal()+" , "+iotUserIPage.getPages());
        iotUserIPage.getRecords().stream().forEach(
                System.out::println
        );
    }

    public void query(){
        List<IotUser> iotUsers = iotUserMapper.selectList(null);
        iotUsers.stream().forEach(
                System.out::println
        );
    }

    @Override
    public String getUserAll() {
        List<IotUser> iotUsers = showXml();
        String userJson = new Gson().toJson(iotUsers);
        return userJson;
    }
}
