package com.ruyuan.jiangzh.iot.rule.interfaces.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleChainMetaDataEntity;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.factory.AggrRuleChainMetaDataFactory;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.RuleChainRepository;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.utils.ConsistContext;
import com.ruyuan.jiangzh.iot.rule.interfaces.dto.RuleChainDTO;
import com.ruyuan.jiangzh.iot.rule.interfaces.dto.RuleChainMetaDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class RuleChainController extends BaseController {

    @Autowired
    private RuleChainRepository ruleChainRepository;

    @Autowired
    private AggrRuleChainMetaDataFactory ruleChainMetaDataFactory;

    @Autowired
    private RuleChainDomainService ruleChainDomainService;


    /*
        http://localhost:8083/api/v1/ruleChains?ruyuan_name=ruyuan_00
        http://localhost:8083/api/v1/ruleChains?ruyuan_name=ruyuan_00&ruleChainName=ry_rc_01

{
    "code": 200,
    "requestId": "8fcab462050a4dee91047cd365deb204",
    "message": "",
    "state": "",
    "data": {
        "nowPage": 1,
        "pageSize": 10,
        "totals": 1,
        "totalPages": 1,
        "records": [
            {
                "ruleChainId": "f3dfd360-0fdb-11ed-8010-1de00285059b",
                "tenantId": "f20cc080-c89f-11ec-989e-8b76480d43cf",
                "userId": "f20cc080-c89f-11ec-989e-8b76480d43cf",
                "ruleChainName": "ry_rc_01",
                "firstRuleNodeId": null
            }
        ]
    }
}
     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChains", method = RequestMethod.GET)
    public RespDTO ruleChains(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") long nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") long pageSize,
            @RequestParam(name = "ruleChainName", required = false) String ruleChainName){
        // 组织分页条件参数
        IoTSecurityUser currentUser = getCurrentUser();
        PageDTO<RuleChainEntity> input = new PageDTO<>(nowPage, pageSize);
        if(!IoTStringUtils.isBlank(ruleChainName)){
            input.spellCondition(ConsistContext.RULE_CHAIN_QUERY_PARAM_NAME,  ruleChainName);
        }
        // 需要转换成DataId
        input.spellCondition(ConsistContext.TENANT_ID, UUIDHelper.fromTimeUUID(currentUser.getTenantId().getUuid()));

        // 根据entity查询数据
        PageDTO<RuleChainEntity> entityPageDTO = ruleChainRepository.ruleChains(input);

        // 将entity转换为dto
        List<RuleChainEntity> entities = entityPageDTO.getRecords();
        if(entities == null || entities.size() == 0){
            return RespDTO.success(entityPageDTO);
        }

        List<RuleChainDTO> dtos = entities.stream().map(e -> RuleChainDTO.entityToDto(e)).collect(Collectors.toList());

        // 设置返回数据
        PageDTO<RuleChainDTO> result = new PageDTO<>(nowPage, pageSize);
        result.setResult(entityPageDTO.getTotals(),entityPageDTO.getTotalPages(), dtos);

        return RespDTO.success(result);
    }

    /*
        http://localhost:8083/api/v1/ruleChain?ruyuan_name=ruyuan_00
        request:
            {
                "ruleChainName":"ry_rc_01"
            }

        response:

            {
                "code": 200,
                "requestId": null,
                "message": "",
                "state": "",
                "data": {
                    "ruleChainId": "f3dfd360-0fdb-11ed-8010-1de00285059b",
                    "tenantId": "f20cc080-c89f-11ec-989e-8b76480d43cf",
                    "userId": "f20cc080-c89f-11ec-989e-8b76480d43cf",
                    "ruleChainName": "ry_rc_01",
                    "firstRuleNodeId": null
                }
            }
     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChain", method = RequestMethod.POST)
    public RespDTO saveRuleChain(@RequestBody RuleChainDTO dto){
        IoTSecurityUser currentUser = getCurrentUser();

        // dto 转entity
        RuleChainEntity ruleChainEntity =
                RuleChainDTO.dtoToEntity(dto, currentUser.getTenantId(), currentUser.getUserId());

        // 进行插入或者修改操作
        RuleChainEntity resultEntity = checkNotNull(ruleChainRepository.saveRuleChain(ruleChainEntity));

        // 转换返回值
        RuleChainDTO result = RuleChainDTO.entityToDto(resultEntity);

        return RespDTO.success(result);
    }


    /*
        http://localhost:8083/api/v1/ruleChain/f3dfd360-0fdb-11ed-8010-1de00285059b?ruyuan_name=ruyuan_00

        {
            "code": 200,
            "requestId": "cf6976f51eeb4320951853fff331bf47",
            "message": "",
            "state": "",
            "data": {
                "ruleChainId": "f3dfd360-0fdb-11ed-8010-1de00285059b",
                "tenantId": "f20cc080-c89f-11ec-989e-8b76480d43cf",
                "userId": "f20cc080-c89f-11ec-989e-8b76480d43cf",
                "ruleChainName": "ry_rc_01",
                "firstRuleNodeId": null
            }
        }
     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChain/{ruleChainId}", method = RequestMethod.GET)
    public RespDTO findRuleChainById(@PathVariable("ruleChainId") String ruleChainIdStr){
        // 通过ID进行操作的三板斧
        checkParameter(ruleChainIdStr, ConsistContext.RULE_CHAIN_ID_EMPTY);
        RuleChainId ruleChainId = new RuleChainId(toUUID(ruleChainIdStr));
        IoTSecurityUser currentUser = getCurrentUser();
        // 业务处理
        RuleChainEntity resultEntity = checkNotNull(ruleChainRepository.findRuleChainById(currentUser.getTenantId(), ruleChainId));
        // 对象转换
        RuleChainDTO result = RuleChainDTO.entityToDto(resultEntity);
        return RespDTO.success(result);
    }

    /*
        http://localhost:8083/api/v1/ruleChain/f3dfd360-0fdb-11ed-8010-1de00285059b?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChain/{ruleChainId}", method = RequestMethod.DELETE)
    public RespDTO deleteRuleChainById(@PathVariable("ruleChainId") String ruleChainIdStr){
        // 通过ID进行操作的三板斧
        checkParameter(ruleChainIdStr, ConsistContext.RULE_CHAIN_ID_EMPTY);
        RuleChainId ruleChainId = new RuleChainId(toUUID(ruleChainIdStr));
        IoTSecurityUser currentUser = getCurrentUser();
        // 业务处理
        boolean success = ruleChainRepository.deleteRuleChainById(currentUser.getTenantId(), ruleChainId);

        return success ? RespDTO.success() : RespDTO.failture(500, ConsistContext.RESOURCE_NOT_EXISTS);
    }


    /*
        http://localhost:8083/api/v1/ruleChain/metadata?ruyuan_name=ruyuan_00


     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChain/metadata", method = RequestMethod.POST)
    public RespDTO saveRuleChainMetaData(@RequestBody RuleChainMetaDataDTO ruleChainMetaDataDTO){
        IoTSecurityUser currentUser = getCurrentUser();
        RuleChainMetaDataEntity metaDataEntity = ruleChainMetaDataFactory.emptyMetaDataEntity();

        RuleChainMetaDataDTO.dtoToEntity(ruleChainMetaDataDTO, metaDataEntity);

        RuleChainMetaDataEntity resultEntity = ruleChainDomainService.saveRuleChainMetaData(currentUser.getTenantId(), metaDataEntity);

        return RespDTO.success(RuleChainMetaDataDTO.entity2Dto(resultEntity));
    }

    /*
        http://localhost:8083/api/v1/ruleChain/metadata/07941380-0fdc-11ed-8010-1de00285059b?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChain/metadata/{ruleChainId}", method = RequestMethod.GET)
    public RespDTO queryRuleChainMetaData(@PathVariable("ruleChainId") String ruleChainIdStr){
        IoTSecurityUser currentUser = getCurrentUser();
        RuleChainId ruleChainId = new RuleChainId(toUUID(ruleChainIdStr));

        RuleChainMetaDataEntity metaDataEntity =
                ruleChainDomainService.loadRuleChainMetaData(currentUser.getTenantId(), ruleChainId);

        return RespDTO.success(RuleChainMetaDataDTO.entity2Dto(metaDataEntity));
    }

}


