package common.niuniu.mapper

import common.niuniu.po.DishFlavor
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface DishFlavorMapper {
    fun insertBatch(flavorList: List<DishFlavor?>?)

    @Select("select * from dish_flavor where dish_id = #{id}")
    fun getByDishId(id: Int?): List<DishFlavor?>?

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    fun deleteByDishId(dishId: Int?)

    fun deleteBatch(ids: List<Int?>?)
}
