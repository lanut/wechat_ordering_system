package common.niuniu.mapper

import common.niuniu.po.SetmealDish
import common.niuniu.po.SetmealDishWithPic
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface SetmealDishMapper {
    fun insertBatch(setmealDishes: List<SetmealDish?>?)

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    fun getDishesBySetmealId(id: Int?): List<SetmealDish?>?

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    fun deleteBySetmealId(setmealId: Int?)

    fun deleteBatch(ids: List<Int?>?)

    @Select("select sd.*, d.pic from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id}")
    fun getDishesWithPic(id: Int?): List<SetmealDishWithPic?>?

    fun getSetmealIdsByDishIds(ids: List<Int?>?): List<Int?>?
}
