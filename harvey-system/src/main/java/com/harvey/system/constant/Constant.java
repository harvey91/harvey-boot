package com.harvey.system.constant;

/**
 * 通用常量类
 * @author Harvey
 * @date 2024-11-08 14:58
 **/
public class Constant {

    /** 超级管理员角色ROOT */
    public static final String ROOT = "ROOT";

    /** 默认密码 */
    public static final String DEFAULT_PASSWORD = "123456";

    /** www主域 */
    public static final String WWW = "www.";

    /** http请求 */
    public static final String HTTP = "http://";

    /** https请求 */
    public static final String HTTPS = "https://";

    public static final long MILLIS_SECOND = 1000;

    public static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    public static final long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /** 在线状态 */
    public static final int ONLINE_STATUS = 1;

    /** 离线状态 */
    public static final int OFFLINE_STATUS = 0;
}
