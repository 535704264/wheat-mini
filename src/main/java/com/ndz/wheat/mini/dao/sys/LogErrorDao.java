package com.ndz.wheat.mini.dao.sys;

import com.ndz.wheat.mini.dao.base.BaseDao;
import com.ndz.wheat.mini.entity.base.SysErrorLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogErrorDao extends BaseDao<SysErrorLogEntity> {
}
