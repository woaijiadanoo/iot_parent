package com.ruyuan.jiangzh.iot.rule.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.RuleChainRepository;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.utils.ConsistContext;
import com.ruyuan.jiangzh.iot.rule.interfaces.dto.RuleChainDTO;
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
        // ????????????????????????
        IoTSecurityUser currentUser = getCurrentUser();
        PageDTO<RuleChainEntity> input = new PageDTO<>(nowPage, pageSize);
        if(!IoTStringUtils.isBlank(ruleChainName)){
            input.spellCondition(ConsistContext.RULE_CHAIN_QUERY_PARAM_NAME,  ruleChainName);
        }
        // ???????????????DataId
        input.spellCondition(ConsistContext.TENANT_ID, UUIDHelper.fromTimeUUID(currentUser.getTenantId().getUuid()));

        // ??????entity????????????
        PageDTO<RuleChainEntity> entityPageDTO = ruleChainRepository.ruleChains(input);

        // ???entity?????????dto
        List<RuleChainEntity> entities = entityPageDTO.getRecords();
        if(entities == null || entities.size() == 0){
            return RespDTO.success(entityPageDTO);
        }

        List<RuleChainDTO> dtos = entities.stream().map(e -> RuleChainDTO.entityToDto(e)).collect(Collectors.toList());

        // ??????????????????
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

        // dto ???entity
        RuleChainEntity ruleChainEntity =
                RuleChainDTO.dtoToEntity(dto, currentUser.getTenantId(), currentUser.getUserId());

        // ??????????????????????????????
        RuleChainEntity resultEntity = checkNotNull(ruleChainRepository.saveRuleChain(ruleChainEntity));

        // ???????????????
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
        // ??????ID????????????????????????
        checkParameter(ruleChainIdStr, ConsistContext.RULE_CHAIN_ID_EMPTY);
        RuleChainId ruleChainId = new RuleChainId(toUUID(ruleChainIdStr));
        IoTSecurityUser currentUser = getCurrentUser();
        // ????????????
        RuleChainEntity resultEntity = checkNotNull(ruleChainRepository.findRuleChainById(currentUser.getTenantId(), ruleChainId));
        // ????????????
        RuleChainDTO result = RuleChainDTO.entityToDto(resultEntity);
        return RespDTO.success(result);
    }

    /*
        http://localhost:8083/api/v1/ruleChain/f3dfd360-0fdb-11ed-8010-1de00285059b?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/ruleChain/{ruleChainId}", method = RequestMethod.DELETE)
    public RespDTO deleteRuleChainById(@PathVariable("ruleChainId") String ruleChainIdStr){
        // ??????ID????????????????????????
        checkParameter(ruleChainIdStr, ConsistContext.RULE_CHAIN_ID_EMPTY);
        RuleChainId ruleChainId = new RuleChainId(toUUID(ruleChainIdStr));
        IoTSecurityUser currentUser = getCurrentUser();
        // ????????????
        boolean success = ruleChainRepository.deleteRuleChainById(currentUser.getTenantId(), ruleChainId);

        return success ? RespDTO.success() : RespDTO.failture(500, ConsistContext.RESOURCE_NOT_EXISTS);
    }

}


