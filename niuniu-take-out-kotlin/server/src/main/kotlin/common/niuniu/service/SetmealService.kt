package common.niuniu.service

import common.niuniu.dto.SetmealDTO
import common.niuniu.dto.SetmealPageDTO
import common.niuniu.po.Setmeal
import common.niuniu.result.PageResult
import common.niuniu.vo.DishItemVO
import common.niuniu.vo.SetmealVO
import common.niuniu.vo.SetmealWithPicVO

interface SetmealService {
    fun addSetmeal(setmealDTO: SetmealDTO?)

    fun getPageList(setmealPageDTO: SetmealPageDTO?): PageResult?

    fun getSetmealById(id: Int?): SetmealVO?

    fun onOff(id: Int?)

    fun update(setmealDTO: SetmealDTO?)

    fun deleteBatch(ids: List<Int?>?)

    fun getList(categoryId: Int?): List<Setmeal>?

    fun getSetmealDishesById(id: Int?): List<DishItemVO>?

    fun getSetmealWithPic(id: Int?): SetmealWithPicVO?
}
