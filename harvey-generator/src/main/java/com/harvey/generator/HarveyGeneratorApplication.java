package com.harvey.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

public class HarveyGeneratorApplication {

	private final static String OUTPUT_DIR = "G://Development//Workspace//Harvey-Base//harvey-system//src//main//";

	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/harvey", "root", "123456")
				.globalConfig(builder -> {
					builder.author("harvey") // 设置作者
							.enableSwagger() // 开启 swagger 模式
							.outputDir(OUTPUT_DIR+"java"); // 指定输出目录
				})
				.packageConfig(builder -> {
					builder.parent("com.harvey") // 设置父包名
							.moduleName("system") // 设置父包模块名
//							.service("")
							.pathInfo(Collections.singletonMap(OutputFile.xml, OUTPUT_DIR+"resources//mapper")); // 设置mapperXml生成路径
				})
				.strategyConfig(builder -> {
					builder.addInclude("sys_role") // 设置需要生成的表名
							.addTablePrefix("t_", "c_"); // 设置过滤表前缀
				})
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}

}
