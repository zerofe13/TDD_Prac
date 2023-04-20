package com.scurity.userProj.app.user.service;

import com.scurity.userProj.app.user.model.dto.UserRequestDto;
import com.scurity.userProj.app.user.model.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userDto);
    UserResponseDto getUserByUserid(String userId);

}
