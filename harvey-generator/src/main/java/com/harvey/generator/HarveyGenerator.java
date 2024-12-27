package com.harvey.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.ibatis.type.JdbcType;

import java.sql.Types;
import java.util.Collections;

public class HarveyGenerator {

    private final static String OUTPUT_DIR = "G://Development//Workspace//harvey-boot//harvey-system//src//main//";

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/harvey", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("harvey") // 设置作者
                            .enableSpringdoc() // 开启 springdoc 模式
//							.enableSwagger() // 开启 swagger 模式
//							.fileOverride() // 覆盖已生成文件
                            .outputDir(OUTPUT_DIR + "java") // 指定输出目录
                            .disableOpenDir()
                    ;
                })
                .packageConfig(builder -> {
                    builder.parent("com.harvey") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .entity("model.entity") // 设置实体类
                            .serviceImpl("service") // 实现类直接替代接口
							.pathInfo(Collections.singletonMap(OutputFile.xml, OUTPUT_DIR + "resources//mapper")) // 设置mapperXml生成路径
                    ;
                })
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                                System.out.println("准备生成文件: " + tableInfo.getEntityName());
                                // 可以在这里添加自定义逻辑，如修改 objectMap 中的配置
                            })
                            // 自定义Query类
                            .customFile(builder1 ->
                                    builder1.packageName("model.query")
                                            .fileName("Query.java")
                                            .templatePath("/templates/query.java.ftl"))
                            // 自定义DTO类
                            .customFile(builder1 ->
                                    builder1.packageName("model.dto")
                                            .fileName("Dto.java")
                                            .templatePath("/templates/dto.java.ftl"))
                            // 自定义VO类
                            .customFile(builder1 ->
                                    builder1.packageName("model.vo")
                                            .fileName("VO.java")
                                            .templatePath("/templates/vo.java.ftl"))
                            // 自定义Converter接口
                            .customFile(builder1 ->
                                    builder1.packageName("mapstruct")
                                            .fileName("Converter.java")
                                            .templatePath("/templates/converter.java.ftl"))
                            .build()
                    ;
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_verify_code") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_", "sys_")   // 设置过滤表前缀
                            .build()
                            .entityBuilder()
                            .enableLombok() // 开启Lombok
                            .enableChainModel() // 开启链式模型
                            .javaTemplate("/templates/entity.java") // 设置实体类模板
                            .superClass("com.harvey.core.model.BaseEntity") // 设置实体类父类，含过滤属性时开启继承，否则关闭
                            .addSuperEntityColumns("id", "enabled", "remark", "sort", "create_time", "update_time", "deleted") // 过滤父类属性
//							.enableFileOverride() // 开启文件覆盖
                            .serviceBuilder()
                            .disableService() // 禁用service接口，直接使用实现类
//							.serviceTemplate("/templates/service.java") // 设置 Service 模板
                            .serviceImplTemplate("/templates/serviceImpl.java")// 设置 ServiceImpl 模板
                            .formatServiceImplFileName("%sService") // 格式化 Service 实现类文件名称
//							.enableFileOverride() //  开启文件覆盖
                            .controllerBuilder()
                            .template("/templates/controller.java") // 设置 Controller 模板
                            .enableRestStyle() // 启用Rest风格
//							.enableFileOverride() //  开启文件覆盖
                            .mapperBuilder()
                            .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class) // 开启 @Mapper 注解
                            .mapperTemplate("/templates/mapper.java") // 设置 Mapper 模板
                            .mapperXmlTemplate("/templates/mapper.xml") // 设置 Mapper XML模板
//							.enableFileOverride() //  开启文件覆盖
                    ;
                })
                .dataSourceConfig(builder -> {
                    builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                        if (Types.TINYINT == metaInfo.getJdbcType().TYPE_CODE) {
                            // 自定义类型转换
                            return DbColumnType.INTEGER;
                        }
                        return typeRegistry.getColumnType(metaInfo);
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
