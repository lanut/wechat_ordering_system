package common.niuniu.service

import common.niuniu.dto.CategoryDTO
import common.niuniu.dto.CategoryTypePageDTO
import common.niuniu.po.Category
import common.niuniu.result.PageResult

interface CategoryService {
    fun addCategory(categoryDTO: CategoryDTO?)

    fun getPageList(categoryTypePageDTO: CategoryTypePageDTO?): PageResult?

    fun getList(type: Int): List<Category>

    fun getById(id: Int?): Category?
    fun onOff(id: Int?)

    fun udpate(categoryDTO: CategoryDTO?)

    fun delete(id: Int?)
}
