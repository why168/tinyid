package com.xiaoju.uemc.tinyid.server.dao.entity;

import java.time.LocalDateTime;

/**
 * @author du_imba
 */
public class TinyIdToken {
    private Integer id;

    private String token;

    private String bizType;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TinyIdToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", bizType='" + bizType + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
