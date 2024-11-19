package com.harvey.system.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Harvey
 * @date 2024-11-07 17:26
 **/
@Data
public class UserVO {

    public Long id;

    public String username;

    public String nickname;

    public String avatar;

    public Integer gender;

    public String phone;

    public String email;

    public Integer enabled;

    public String deptName;

    public String roleName;

    public LocalDateTime createTime;
}
