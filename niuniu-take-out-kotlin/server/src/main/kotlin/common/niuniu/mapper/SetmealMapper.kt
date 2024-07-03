package common.niuniu.mapper

import com.github.pagehelper.Page
import common.niuniu.annotation.AutoFill
import common.niuniu.dto.SetmealPageDTO
import common.niuniu.enumeration.OperationType
import common.niuniu.po.Setmeal
import common.niuniu.vo.DishItemVO
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface SetmealMapper {
    @AutoFill(OperationType.INSERT)
    fun addSetmeal(setmeal: Setmeal?)

    fun getPageList(setmealPageDTO: SetmealPageDTO?): Page<Setmeal?>?

    @Select("select * from setmeal where id = #{id}")
    fun getSetmealById(id: Int?): Setmeal?

    @Update("update setmeal set status = IF(status = 1, 0, 1) where id = #{id}")
    fun onOff(id: Int?)

    @AutoFill(OperationType.UPDATE)
    fun update(setmeal: Setmeal?)

    fun deleteBatch(ids: List<Int?>?)

    fun getList(setmeal: Setmeal?): List<Setmeal>?

    @Select(
        "select s.name, s.pic, s.detail, sd.copies from " +
                "setmeal s left join setmeal_dish sd on s.id = sd.dish_id " +
                "where sd.setmeal_id = #{id}"
    )
    fun getSetmealDishesById(id: Int?): List<DishItemVO>?

    @Select("select count(id) from setmeal where status = #{i}")
    fun getByStatus(i: Int): Int?
}
