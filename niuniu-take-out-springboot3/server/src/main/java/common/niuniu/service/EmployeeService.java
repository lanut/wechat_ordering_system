package common.niuniu.service;

import common.niuniu.dto.EmployeeDTO;
import common.niuniu.dto.EmployeeFixPwdDTO;
import common.niuniu.dto.EmployeeLoginDTO;
import common.niuniu.dto.PageDTO;
import common.niuniu.entity.Employee;
import common.niuniu.result.PageResult;

public interface EmployeeService {
    Employee getEmployeeById(Integer id);

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void register(EmployeeLoginDTO employeeLoginDTO);

    PageResult employeePageList(PageDTO pageDTO);

    void update(EmployeeDTO employeeDTO);

    void delete(Integer id);

    void onOff(Integer id);

    void addEmployee(EmployeeDTO employeeDTO);

    void fixPwd(EmployeeFixPwdDTO employeeFixPwdDTO);
}
