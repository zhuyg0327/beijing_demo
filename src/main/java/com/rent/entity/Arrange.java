package com.rent.entity;

public class Arrange {
    private String id;
    private String dutyDate;
    private String userId;
    private String userName;
    private String leaderId;
    private String leaderName;
    private String week;
    private String updateTime;

    @Override
    public String toString() {
        return "Arrange{" +
                "id='" + id + '\'' +
                ", dutyDate='" + dutyDate + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", leaderId='" + leaderId + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", week='" + week + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
