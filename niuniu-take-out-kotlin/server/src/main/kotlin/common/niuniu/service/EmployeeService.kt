package common.niuniu.service

import common.niuniu.dto.EmployeeDTO
import common.niuniu.dto.EmployeeFixPwdDTO
import common.niuniu.dto.EmployeeLoginDTO
import common.niuniu.dto.PageDTO
import common.niuniu.po.Employee
import common.niuniu.result.PageResult

interface EmployeeService {
    fun getEmployeeById(id: Int?): Employee?

    fun login(employeeLoginDTO: EmployeeLoginDTO?): Employee?

    fun register(employeeLoginDTO: EmployeeLoginDTO?)

    fun employeePageList(pageDTO: PageDTO?): PageResult?

    fun update(employeeDTO: EmployeeDTO?)

    fun delete(id: Int?)

    fun onOff(id: Int?)

    fun addEmployee(employeeDTO: EmployeeDTO?)

    fun fixPwd(employeeFixPwdDTO: EmployeeFixPwdDTO?)
}
