package com.crm.core.group.service;

import com.crm.core.group.entity.Groups;
import org.wah.doraemon.entity.consts.UsingState;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

public interface GroupsService{

    void save(Groups group);

    void update(Groups group);

    Groups getById(String id);

    Page<Groups> page(PageRequest pageRequest, String id, String name, UsingState state);

    void updateRelationByGroupId(String groupId, List<String> wechatIds);

    void updateRelationByWechatId(String wechatId, List<String> groupIds);
}