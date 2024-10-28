package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-10-24 21:20
 **/
@Data
@TableName(value = "sys_user", autoResultMap = true)
public class SysUser extends BaseEntity {

    public Long id;

    public String username;

    public String password;

    public String nickname;

    public Integer enabled;
}
