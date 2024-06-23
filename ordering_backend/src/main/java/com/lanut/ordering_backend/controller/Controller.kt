package com.lanut.ordering_backend.controller

import com.lanut.ordering_backend.context.BaseContext
import com.lanut.ordering_backend.entity.vo.RestBean
import com.lanut.ordering_backend.entity.vo.RestSuccess
import com.lanut.ordering_backend.service.IDishService
import com.lanut.ordering_backend.service.IUserService
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/")
class UserController {

    @Resource
    lateinit var userService: IUserService

    @PostMapping("/profile")
    fun getUserProfile(
//        openid: String
    ): String {
        // 先处理Token user类
        val openid = BaseContext.currentOpenid
        val user = userService.getUserByOpenid(openid)
        // 返回user
        return user?.RestSuccess() ?: RestBean.failure(404).asJsonString()
    }
}


@RestController
@RequestMapping("/api/common/dish")
class DishController {


    @Autowired
    lateinit var dishService: IDishService

    // 获得所有的饭菜带着分类
    @GetMapping("/dishes")
    fun getDishes(): String {
        // TODO: 获得所有的饭菜表单
        val allDishes = dishService.getAllDishes()
        // TODO: 导出为Json格式
        return allDishes.RestSuccess()
    }

    // 搜索匹配到的饭菜
}