package common.niuniu.service.serviceImpl

import com.github.pagehelper.PageHelper
import common.niuniu.constant.MessageConstant
import common.niuniu.constant.StatusConstant
import common.niuniu.context.BaseContext.currentId
import common.niuniu.dto.DishDTO
import common.niuniu.dto.DishPageDTO
import common.niuniu.exception.DeleteNotAllowedException
import common.niuniu.mapper.DishFlavorMapper
import common.niuniu.mapper.DishMapper
import common.niuniu.mapper.SetmealDishMapper
import common.niuniu.po.Dish
import common.niuniu.po.DishFlavor
import common.niuniu.result.PageResult
import common.niuniu.service.DishService
import common.niuniu.vo.DishVO
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class DishServiceImpl : DishService {
    @Autowired
    private lateinit var dishMapper: DishMapper

    @Autowired
    private lateinit var dishFlavorMapper: DishFlavorMapper

    @Autowired
    private lateinit var setmealDishMapper: SetmealDishMapper

    /**
     * 新增菜品
     * @param dishDTO
     */
    override fun addDishWithFlavor(dishDTO: DishDTO?) {
        // 不仅要向dish表添加数据，还要向dish_flavor表添加口味数据
        val dish = Dish()
        BeanUtils.copyProperties(dishDTO!!, dish)
        dish.status = 1
        dishMapper.addDish(dish)
        println("新增dish成功！")
        // 由于在动态sql中，用了useGeneralKeys=true，因此会在插入数据后自动返回该行数据的主键id，
        // 并且使用keyProperty="id"，表示将返回的主键值赋值给dish的id属性，下面就可以dish.getId()获取到id
        val dishId = dish.id

        // 有了DTO中的flavors，加上上面的dish_id，就可以批量插入口味数据到dish_flavor表了

        // 1. 先拿到口味列表
        val flavorList = dishDTO.flavors
        if (flavorList != null && flavorList.isNotEmpty()) {
            // 2. 再遍历这些口味，每条口味信息都加上dish_id字段，这样相当于DishFlavor就有id,name,value,dish_id四个完整字段了
            flavorList.forEach(Consumer { dishFlavor: DishFlavor? -> dishFlavor!!.dishId = dishId })
            // 3. 最后批量插入口味数据，动态sql中需要根据,分割，foreach批量插入
            dishFlavorMapper.insertBatch(flavorList)
        }
    }

    /**
     * 根据条件page信息分页查询菜品
     * @param dishPageDTO
     * @return
     */
    override fun getPageList(dishPageDTO: DishPageDTO?): PageResult? {
        PageHelper.startPage<Any>(dishPageDTO!!.page, dishPageDTO.pageSize)
        val dishList = dishMapper.getPageList(dishPageDTO)
        return PageResult(dishList!!.total, dishList.result)
    }

    /**
     * 根据id查询对应的dish和对应的flavors
     * @param id
     * @return
     */
    override fun getDishWithFlavorById(id: Int?): DishVO? {
        // 使用 useGenerateKey 和 keyProperty 来返回对应的id
        val dish = dishMapper.getById(id)
        // 根据id查询对应的口味数据
        val dishFlavors = dishFlavorMapper.getByDishId(id)
        val dishVO = DishVO()
        BeanUtils.copyProperties(dish!!, dishVO)
        dishVO.flavors = dishFlavors
        return dishVO
    }

    /**
     * 更新菜品和对应口味数据
     * @param dishDTO
     */
    override fun updateDishWithFlavor(dishDTO: DishDTO?) {
        val dish = Dish()
        BeanUtils.copyProperties(dishDTO!!, dish)
        // 先根据id更新菜品数据
        dishMapper.update(dish)
        // 再根据where dishId=菜品id，去批量更新对应的口味数据
        val dishId = dishDTO.id
        // 原来的口味和新的口味的行数据量可能不一样，不能直接更新，只能批量删除再批量插入
        // 1. 根据dishId批量删除
        dishFlavorMapper.deleteByDishId(dishId)
        val flavorList = dishDTO.flavors
        if (flavorList != null && flavorList.isNotEmpty()) {
            flavorList.forEach(Consumer { dishFlavor: DishFlavor? -> dishFlavor!!.dishId = dishId })
            // 2. 再批量插入口味数据
            dishFlavorMapper.insertBatch(flavorList)
        }
    }

    /**
     * 根据菜品id列表，批量删除菜品
     * @param ids
     */
    override fun deleteBatch(ids: List<Int?>?) {
        // 1. 遍历所有菜品，如果有任何一个在起售，则抛异常表示不能删除
        for (id in ids!!) {
            val dish = dishMapper.getById(id)
            if (dish!!.status == StatusConstant.ENABLE) {
                throw DeleteNotAllowedException(MessageConstant.DISH_ON_SALE)
            }
        }
        // 2. 遍历所有菜品，如果有关联套餐也不能删除
        val setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids)
        if (!setmealIds.isNullOrEmpty()) {
            throw DeleteNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL)
        }
        // 可以批量删除菜品和对应口味数据了
        dishMapper.deleteBatch(ids)
        dishFlavorMapper.deleteBatch(ids)
    }

    /**
     * 根据id修改起售停售状态
     * @param id
     */
    override fun onOff(id: Int?) {
        dishMapper.onOff(id)
    }

    /**
     * 获取对应分类下的所有菜品，包括对应口味
     * @param dish
     * @return
     */
    override fun getDishesWithFlavorById(dish: Dish?): List<DishVO> {
        val dishList = dishMapper.getList(dish)
        val dishVOList: MutableList<DishVO> = ArrayList()
        // 对菜品列表的每个菜品都加上对应口味，分别封装成DishVO再返回
        if (dishList != null) {
            for (d in dishList) {
                val dishVO = DishVO()
                BeanUtils.copyProperties(d!!, dishVO)
                val flavors = dishFlavorMapper.getByDishId(d.id)
                dishVO.flavors = flavors
                dishVOList.add(dishVO)
            }
        }
        return dishVOList
    }
}
