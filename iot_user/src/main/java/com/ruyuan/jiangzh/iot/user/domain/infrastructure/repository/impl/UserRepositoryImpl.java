package com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl.mapper.UserMapper;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.po.UserPO;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Resource
    private UserMapper userMapper;

    /*
        用户名查询用户信息
     */
    @Override
    public UserEntity describeUserByName(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", username);
        UserPO po = findFirst(userMapper.selectList(queryWrapper));

        UserEntity userEntity = new UserEntity(po);
        return userEntity;
    }

    /*
        根据编号查询用户信息
     */
    @Override
    public UserEntity findUserById(UserId userId) {
        String dataId = UUIDHelper.fromTimeUUID(userId.getUuid());
        UserPO userPO = userMapper.selectById(dataId);
        if(userPO != null){
            return new UserEntity(userPO);
        }else{
            return null;
        }
    }

    /*
        保存用户信息
     */
    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        // 如果没有ID，则赋予一个ID编号
        if(userEntity.getId() == null){
            userEntity.setId(new UserId(UUIDHelper.genUuid()));
        }

        UserPO userPO = UserEntity.transToPo(userEntity);
        userMapper.insert(userPO);

        return userEntity;
    }

    /*
        删除用户信息
     */
    @Override
    public boolean delUser(UserId userId) {
        String dataId = UUIDHelper.fromTimeUUID(userId.getUuid());

        int deleted = userMapper.deleteById(dataId);
        return deleted == 0 ? false : true;
    }

    /*
        查询用户列表
     */
    @Override
    public PageDTO<UserEntity> users(PageDTO<UserEntity> input) {
        // 组织mybatis-plus的两个核心对象 ipage , queryWrapper
        IPage<UserPO> iPage = new Page<>(input.getNowPage(),input.getPageSize());
        QueryWrapper<UserPO> queryWrapper = null;
        if(input.getConditions().size() > 0){
            queryWrapper = new QueryWrapper<>();
            Set<String> keys = input.getConditions().keySet();
            for(String key : keys){
                // 拼接查询条件
                spellCondition(queryWrapper, key, input.getConditions().get(key));
            }
        }

        IPage<UserPO> page = userMapper.selectPage(iPage, queryWrapper);
        List<UserPO> records = page.getRecords();
        // 将PO转换为领域实体
        List<UserEntity> entities =
                records.stream().map(po -> new UserEntity(po)).collect(Collectors.toList());
        // 封装返回对象
        input.setResult(page.getTotal(), page.getPages(), entities);

        return input;
    }

    /*
        查询Tenant管理员列表
     */
    @Override
    public PageDTO<UserEntity> findTenantAdmins(PageDTO<UserEntity> input) {
        // 组织mybatis-plus的两个核心对象 ipage , queryWrapper
        IPage<UserPO> iPage = new Page<>(input.getNowPage(),input.getPageSize());
        QueryWrapper<UserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("authority_role", AuthorityRole.TENANT_ADMIN);
        if(input.getConditions().size() > 0){
            Set<String> keys = input.getConditions().keySet();
            for(String key : keys){
                // 拼接查询条件
                spellCondition(queryWrapper, key, input.getConditions().get(key));
            }
        }

        IPage<UserPO> page = userMapper.selectPage(iPage, queryWrapper);
        List<UserPO> records = page.getRecords();
        // 将PO转换为领域实体
        List<UserEntity> entities =
                records.stream().map(po -> new UserEntity(po)).collect(Collectors.toList());
        // 封装返回对象
        input.setResult(page.getTotal(), page.getPages(), entities);

        return input;
    }

    /*
        获取list中的第一条
     */
    private UserPO findFirst(List<UserPO> userPOs){
        if(userPOs != null && userPOs.size()>0){
            return userPOs.stream().findFirst().get();
        }
        return null;
    }

    /*
        拼接查询条件
     */
    private void spellCondition(QueryWrapper queryWrapper, String fieldName, Object fieldValue){
        switch (fieldName){
            case "email":
                // 非空判断
                queryWrapper.like("email", fieldValue);
                break;
            case "phone":
                queryWrapper.eq("phone", fieldValue);
                break;
            case "tenantId":
                UUID tenantId = IoTStringUtils.toUUID(fieldValue + "");
                queryWrapper.eq("tenant_id", UUIDHelper.fromTimeUUID(tenantId));
                break;
        }
    }

    private UserEntity currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = null;
        if(authentication.getPrincipal() instanceof SecurityUser){
            securityUser = (SecurityUser)authentication.getPrincipal();
        }
        return securityUser;
    }

}
