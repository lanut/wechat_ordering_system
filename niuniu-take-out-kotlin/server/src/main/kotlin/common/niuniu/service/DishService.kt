package common.niuniu.service

import common.niuniu.dto.DishDTO
import common.niuniu.dto.DishPageDTO
import common.niuniu.po.Dish
import common.niuniu.result.PageResult
import common.niuniu.vo.DishVO

interface DishService {
    fun addDishWithFlavor(dishDTO: DishDTO?)

    fun getPageList(dishPageDTO: DishPageDTO?): PageResult?

    fun getDishWithFlavorById(id: Int?): DishVO?

    fun updateDishWithFlavor(dishDTO: DishDTO?)

    fun deleteBatch(ids: List<Int?>?)

    fun onOff(id: Int?)

    fun getDishesWithFlavorById(dish: Dish?): List<DishVO>
}
