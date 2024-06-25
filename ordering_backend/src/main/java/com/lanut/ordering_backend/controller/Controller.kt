package com.lanut.ordering_backend.controller

import com.lanut.ordering_backend.annotation.Slf4j
import com.lanut.ordering_backend.annotation.Slf4j.Companion.log
import com.lanut.ordering_backend.context.BaseContext
import com.lanut.ordering_backend.entity.vo.OrderVO
import com.lanut.ordering_backend.entity.vo.RestBean
import com.lanut.ordering_backend.entity.vo.RestSuccess
import com.lanut.ordering_backend.mapper.OrderMapper
import com.lanut.ordering_backend.service.ICategoryService
import com.lanut.ordering_backend.service.IDishService
import com.lanut.ordering_backend.service.IOrderService
import com.lanut.ordering_backend.service.IUserService
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/")
class UserController {

    @Resource
    lateinit var userService: IUserService

    // 获取个人信息
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
@RequestMapping("/api/dish")
class DishController {

    @Autowired
    lateinit var dishService: IDishService

    // 获得所有的菜品
    @GetMapping("/dishes")
    fun getDishes(): String {
        val allDishes = dishService.getAllDishes()
        return allDishes.RestSuccess()
    }

    // 搜索匹配到的饭菜
}

@RestController
@RequestMapping("/api/category")
class CategoryController {
    @Autowired
    lateinit var categoryService: ICategoryService

    @GetMapping("/categories")
    fun getCategories(): String {
        return categoryService.getAllCategories().RestSuccess()
    }

    // 获取某类别下的菜品
    // **接口路径**：`GET /api/category/{categoryId}/dishes`
    @GetMapping("/{categoryId}/dishes")
    fun getDishesByCategory(
        @PathVariable categoryId: Int
    ): String {
        return categoryService.getDishesByCategory(categoryId).RestSuccess()
    }
}

@Slf4j
@RestController
@RequestMapping("/api/admin")
class AdminController {

    @Autowired
    lateinit var dishService: IDishService


    // 管理员添加菜品
    // **接口路径**：`PUT /api/admin/dish`
    @PutMapping("/dish")
    fun addDish(
        @RequestBody dishMap: Map<String, String>
    ): String {
        // 从请求中获取菜品信息
        log.info("addDish: $dishMap")
        // 调用service层添加菜品
        val dish = dishService.addDish(dishMap)
        log.info("addDish: $dish")
        return if (dish != null) {
            RestBean.success(object {
                val message = "Dish created successfully"
                val dish_id = dish.dishId
            }).asJsonString()
        } else {
            RestBean.failure(400, "添加失败").asJsonString()
        }
    }
}


@RestController
@RequestMapping("/api/orders")
class OrderController {

    @Resource
    lateinit var orderService: IOrderService


    // 用户下单
    // **接口路径**：`POST /api/order`
    @PostMapping
    fun placeOrder(
        @RequestBody olderVO: OrderVO
    ): String {
        log.info(BaseContext.currentOpenid)
        // 从请求中获取订单信息
        log.info("placeOrder: $olderVO")
        // 调用service层添加订单
        // TODO: 添加订单

        return RestBean.success(object {
            val message = "Order created successfully"
        }).asJsonString()
    }

    // 获取个人所有订单
    @GetMapping("/orders")
    fun getUserOrders(): String {
        // 先处理Token user类
        val openid = BaseContext.currentOpenid
        val orders = orderService.getUserOrders(openid)
        // 返回订单
        return orders.RestSuccess()
    }
}

