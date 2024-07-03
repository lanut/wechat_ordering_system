package common.niuniu.mapper

import com.github.pagehelper.Page
import common.niuniu.annotation.AutoFill
import common.niuniu.dto.PageDTO
import common.niuniu.enumeration.OperationType
import common.niuniu.po.Employee
import org.apache.ibatis.annotations.*

@Mapper
interface EmployeeMapper {
    @Select("select * from employee where id = #{id}")
    fun getById(id: Int?): Employee?

    @Select("select * from employee where account = #{account}")
    fun getByAccount(account: String?): Employee?

    @Insert(
        "insert into employee (name, account, password, phone, age, gender, pic, status, create_user, update_user, create_time, update_time) VALUES " +
                "(#{name}, #{account}, #{password}, #{phone}, #{age}, #{gender}, #{pic}, #{status}, #{createUser}, #{updateUser}, #{createTime}, #{updateTime})"
    )
    @AutoFill(value = OperationType.REG)
    fun regEmployee(employee: Employee?)

    @Insert(
        "insert into employee (name, account, password, phone, age, gender, pic, status, create_user, update_user, create_time, update_time) VALUES " +
                "(#{name}, #{account}, #{password}, #{phone}, #{age}, #{gender}, #{pic}, #{status}, #{createUser}, #{updateUser}, #{createTime}, #{updateTime})"
    )
    @AutoFill(value = OperationType.INSERT)
    fun addEmployee(employee: Employee?)

    fun pageQuery(pageDTO: PageDTO?): Page<Employee?>?

    /**
     * 加上注解，指定操作类型，方便自动填充时间字段
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    fun update(employee: Employee?)

    @Delete("delete from employee where id = #{id}")
    fun delete(id: Int?)

    @Update("update employee set status = IF(status = 0, 1, 0) where id = #{id}")
    fun onOff(id: Int?)

    @AutoFill(value = OperationType.UPDATE)
    fun updatePwd(employee: Employee?)
}
