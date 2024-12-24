package com.harvey.system.model.dto;

import lombok.Data;

/**
 * @author Harvey
 * @date 2024-12-11 18:46
 **/
@Data
public class ProfileDto {

    private Long id;

    private String nickname;

    private Integer gender;

    private String avatar;
}
