package com.crm.core.permission.dao;

import com.crm.core.permission.dao.mapper.PermissionMapper;
import com.crm.core.permission.entity.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.wah.doraemon.security.exception.DataAccessException;
import org.wah.doraemon.utils.IDGenerator;

import java.util.Date;
import java.util.List;

@Repository
public class PermissionDao{

    private Logger logger = LoggerFactory.getLogger(PermissionDao.class);

    @Autowired
    private PermissionMapper mapper;

    public void save(Permission permission){
        try{
            Assert.notNull(permission, "权限信息不能为空");
            Assert.hasText(permission.getResourceId(), "资源ID不能为空");
            Assert.notNull(permission.getType(), "权限类型不能为空");

            permission.setId(IDGenerator.uuid32());
            permission.setCreateTime(new Date());
            mapper.save(permission);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void saveList(List<Permission> permissions){
        try{
            Assert.notEmpty(permissions, "权限列表不能为空");

            final Date now = new Date();
            for(Permission permission : permissions){
                Assert.notNull(permission, "权限信息不能为空");
                Assert.hasText(permission.getResourceId(), "资源ID不能为空");
                Assert.notNull(permission.getType(), "权限类型不能为空");

                permission.setId(IDGenerator.uuid32());
                permission.setCreateTime(new Date());
            }

            mapper.saveList(permissions);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}