package com.harvey.web.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Harvey
 * @date 2024-10-24 21:20
 **/
@Data
public class SysUser {
    public Long id;

    public String username;

    public String password;

    public String nickname;

    public int enabled;

    public Date createTime;

    public Date updateTime;

    public int deleted;
}
