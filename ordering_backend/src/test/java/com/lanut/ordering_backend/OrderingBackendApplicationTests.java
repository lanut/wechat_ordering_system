package com.lanut.ordering_backend;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class OrderingBackendApplicationTests {
	@Resource
	DataSource dataSource;

	@Test
	void contextLoads() {

	}


	@Test
	// 使用代码生成器输出
	void generateDatabaseCode() {
		FastAutoGenerator
				.create(new DataSourceConfig.Builder(dataSource))
				.globalConfig(builder -> {
					builder.author("lanut");
					builder.commentDate("2024-06-04");
					builder.outputDir("src\\main\\java");
				})      			//打包设置，这里设置一下包名就行，注意跟我们项目包名设置为一致的
				.packageConfig(builder -> builder.parent("com.lanut.ordering_backend"))
				.strategyConfig(builder -> {
					//设置为所有Mapper添加@Mapper注解
					builder
							.mapperBuilder()
							.mapperAnnotation(Mapper.class)
							.build();
				})
				.execute();
	}



}
