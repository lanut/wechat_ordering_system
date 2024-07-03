package common.niuniu.service

import common.niuniu.dto.UserDTO
import common.niuniu.dto.UserLoginDTO
import common.niuniu.po.User

interface UserService {
    fun wxLogin(userLoginDTO: UserLoginDTO?): User?

    fun getUser(id: Int?): User?

    fun update(userDTO: UserDTO?)
}
