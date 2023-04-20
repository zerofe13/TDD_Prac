package com.scurity.userProj.app.user.service;

import com.scurity.userProj.app.exception.CustomException;
import com.scurity.userProj.app.exception.ErrorResult;
import com.scurity.userProj.app.user.model.dto.UserRequestDto;
import com.scurity.userProj.app.user.model.UserEntity;
import com.scurity.userProj.app.user.model.dto.UserResponseDto;
import com.scurity.userProj.app.user.model.UserRole;
import com.scurity.userProj.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        Optional<UserEntity> byUserId = userRepository.findByUserId(userDto.getUserId());
        byUserId.ifPresent(p-> {throw new CustomException(ErrorResult.DUPLICATE_USER_ID);});
        UserEntity save = userRepository.save(UserEntity.builder()
                .userId(userDto.getUserId())
                .passwd(userDto.getPasswd())
                .role(UserRole.USER)
                .build());

        return UserResponseDto.builder()
                .userId(save.getUserId())
                .passwd(save.getPasswd())
                .build();
    }

    @Override
    public UserResponseDto getUserByUserid(String userId) {
        Optional<UserEntity> byUserId = userRepository.findByUserId(userId);
        UserEntity userEntity = byUserId.orElseThrow(() -> new CustomException(ErrorResult.USER_NOT_FOUND));

        return UserResponseDto.builder()
                .userId(userEntity.getUserId())
                .passwd(userEntity.getPasswd())
                .role(userEntity.getRole())
                .build();
    }

}
