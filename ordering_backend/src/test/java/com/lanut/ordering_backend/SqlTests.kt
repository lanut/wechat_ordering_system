package com.lanut.ordering_backend


import com.lanut.ordering_backend.mapper.CarouselMapper
import com.lanut.ordering_backend.mapper.CategoryMapper
import com.lanut.ordering_backend.mapper.DishMapper
import com.lanut.ordering_backend.mapper.FeedbackMapper
import com.lanut.ordering_backend.mapper.OrderMapper
import com.lanut.ordering_backend.mapper.UserMapper
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class SqlTests {

    @Resource
    lateinit var userMapper: UserMapper

    @Resource
    lateinit var categoryMapper: CategoryMapper

    @Resource
    lateinit var dishMapper: DishMapper

    @Resource
    lateinit var feedbackMapper: FeedbackMapper

    @Resource
    lateinit var orderMapper: OrderMapper

    @Resource
    lateinit var orderDeMapper: DishMapper

    @Resource
    lateinit var carouselMapper: CarouselMapper

    @Test
    fun testSelect() {
        val user = userMapper.selectById(1)
        println(user)
        println(user.lastLogin!!.toString())
    }

    // 测试所有Entity的Mapper
    @Test
    fun testSelectAll() {
        userMapper.selectList(null)
        categoryMapper.selectList(null)
        dishMapper.selectList(null)
        feedbackMapper.selectList(null)
        orderMapper.selectList(null)
        orderDeMapper.selectList(null)
        carouselMapper.selectList(null)
    }


/*
    @Test
    // 使用代码生成器输出
    fun generateDatabaseCode() {

        FastAutoGenerator.create(DataSourceConfig.Builder(datasource))
            // 全局配置
            .globalConfig { builder ->
                builder.author("lanut")
                builder.commentDate("2024-06-04")
                builder.outputDir("src\\main\\java")
                builder.enableKotlin()
            }
            //打包设置，这里设置一下包名就行，注意跟我们项目包名设置为一致的
            .packageConfig { builder ->
                builder.parent("com.lanut.ordering_backend")
            }
            // 策略配置
            .strategyConfig { builder ->
                //设置为所有Mapper添加@Mapper注解
                builder.mapperBuilder().mapperAnnotation(Mapper::class.java).build()
            }
            .execute()
    }
*/
}