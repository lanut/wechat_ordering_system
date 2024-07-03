package common.niuniu.service.serviceImpl

import common.niuniu.context.BaseContext.currentId
import common.niuniu.dto.CartDTO
import common.niuniu.mapper.CartMapper
import common.niuniu.mapper.DishMapper
import common.niuniu.mapper.SetmealMapper
import common.niuniu.po.Cart
import common.niuniu.service.CartService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CartServiceImpl : CartService {
    @Autowired
    private lateinit var cartMapper: CartMapper

    @Autowired
    private lateinit var dishMapper: DishMapper

    @Autowired
    private lateinit var setmealMapper: SetmealMapper

    /**
     * 添加进购物车
     *
     * @param cartDTO
     */
    override fun add(cartDTO: CartDTO?) {
        // cart表示当前用户要加入购物车的一条数据
        var cart = Cart()
        BeanUtils.copyProperties(cartDTO!!, cart)
        cart.userId = currentId
        // 查询该用户自己购物车的所有菜品和套餐，看看有没有和要加入的cart一样的？
        val cartList = cartMapper.list(cart)
        // 1、存在，无需再insert，直接数量+1就行
        if (cartList != null && cartList.size == 1) {
            cart = cartList[0]
            cart.number = cart.number + 1
            cartMapper.updateNumberById(cart)
        } else {
            // 判断当前添加到购物车的是菜品还是套餐
            val dishId = cartDTO.dishId
            if (dishId != null) {
                // 添加到购物车的是菜品
                val dish = dishMapper.getById(dishId)
                cart.name = dish!!.name
                cart.pic = dish.pic
                cart.amount = dish.price
            } else {
                // 添加到购物车的是套餐
                val setmeal = setmealMapper.getSetmealById(cartDTO.setmealId)
                cart.name = setmeal!!.name
                cart.pic = setmeal.pic
                cart.amount = setmeal.price
            }
            cart.number = 1
            cart.createTime = LocalDateTime.now()
            cartMapper.insert(cart)
        }
    }

    /**
     * 在购物车中的对应菜品/套餐数量减一
     *
     * @param cartDTO
     */
    override fun sub(cartDTO: CartDTO?) {
        // cart表示当前用户要减少购物车菜品/套餐数量的一条数据
        var cart = Cart()
        BeanUtils.copyProperties(cartDTO!!, cart)
        cart.userId = currentId
        // 查询该菜品/套餐详细信息（必有，而且只有一条）
        val cartList = cartMapper.list(cart)
        cart = cartList[0]
        // 1、数量-1后为0，直接把这条记录删除
        if (cart.number == 1) {
            if (cart.dishId != null) {
                // 不能只根据dishId删除，还要考虑到一个菜品可能用户选择了多个口味，对应多个菜品
                cartMapper.deleteByDishId(cart.dishId, cart.dishFlavor)
            } else {
                cartMapper.deleteBySetmealId(cart.setmealId)
            }
        } else {
            cart.number = cart.number - 1
            cartMapper.updateNumberById(cart)
        }
    }

    override val list: List<Cart>
        /**
         * 根据userid，获取当前用户的购物车列表
         *
         * @return
         */
        get() = cartMapper.list(
            Cart().apply {
                id = currentId
            }
        ) ?: emptyList()

    /**
     * 根据userid，清空其购物车
     */
    override fun clean() {
        cartMapper.delete(currentId)
    }
}
