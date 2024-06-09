package com.lanut.ordering_backend



import com.lanut.ordering_backend.mapper.UserMapper
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class SqlTests {


//    @Resource
//    lateinit var datasource: DataSource
//
//    @Value("\${spring.datasource.url}")
//    val url: String = ""
//
//    @Value("\${spring.datasource.username}")
//    val username: String = ""
//
//    @Value("\${spring.datasource.password}")
//    val password: String = ""
//
//    @Value("\${spring.datasource.driver-class-name}")
//    val driverClassName: String = ""
//    @Test
//    fun testResource() {
//        println(url)
//        println(username)
//        println(password)
//        println(driverClassName)
//    }


    @Resource
    lateinit var userMapper: UserMapper

    @Test
    fun testSelect() {
        val user = userMapper.selectById(1)
        println(user)
        println(user.lastLogin!!.toString())
    }


//    @Test
//    // 使用代码生成器输出
//    fun generateDatabaseCode() {
//
//        FastAutoGenerator.create(DataSourceConfig.Builder(datasource))
//            // 全局配置
//            .globalConfig { builder ->
//                builder.author("lanut")
//                builder.commentDate("2024-06-04")
//                builder.outputDir("src\\main\\java")
//                builder.enableKotlin()
//            }
//            //打包设置，这里设置一下包名就行，注意跟我们项目包名设置为一致的
//            .packageConfig { builder ->
//                builder.parent("com.lanut.ordering_backend")
//            }
//            // 策略配置
//            .strategyConfig { builder ->
//                //设置为所有Mapper添加@Mapper注解
//                builder.mapperBuilder().mapperAnnotation(Mapper::class.java).build()
//            }
//            .execute()
//    }
}