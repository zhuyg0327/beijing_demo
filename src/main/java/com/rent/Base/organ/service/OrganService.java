package com.rent.Base.organ.service;

import com.rent.Base.organ.entity.Organ;

import java.util.List;
import java.util.Map;

public interface OrganService {
    int queryTotal(Map<String, Object> paramMap);

    Organ queryObject(String id);

    List<Organ> queryList(Map<String, Object> paramMap);

    void save(Organ paramBaseAppOrgan);

    void update(Organ paramBaseAppOrgan);

    void delete(String paramString);

    void deleteBatch(String[] paramArrayOfString);

    List<Organ> findByParentId(String paramString);

    List<Organ> findAllDeptById(String paramString);

    List<Organ> findAllByparentId(String paramString);

//    void clearOrgan();

    Organ getSecondary(String paramString);

    String getAllDeptName(String paramString);

    String queryUserOrg(String paramString);
}
