package common.niuniu.service.serviceImpl

import com.github.pagehelper.PageHelper
import common.niuniu.constant.MessageConstant
import common.niuniu.constant.StatusConstant
import common.niuniu.dto.SetmealDTO
import common.niuniu.dto.SetmealPageDTO
import common.niuniu.exception.DeleteNotAllowedException
import common.niuniu.mapper.SetmealDishMapper
import common.niuniu.mapper.SetmealMapper
import common.niuniu.po.Setmeal
import common.niuniu.po.SetmealDish
import common.niuniu.result.PageResult
import common.niuniu.service.SetmealService
import common.niuniu.vo.DishItemVO
import common.niuniu.vo.SetmealVO
import common.niuniu.vo.SetmealWithPicVO
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class SetmealServiceImpl : SetmealService {
    @Autowired
    private lateinit var setmealMapper: SetmealMapper

    @Autowired
    private lateinit var setmealDishMapper: SetmealDishMapper

    /**
     * 新增套餐
     * @param setmealDTO
     */
    override fun addSetmeal(setmealDTO: SetmealDTO?) {
        val setmeal = Setmeal()
        BeanUtils.copyProperties(setmealDTO!!, setmeal)
        setmeal.status = 1 // 默认启用套餐
        setmealMapper.addSetmeal(setmeal)
        // 套餐包含的菜品批量插入
        val setmealId = setmeal.id
        // 1. 遍历setmealDTO中的菜品列表，每个菜品都为其setmealId字段赋值
        val setmealDishes = setmealDTO.setmealDishes
        if (setmealDishes != null && setmealDishes.isNotEmpty()) {
            setmealDishes.forEach(Consumer { setmealDish: SetmealDish? -> setmealDish!!.setmealId = setmealId })
            setmealDishMapper.insertBatch(setmealDishes)
        }
    }

    /**
     * 套餐条件分页查询
     * @param setmealPageDTO
     * @return
     */
    override fun getPageList(setmealPageDTO: SetmealPageDTO?): PageResult? {
        PageHelper.startPage<Any>(setmealPageDTO!!.page, setmealPageDTO.pageSize)
        val setmealList = setmealMapper.getPageList(setmealPageDTO)
        return PageResult(setmealList!!.total, setmealList.result)
    }

    /**
     * 根据套餐id查询套餐，包括菜品信息
     * @param id
     * @return
     */
    override fun getSetmealById(id: Int?): SetmealVO? {
        val setmeal = setmealMapper.getSetmealById(id)
        val setmealDishes = setmealDishMapper.getDishesBySetmealId(id)
        // 组成SetmealVO后返回
        val setmealVO = SetmealVO()
        BeanUtils.copyProperties(setmeal!!, setmealVO)
        setmealVO.setmealDishes = setmealDishes
        return setmealVO
    }

    /**
     * 起售停售套餐
     * @param id
     */
    override fun onOff(id: Int?) {
        setmealMapper.onOff(id)
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    override fun update(setmealDTO: SetmealDTO?) {
        val setmeal = Setmeal()
        BeanUtils.copyProperties(setmealDTO!!, setmeal)
        // 先修改套餐setmeal
        setmealMapper.update(setmeal)
        // 再修改套餐下的菜品setmealDish
        // 由于行数据可能不同，因此需要先根据setmealId批量删除，再批量插入
        val setmealId = setmealDTO.id
        setmealDishMapper.deleteBySetmealId(setmealId)
        val setmealDishes = setmealDTO.setmealDishes
        setmealDishes.forEach(Consumer { setmealDish: SetmealDish? -> setmealDish!!.setmealId = setmealId })
        setmealDishMapper.insertBatch(setmealDishes)
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    override fun deleteBatch(ids: List<Int?>?) {
        // 遍历要删除的所有套餐，如果但凡有一个在起售就抛异常
        for (id in ids!!) {
            val setmeal = setmealMapper.getSetmealById(id)
            if (setmeal!!.status == StatusConstant.ENABLE) {
                throw DeleteNotAllowedException(MessageConstant.SETMEAL_ON_SALE)
            }
        }
        setmealMapper.deleteBatch(ids)
        setmealDishMapper.deleteBatch(ids)
    }

    /**
     * 根据分类id查询所有套餐
     * @return
     */
    override fun getList(categoryId: Int?): List<Setmeal>? {
        // 还有一个条件：起售的套餐才能展示在客户端
        val setmeal = Setmeal()
        setmeal.categoryId = categoryId
        setmeal.status = StatusConstant.ENABLE
        val setmealList = setmealMapper.getList(setmeal)
        return setmealList
    }

    /**
     * 根据套餐id查询所有菜品
     * @param id
     * @return
     */
    override fun getSetmealDishesById(id: Int?): List<DishItemVO>? {
        val dishItemVOList = setmealMapper.getSetmealDishesById(id)
        return dishItemVOList
    }

    /**
     * 根据套餐id获取套餐详情，其中菜品都要有pic图片信息
     * @param id
     * @return
     */
    override fun getSetmealWithPic(id: Int?): SetmealWithPicVO? {
        val setmeal = setmealMapper.getSetmealById(id)
        // 该套餐下的每个菜品都需要加上pic字段
        val dishWithPics = setmealDishMapper.getDishesWithPic(id)
        // 组成setmealWithPicVO后返回
        val setmealWithPicVO = SetmealWithPicVO()
        BeanUtils.copyProperties(setmeal!!, setmealWithPicVO)
        setmealWithPicVO.setmealDishes = dishWithPics
        return setmealWithPicVO
    }
}
