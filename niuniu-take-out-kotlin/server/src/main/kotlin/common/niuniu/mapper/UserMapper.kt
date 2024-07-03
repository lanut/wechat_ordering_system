package common.niuniu.mapper

import common.niuniu.po.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    fun getByOpenid(openid: String?): User?

    fun insert(user: User?)

    @Select("select * from user where id = #{id}")
    fun getById(id: Int?): User?

    fun update(user: User?)

    fun countByMap(map: Map<*, *>?): Int?
}
