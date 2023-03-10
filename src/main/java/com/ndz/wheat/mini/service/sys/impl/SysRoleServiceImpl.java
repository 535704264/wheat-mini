package com.ndz.wheat.mini.service.sys.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ndz.wheat.mini.dao.sys.SysUserRoleDao;
import com.ndz.wheat.mini.dto.sys.AssginRoleDTO;
import com.ndz.wheat.mini.dto.sys.SaveSysRoleDTO;
import com.ndz.wheat.mini.entity.sys.SysUserRoleEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ndz.wheat.mini.common.constant.MybatisConstant;
import com.ndz.wheat.mini.common.page.PageData;
import com.ndz.wheat.mini.dao.sys.SysRoleDao;
import com.ndz.wheat.mini.entity.sys.SysRoleEntity;
import com.ndz.wheat.mini.service.base.impl.BaseServiceImpl;
import com.ndz.wheat.mini.service.sys.SysUserRoleService;
import com.ndz.wheat.mini.vo.sys.SysRoleVO;

import cn.hutool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRoleEntity> implements SysUserRoleService {

    @Resource
    SysUserRoleDao userRoleDao;

    @Override
    public PageData<SysRoleVO> page(Map<String, Object> params) {
        LambdaQueryWrapper<SysRoleEntity> searchQuery = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty((String)params.get("roleName"))) {
            searchQuery.like(SysRoleEntity::getRoleName, params.get("roleName"));
        }
        IPage<SysRoleEntity> page = baseDao.selectPage(
                getPage(params, MybatisConstant.CREATE_TIME,false), searchQuery
        );
        return getPageData(page, SysRoleVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save( SaveSysRoleDTO dto) {
        SysRoleEntity entity =  BeanUtil.copyProperties(dto, SysRoleEntity.class);
        insert(entity);
    }

    @Override
    public void remove(String id) {
        deleteById(id);
    }

    @Override
    public SysRoleVO info(String id) {
        SysRoleEntity entity = selectById(id);
        return BeanUtil.copyProperties(entity, SysRoleVO.class);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SaveSysRoleDTO dto) {
        SysRoleEntity entity = selectById(dto.getId());
        BeanUtil.copyProperties(dto, entity, "id");
        updateById(entity);
    }

    @Override
    public void batchRemove(Long[] idList) {
        deleteBatchIds(ListUtil.toList(idList));
    }

    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        //??????????????????
        List<SysRoleEntity> roles = this.baseDao.selectList(null);
        //????????????id??????
        LambdaQueryWrapper<SysUserRoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRoleEntity::getUserId,userId);
        //??????????????????????????????
        List<SysUserRoleEntity> userRoles = userRoleDao.selectList(queryWrapper);
        //??????????????????????????????id
        List<Long> userRoleIds = new ArrayList<>();
        for (SysUserRoleEntity userRole : userRoles) {
            userRoleIds.add(userRole.getRoleId());
        }
        //???????????????Map
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",roles);
        returnMap.put("userRoleIds",userRoleIds);
        return returnMap;
    }

    @Override
    public void doAssign(AssginRoleDTO dto) {
        //????????????id???????????????????????????
        LambdaQueryWrapper<SysUserRoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRoleEntity::getUserId, dto.getUserId());
        userRoleDao.delete(queryWrapper);
        //?????????????????????id
        List<Long> roleIdList = dto.getRoleIdList();
        for (Long roleId : roleIdList) {
            if(roleId != null){
                SysUserRoleEntity sysUserRole = new SysUserRoleEntity();
                sysUserRole.setUserId(dto.getUserId());
                sysUserRole.setRoleId(roleId);
                //??????
                userRoleDao.insert(sysUserRole);
            }
        }
    }
}
