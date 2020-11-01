package com.rent.Base.organ.service.impl;

import com.rent.Base.organ.dao.OrganDao;
import com.rent.Base.organ.entity.Organ;
import com.rent.Base.organ.service.OrganService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganServiceImpl implements OrganService {
    @Autowired
    OrganDao organDao;

    @Override
    public int queryTotal(Map<String, Object> map) {
        return this.organDao.queryTotal(map);
    }

    @Override
    public Organ queryObject(String id) {
        return this.organDao.queryObject(id);
    }

    @Override
    public List<Organ> queryList(Map<String, Object> map) {
        return this.organDao.queryList(map);
    }

    @Override
    public void save(Organ baseAppOrgan) {
        this.organDao.save(baseAppOrgan);
    }

    @Override
    public void update(Organ baseAppOrgan) {
        this.organDao.update(baseAppOrgan);
    }

    @Override
    public void delete(String id) {
        this.organDao.delete(id);
    }

    @Override
    public void deleteBatch(String[] ids) {
        this.organDao.deleteBatch((Object[]) ids);
    }

    @Override
    public List<Organ> findByParentId(String parentId) {
        return this.organDao.findByParentId(parentId);
    }

    @Override
    public List<Organ> findAllDeptById(String deptId) {
        return this.organDao.findAllDeptById(deptId);
    }

    //    public void clearOrgan() {
//        this.organDao.clearOrgan();
//    }
    @Override
    public List<Organ> findAllByparentId(String id) {
        return organDao.findAllByparentId(id);
    }

    @Override
    public Organ getSecondary(String orgId) {
        Organ organ = organDao.queryObject(orgId);
        if (organ != null && (organ.getTreePath().split(",")).length > 1)
            return organDao.queryObject(organ.getTreePath().split(",")[1]);
        return null;
    }

    @Override
    public String getAllDeptName(String deptId) {
        String deptName = "";
        Organ organ = organDao.queryObject(deptId);
        if (organ == null || StringUtils.isBlank(organ.getTreePath()))
            return deptName;
        String path = organ.getTreePath();
        if (StringUtils.isNotBlank(path) && (path.split(",")).length > 2)
            for (int i = 2; i < (path.split(",")).length; i++) {
                organ = organDao.queryObject(path.split(",")[i]);
                if (organ != null && StringUtils.isNotBlank(organ.getName()))
                    deptName = deptName + organ.getName();
            }
        return deptName;
    }

    @Override
    public String queryUserOrg(String userId) {
        return organDao.queryUserOrg(userId);
    }
}
