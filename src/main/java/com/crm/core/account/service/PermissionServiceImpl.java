package com.crm.core.account.service;

import com.crm.commons.consts.Constants;
import com.crm.commons.utils.ScanUtils;
import com.crm.core.account.dao.PermissionDao;
import com.crm.core.account.dao.RoleDao;
import com.crm.core.account.entity.Permission;
import com.crm.core.account.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wah.doraemon.utils.ObjectUtils;

import java.lang.annotation.Annotation;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService{

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional(readOnly = false)
    public void save(Permission permission){
        Assert.notNull(permission, "权限信息不能为空");
        Assert.hasText(permission.getUrl(), "权限路径不能为空");

        permissionDao.saveOrUpdate(permission);
    }

    @Override
    @Transactional(readOnly = false)
    public void synchronize(){
        List<Permission> insert = new ArrayList<Permission>();
        List<Permission> update = new ArrayList<Permission>();

        //原有数据
        List<Permission> original = permissionDao.find(null, null, null);

        //现有数据
        List<Permission> permissions = ScanUtils.findPermission(Constants.PROJECT_ROOT, Constants.API_ANNOTATIONS);

        for(Permission permission : permissions){
            if(original.contains(permission)){
                permission.setId(original.get(original.indexOf(permission)).getId());
                update.add(permission);
            }else{
                insert.add(permission);
            }
        }

        if(!insert.isEmpty()){
            permissionDao.saveList(insert);
        }
        if(!update.isEmpty()){
            permissionDao.updateList(update);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void updateRelationByAccountId(String accountId, List<String> permissionIds){
        Assert.hasText(accountId, "账户ID不能为空");

        permissionDao.updateRelationByAccountId(accountId, permissionIds);
    }

    @Override
    public Set<Permission> findByAccountId(String accountId){
        Assert.hasText(accountId, "账户ID不能为空");

        Set<Permission> permissions = new HashSet<Permission>();

        //根据账户ID查询
        permissions.addAll(permissionDao.findByAccountId(accountId));

        //根据角色ID查询
        List<Role> roles = roleDao.findByAccountId(accountId);
        if(roles != null && !roles.isEmpty()){
            permissions.addAll(permissionDao.findByRoleIds(ObjectUtils.ids(roles)));
        }

        return permissions;
    }

    @Override
    public List<Permission> find(String url, RequestMethod method, Boolean needAllot){
        return permissionDao.find(url, method, needAllot);
    }
}
