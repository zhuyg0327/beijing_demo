package com.rent.Base.organ.entity;

import java.io.Serializable;

public class Organ implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String treePath;

    private Integer isdelete;

    private String name;

    private Long timestamp;

    private Integer sort;

    private Integer type;

    private String code;

    private String parentId;

    private Integer deptOfficer;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    public String getTreePath() {
        return this.treePath;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public Integer getIsdelete() {
        return this.isdelete;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public Integer getDeptOfficer() {
        return this.deptOfficer;
    }

    public void setDeptOfficer(Integer deptOfficer) {
        this.deptOfficer = deptOfficer;
    }
}
