package common.niuniu.service;

import common.niuniu.dto.UserDTO;
import common.niuniu.dto.UserLoginDTO;
import common.niuniu.entity.User;

public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);

    User getUser(Integer id);

    void update(UserDTO userDTO);
}
