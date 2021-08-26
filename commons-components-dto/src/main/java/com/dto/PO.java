package com.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description：db公用字段
 * @author:  caijie
 * @History: 变更记录
 * <ModifiedBy>    <ModifiedDate>     <ModifiedVersion>
 * Administrator     2021-08-24          1.0
 * @ModifiedRemark:
 * @Copyright (C):
 */
public class PO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 是否删除 1-是 0-否
     */
    private Boolean deleted;
    /**
     * 创建用户
     */
    @TableField("created_user")
    private String createdUser;
    /**
     * 更新用户
     */
    @TableField("updated_user")
    private String updatedUser;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "PO{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", createdUser='" + createdUser + '\'' +
                ", updatedUser='" + updatedUser + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
