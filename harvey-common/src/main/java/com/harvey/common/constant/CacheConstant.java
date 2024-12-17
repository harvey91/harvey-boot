package com.harvey.common.constant;

/**
 * 缓存key 常量
 * @author Harvey
 * @date 2024-11-08 15:36
 **/
public class CacheConstant {

    /** 登录用户token key */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /** 登录验证码 key */
    public static final String LOGIN_CAPTCHA_KEY = "login_captcha:";

    /** 手机邮箱等联系方式验证码 key */
    public static final String CONTACT_VERIFY_KEY = "contact_verify_key:";

    /** 系统配置缓存 */
    public static final String SYS_CONFIG_KEY = "sys_config_key";

    /* ------------------------方法缓存------------------------ */

    /** 菜单路由缓存 */
    public static final String MENU_KEY = "menu";

    /** 字典缓存 */
    public static final String DICT_KEY = "dict";
}
