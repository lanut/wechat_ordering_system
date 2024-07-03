package common.niuniu.mapper

import common.niuniu.po.Cart
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Update


@Mapper
interface CartMapper {
    /**
     * 查找有没有和cart菜品/套餐相同的商品在当前购物车里
     *
     * @param cart
     * @return
     */
    fun list(cart: Cart?): List<Cart>

    /**
     * 数量更新+1
     *
     * @param cart
     */
    @Update("update cart set number = #{number} where id = #{id}")
    fun updateNumberById(cart: Cart?)

    /**
     * 插入购物车数据
     *
     * @param shoppingCart
     */
    @Insert(
        "insert into cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, pic, create_time) " +
                " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{pic},#{createTime})"
    )
    fun insert(shoppingCart: Cart?)

    /**
     * 清空购物车
     *
     * @param currentId
     */
    @Delete("delete from cart where user_id = #{currentId}")
    fun delete(currentId: Int?)

    fun deleteByDishId(dishId: Int?, dishFlavor: String?)

    @Delete("delete from cart where setmeal_id = #{setmealId} ")
    fun deleteBySetmealId(setmealId: Int?)

    fun insertBatch(cartList: List<Cart?>)
}
