package com.ndz.wheat.mini.app.sys;
import com.ndz.wheat.mini.common.annotation.Log;

import java.util.Map;

import com.ndz.wheat.mini.common.enums.BusinessType;
import com.ndz.wheat.mini.dto.sys.AssginRoleDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson2.JSON;
import com.ndz.wheat.mini.common.bean.ApiResult;
import com.ndz.wheat.mini.common.page.PageData;
import com.ndz.wheat.mini.dto.sys.SaveSysRoleDTO;
import com.ndz.wheat.mini.service.sys.SysUserRoleService;
import com.ndz.wheat.mini.utils.ApiResultUtils;
import com.ndz.wheat.mini.vo.sys.SysRoleVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "系统角色")
@Slf4j
@RestController
@RequestMapping("admin/system/sysRole")
public class SysRoleController {

    @Autowired
    SysUserRoleService sysUserRoleService;


    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping("/page")
    public ApiResult<PageData<SysRoleVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        log.info("角色分页请求Req: {}", JSON.toJSONString(params));
        return ApiResultUtils.ok(sysUserRoleService.page(params));
    }

    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('role:add')")
    @PostMapping("/save")
    public ApiResult<Object> save(@RequestBody SaveSysRoleDTO dto) {
        sysUserRoleService.save(dto);
        return ApiResultUtils.ok();
    }

    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('role:remove')")
    @DeleteMapping(value = "/remove/{id}")
    public ApiResult<Object> remove(@PathVariable String id) {
        sysUserRoleService.remove(id);
        return ApiResultUtils.ok();
    }

    @DeleteMapping(value = "/batchRemove")
    public ApiResult<Object> batchRemove(@RequestBody Long[] idList) {
        sysUserRoleService.batchRemove(idList);
        return ApiResultUtils.ok();
    }

    @GetMapping("/info/{id}")
    public ApiResult<SysRoleVO> info(@PathVariable String id) {
        return ApiResultUtils.ok(sysUserRoleService.info(id));
    }

    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/update")
    public ApiResult<Object> update(@RequestBody SaveSysRoleDTO dto) {
        sysUserRoleService.update(dto);
        return ApiResultUtils.ok();
    }


    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public ApiResult<Object> toAssign(@PathVariable Long userId) {
        Map<String, Object> roleMap = sysUserRoleService.getRolesByUserId(userId);
        return ApiResultUtils.ok(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public ApiResult<Object> doAssign(@RequestBody AssginRoleDTO dto) {
        sysUserRoleService.doAssign(dto);
        return ApiResultUtils.ok();
    }





}
