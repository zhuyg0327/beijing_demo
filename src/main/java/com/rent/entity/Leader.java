package com.rent.entity;

public class Leader {
    private String id;
    private String leaderId;
    private String leaderName;
    private String sort;
    private Integer type;

    @Override
    public String toString() {
        return "Leader{" +
                "id='" + id + '\'' +
                ", leaderId='" + leaderId + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", sort='" + sort + '\'' +
                ", type=" + type +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
