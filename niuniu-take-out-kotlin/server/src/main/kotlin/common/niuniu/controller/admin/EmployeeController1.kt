package common.niuniu.controller.admin

import common.niuniu.dto.EmployeeDTO
import common.niuniu.dto.EmployeeFixPwdDTO
import common.niuniu.dto.EmployeeLoginDTO
import common.niuniu.dto.PageDTO
import common.niuniu.po.Employee
import common.niuniu.properties.JwtProperties
import common.niuniu.result.PageResult
import common.niuniu.result.Result
import common.niuniu.service.EmployeeService
import common.niuniu.utils.JwtUtil.createJWT
import common.niuniu.utils.logInfo
import common.niuniu.vo.EmployeeLoginVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/employee")
class EmployeeController {
    @Autowired
    private lateinit var employeeService: EmployeeService

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    fun login(@RequestBody employeeLoginDTO: EmployeeLoginDTO): Result<EmployeeLoginVO> {
        logInfo("用户传过来的登录信息DTO: $employeeLoginDTO")
        val employee = employeeService.login(employeeLoginDTO)
        // 上面的没抛异常，正常来到这里，说明登录成功
        // claims就是用户数据payload部分
        val claims: MutableMap<String, Any> = HashMap() // jsonwebtoken包底层就是Map<String, Object>格式，不能修改！
        claims["employeeId"] = employee!!.id
        // 需要加个token给他，再返回响应
        val token = createJWT(
            jwtProperties.employeeSecretKey,
            jwtProperties.employeeTtl,
            claims
        )
        val employeeLoginVO = EmployeeLoginVO.builder()
            .id(employee.id)
            .account(employee.account)
            .token(token)
            .build()
        return Result.success(employeeLoginVO)
    }

    /**
     * 员工注册（其实就是新增操作而已，和token什么的无关！）
     * @return
     */
    @PostMapping("/register")
    fun register(@RequestBody employeeLoginDTO: EmployeeLoginDTO?): Result<*> {
        logInfo("用户传过来的注册信息(和登录格式一样的DTO): $employeeLoginDTO")
        employeeService.register(employeeLoginDTO)
        return Result.success()
    }

    /**
     * 修改当前登录账号的密码
     * @param employeeFixPwdDTO
     * @return
     */
    @PutMapping("/fixpwd")
    fun fixPwd(@RequestBody employeeFixPwdDTO: EmployeeFixPwdDTO?): Result<*> {
        logInfo("新旧密码信息：$employeeFixPwdDTO")
        employeeService.fixPwd(employeeFixPwdDTO)
        return Result.success()
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping("/add")
    fun addEmployee(@RequestBody employeeDTO: EmployeeDTO): Result<*> {
        logInfo("新增用户的信息：$employeeDTO")
        employeeService.addEmployee(employeeDTO)
        return Result.success()
    }

    /**
     * 根据id获取员工信息
     * @return
     */
    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: Int): Result<Employee> {
        val employee = employeeService.getEmployeeById(id)!!
        return Result.success(employee)
    }

    /**
     * 员工条件分页查询
     * @param pageDTO
     * @return
     */
    @GetMapping("/page")
    fun employeePageList(pageDTO: PageDTO): Result<PageResult> {
        logInfo("前端传过来的page参数：$pageDTO")
        val pageResult = employeeService.employeePageList(pageDTO)!!
        return Result.success(pageResult)
    }

    /**
     * 修改员工信息（管理员能修改所有，员工只能修改自己）
     * @param employeeDTO
     * @return
     */
    @PutMapping("/update")
    fun update(@RequestBody employeeDTO: EmployeeDTO?): Result<*> {
        logInfo("修改员工的formDTO: $employeeDTO")
        employeeService.update(employeeDTO)
        return Result.success()
    }

    /**
     * 根据id启用禁用员工
     * @param id
     * @return
     */
    @PutMapping("/status/{id}")
    fun onOff(@PathVariable id: Int): Result<*> {
        logInfo("启用禁用员工账号：$id")
        employeeService.onOff(id)
        return Result.success()
    }

    /**
     * 管理员根据id删除员工
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): Result<*> {
        logInfo("根据id删除员工: $id")
        employeeService.delete(id)
        return Result.success()
    }
}
