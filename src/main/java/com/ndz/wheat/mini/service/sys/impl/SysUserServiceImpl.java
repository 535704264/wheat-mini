package com.ndz.wheat.mini.service.sys.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ndz.wheat.mini.common.page.PageData;
import com.ndz.wheat.mini.dao.sys.SysUserDao;
import com.ndz.wheat.mini.dto.sys.QuerySysUserDTO;
import com.ndz.wheat.mini.dto.sys.SysUserDTO;
import com.ndz.wheat.mini.entity.sys.SysRoleEntity;
import com.ndz.wheat.mini.entity.sys.SysUserEntity;
import com.ndz.wheat.mini.service.base.impl.BaseServiceImpl;
import com.ndz.wheat.mini.service.sys.SysMenuService;
import com.ndz.wheat.mini.service.sys.SysUserRoleService;
import com.ndz.wheat.mini.service.sys.SysUserService;
import com.ndz.wheat.mini.utils.AssertUtil;
import com.ndz.wheat.mini.utils.MD5Utils;
import com.ndz.wheat.mini.vo.sys.RouterVO;
import com.ndz.wheat.mini.vo.sys.SysUserVO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    SysUserRoleService sysUserRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SysUserDTO user) {
        SysUserEntity sysUserEntity = BeanUtil.copyProperties(user, SysUserEntity.class);
        String encryptPwd = MD5Utils.encrypt(user.getPassword());
        sysUserEntity.setPassword(encryptPwd);
        insert(sysUserEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateById(SysUserDTO user) {
        SysUserEntity sysUserEntity = selectById(user.getId());
        BeanUtil.copyProperties(user, sysUserEntity, "id");
        updateById(sysUserEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(Long id) {
        deleteById(id);
    }

    @Override
    public SysUserVO getById(Long id) {
        SysUserEntity sysUserEntity = selectById(id);
        return BeanUtil.copyProperties(sysUserEntity, SysUserVO.class);
    }

    @Override
    public PageData<SysUserVO>  page(Long page, Long limit, QuerySysUserDTO query) {
        Page<SysUserVO> pageParam = new Page<>(page, limit);
        IPage<SysUserVO> iPage = baseDao.page(pageParam, query);
        return  getPageData(iPage, SysUserVO.class);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUserEntity sysUser = selectById(id);
        sysUser.setStatus(status);
        updateById(sysUser);
    }

    @Override
    public SysUserEntity getByUsername(String userName) {
        AssertUtil.notNull(userName, "????????????????????????");
        return this.baseDao.selectOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getUsername,userName));
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     * @param username
     * @return
     */
    @Override
    public Map<String, Object> userInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        SysUserEntity sysUser = this.getByUsername(username);

        // ????????????????????????
        Map<String, Object> roles = sysUserRoleService.getRolesByUserId(sysUser.getId());
        List<SysRoleEntity> uoleList = MapUtil.get(roles, "allRoles", new TypeReference<>() {});
        List<String> roleCodes = null;
        if (CollUtil.isNotEmpty(uoleList)) {
             roleCodes = uoleList.stream().map(SysRoleEntity::getRoleCode).collect(Collectors.toList());
        }

        //????????????id?????????????????????
        List<RouterVO> routerVOList = sysMenuService.findUserMenuList(sysUser.getId());
        //????????????id????????????????????????
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

        result.put("name", sysUser.getName());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //???????????????????????????????????????????????????
//        result.put("roles",  "['admin']");
        result.put("roles",  roleCodes);
        result.put("buttons", permsList);
        result.put("routers", routerVOList);
        return result;
    }
}
