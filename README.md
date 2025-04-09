<h1 style="text-align: center">HARVEY</h1>
一个基于JDK17、Spring Boot3、Mybatis-Plus、Spring Security6、JWT、Mysql、Redis、Knife4j、Vue3、Element-Plus等技术构建的前后端分离的企业级项目。
<p align="center">

</p>

### 项目特点
- 使用 JDK17、Spring Boot3 和 Vue3，以及 Element-Plus 等主流技术栈，实时更新。
- 结合 Spring Security 和 JWT 提供安全、无状态、分布式友好的身份验证和授权机制。
- 基于 RBAC 模型，实现细粒度的权限控制，涵盖接口方法和按钮级别。
- 使用 dynamic-datasource 对多数据源配置、管理、切换，轻松实现读写分离。
- 包括用户管理、角色管理、菜单管理、部门管理、字典管理等多个功能。
- 使用 Swagger 和 Knife4j 自动生成接口文档，支持在线调试，提高开发效率。

### 项目功能
- 组织架构：用户管理、部门管理、职位管理、角色管理、通知公告
- 系统管理：菜单管理、字典管理、系统配置、操作日志、错误日志、登陆日志
- 系统工具：文件管理、验证码管理、支付管理
- 服务监控：在线用户、服务器监控、Druid监控
- 文档中心：Swagger文档、Knife4j文档

### 项目目录
```
harvey-boot
├── harvey-ai
├── harvey-common
│  ├── constant    # 公共常量类
│  ├── enums       # 公共枚举类
│  ├── exception   # 异常类
│  ├── mapstruct
│  ├── model
│  ├── result
│  └── utils
├── harvey-core 
│  ├── config      
│  ├── mybatis
│  ├── redis
│  ├── storage
│  ├── thread
│  └── xxl
├── harvey-generator
├── harvey-mq
│  ├── harvey-rabbitmq
│  └── harvey-rocketmq
├── harvey-quartz
├── harvey-storage
│  └── harvey-storage-service
│  │  ├── mapper
│  │  ├── mapstruct
│  │  ├── model
│  │  └── service
├── harvey-system
│  ├── harvey-system-service
│  │  ├── mapper
│  │  ├── mapstruct
│  │  ├── model
│  │  └── service
│  └── harvey-system-rest
│  │  ├── aspect
│  │  ├── config
│  │  ├── controller
│  │  ├── handler
│  │  ├── result
│  │  ├── security
│  │  └── service
```

### 系统架构图

### 业务架构图

### 项目地址
| 简介                    | 地址                                          |
|-----------------------|---------------------------------------------|
| 后端[harvey-boot]       | https://gitee.com/harveyit/harvey-boot      |
| 前端[harvey-vue3-admin] | https://gitee.com/harveyit/harvey-vue3-admin |

### 使用框架
|         技术         | 说明                 | 官网                                             |
|:------------------:|--------------------|------------------------------------------------|
| Spring & SpringMVC | MVC架构模式的Web框架      | https://spring.io/projects/spring-framework    |
|    Spring Boot     | 简化 Spring 应用的配置和部署 | https://spring.io/projects/spring-boot         |
|  Spring Security   | 提供安全框架，支持身份验证和授权   | https://spring.io/projects/spring-security     |
|      Mybatis       | 持久层框架              | https://mybatis.org/mybatis-3/zh_CN/index.html |
|    Mybatis-Plus    | MyBatis 增强工具       | https://baomidou.com                           |
|       Mysql        | 关系型数据库             | https://www.mysql.com/                         |
|       Redis        | 非关系型数据库            | https://redis.io/                              |
|     Mapstruct      | 生成JavaBean映射类      | https://mapstruct.org                          |
|        Jwt         | Json Web令牌         | https://jwt.io/                                |
|       Druid        | 数据库连接池             | https://github.com/alibaba/druid/wiki          |
|       Lombok       | Java语言增强库          | https://projectlombok.org/                     |
|      Swagger       | 自动生成接口文档           | https://swagger.io/                            |
|      Xll-job       | 分布式任务调度            | https://www.xuxueli.com/xxl-job                |

### 项目启动


### 项目演示
首页
![首页](./preview/home.png)

用户管理
![用户管理](./preview/userManage.png)

菜单管理
![菜单管理](./preview/menuManage.png)

字典管理
![字典管理](./preview/dictManage.png)



### 鸣谢
