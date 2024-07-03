package common.niuniu.mapper

import com.github.pagehelper.Page
import common.niuniu.annotation.AutoFill
import common.niuniu.dto.CategoryTypePageDTO
import common.niuniu.enumeration.OperationType
import common.niuniu.po.Category
import org.apache.ibatis.annotations.*

@Mapper
interface CategoryMapper {
    @AutoFill(OperationType.INSERT)
    @Insert(
        "insert into category (name, type, sort, status, create_user, update_user, create_time, update_time) VALUES " +
                "(#{name}, #{type}, #{sort}, #{status}, #{createUser}, #{updateUser}, #{createTime}, #{updateTime})"
    )
    fun add(category: Category?)

    fun getPageList(categoryTypePageDTO: CategoryTypePageDTO?): Page<Category?>

    @Select("select * from category where id = #{id}")
    fun getById(id: Int?): Category?

    @AutoFill(OperationType.UPDATE)
    fun update(category: Category?)

    @Delete("delete from category where id = #{id}")
    fun delete(id: Int?)

    @Update("update category set status = IF(status = 0, 1, 0) where id = #{id}")
    fun onOff(id: Int?)

    /**
     * 根据type查询分类，没有就查所有，且只能查有启用的分类
     * @param type
     * @return
     */
    fun getList(type: Int): List<Category>
}
