package common.niuniu.mapper

import com.github.pagehelper.Page
import common.niuniu.annotation.AutoFill
import common.niuniu.dto.DishPageDTO
import common.niuniu.enumeration.OperationType
import common.niuniu.po.Dish
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface DishMapper {
    @AutoFill(OperationType.INSERT)
    fun addDish(dish: Dish?)

    fun getPageList(dishPageDTO: DishPageDTO?): Page<Dish?>?

    @Select("select * from dish where id = #{id}")
    fun getById(id: Int?): Dish?

    @AutoFill(OperationType.UPDATE)
    fun update(dish: Dish?)

    fun deleteBatch(ids: List<Int?>?)

    @Update("update dish set status = IF(status = 0, 1, 0) where id = #{id}")
    fun onOff(id: Int?)

    fun getList(dish: Dish?): List<Dish?>?

    @Select("select count(id) from dish where status = #{i}")
    fun getByStatus(i: Int): Int?
}
