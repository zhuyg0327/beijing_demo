package com.rent.Base.organ.dao;

import com.rent.Base.organ.entity.Organ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrganDao {
    List<Organ> findByParentId(String paramString);

    List<Organ> findAllDeptById(String paramString);

    List<Organ> findAllByparentId(String paramString);

    String queryUserOrg(String paramString);

    int queryTotal(Map<String, Object> paramMap);

    Organ queryObject(@Param("id") String id);

    void save(Map<String, Object> paramMap);

    void save(Organ paramT);

    int update(Organ paramT);

    List<Organ> queryList(Map<String, Object> paramMap);

    int deleteBatch(Object[] paramArrayOfObject);

    int delete(Object paramObject);
}
