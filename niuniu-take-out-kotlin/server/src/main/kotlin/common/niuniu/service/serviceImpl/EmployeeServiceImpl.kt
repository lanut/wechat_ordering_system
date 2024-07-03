package common.niuniu.service.serviceImpl

import com.github.pagehelper.PageHelper
import common.niuniu.constant.MessageConstant
import common.niuniu.context.BaseContext.currentId
import common.niuniu.dto.EmployeeDTO
import common.niuniu.dto.EmployeeFixPwdDTO
import common.niuniu.dto.EmployeeLoginDTO
import common.niuniu.dto.PageDTO
import common.niuniu.exception.EmployeeNotFoundException
import common.niuniu.exception.PasswordErrorException
import common.niuniu.mapper.EmployeeMapper
import common.niuniu.po.Employee
import common.niuniu.result.PageResult
import common.niuniu.service.EmployeeService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils

@Service
class EmployeeServiceImpl : EmployeeService {
    @Autowired
    private lateinit var employeeMapper: EmployeeMapper

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    override fun login(employeeLoginDTO: EmployeeLoginDTO?): Employee? {
        val account = employeeLoginDTO!!.account
        var password = employeeLoginDTO.password
        // 先查数据库，看是否存在该账号
        val employee = employeeMapper.getByAccount(account)
            ?: throw EmployeeNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND)
        // 再将前端传过来的密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.toByteArray())
        // 和之前存进数据库的加密的密码进行比对，看看是否一样，不一样要抛异常
        if (password != employee.password) {
            throw PasswordErrorException(MessageConstant.PASSWORD_ERROR)
        }
        return employee
    }

    /**
     * 注册/新增员工
     */
    override fun register(employeeLoginDTO: EmployeeLoginDTO?) {
        // 先对用户的密码进行MD5加密，再存到数据库中
        var password = employeeLoginDTO!!.password
        password = DigestUtils.md5DigestAsHex(password.toByteArray())
        employeeLoginDTO.password = password

        val employee = Employee()
        // 将userLoginDTO的属性拷贝到user中
        BeanUtils.copyProperties(employeeLoginDTO, employee)
        // 为user其他字段填充默认值(7-3=4个)
        employee.name = "员工"
        employee.phone = "11111111111"
        employee.age = 0
        employee.gender = 1
        employee.status = 1
        employee.createUser = 100 // 100表示员工自己注册，此时还不能拿到BaseContext的currentId，只能用100这个数字表示自己了
        employee.updateUser = 100
        employeeMapper.regEmployee(employee)
    }

    /**
     * 根据id获取员工信息
     * @return
     */
    override fun getEmployeeById(id: Int?): Employee? {
        val employee = employeeMapper.getById(id)
        return employee
    }

    /**
     * 员工分页查询
     * @return
     */
    override fun employeePageList(pageDTO: PageDTO?): PageResult? {
        // 传分页参数给PageHelper自动处理，会自动加上limit和count(*)返回分页结果和总记录数
        PageHelper.startPage<Any>(pageDTO!!.page, pageDTO.pageSize)
        val pagelist = employeeMapper.pageQuery(pageDTO)
        return PageResult(pagelist!!.total, pagelist.result)
    }

    /**
     * 修改员工
     * @param employeeDTO
     */
    override fun update(employeeDTO: EmployeeDTO?) {
        // 缺少时间等字段，需要收到加入，否则Mapper里的autofill注解会为EmployeeDTO去setUpdateTime，然而根本没这个方法导致报错！
        val employee = Employee()
        BeanUtils.copyProperties(employeeDTO!!, employee)
        employeeMapper.update(employee)
    }

    /**
     * 删除员工
     */
    override fun delete(id: Int?) {
        employeeMapper.delete(id)
    }

    /**
     * 根据id修改员工状态
     * @param id
     */
    override fun onOff(id: Int?) {
        employeeMapper.onOff(id)
    }

    /**
     * 管理员新增员工
     * @param employeeDTO
     */
    override fun addEmployee(employeeDTO: EmployeeDTO?) {
        // 先对用户的密码进行MD5加密，再存到数据库中
        var password = employeeDTO!!.password
        password = DigestUtils.md5DigestAsHex(password.toByteArray())
        employeeDTO.password = password
        // 创建employee对象，将employeeDTO的属性拷贝到employee中
        val employee = Employee()
        BeanUtils.copyProperties(employeeDTO, employee)
        // 为user其他字段填充默认值
        employee.status = 1
        employeeMapper.addEmployee(employee)
    }

    /**
     * 修改密码
     * @param employeeFixPwdDTO
     */
    override fun fixPwd(employeeFixPwdDTO: EmployeeFixPwdDTO?) {
        var oldPwd = employeeFixPwdDTO!!.oldPwd
        // 将前端传过来的旧密码进行MD5加密
        oldPwd = DigestUtils.md5DigestAsHex(oldPwd.toByteArray())
        // 根据id查询当前账号信息
        val id = currentId
        val employee = employeeMapper.getById(id)
        // 和之前存进数据库的加密的密码进行比对，看看是否一样，不一样要抛异常
        if (oldPwd != employee!!.password) {
            throw PasswordErrorException(MessageConstant.PASSWORD_ERROR)
        }
        // 旧密码正确，将新密码加密后进行更新
        val newPwd = employeeFixPwdDTO.newPwd
        val password = DigestUtils.md5DigestAsHex(newPwd.toByteArray())
        employee.password = password
        employeeMapper.updatePwd(employee)
    }
}
