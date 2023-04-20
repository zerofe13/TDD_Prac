package com.scurity.userProj.app.user.service;

import com.scurity.userProj.app.exception.CustomException;
import com.scurity.userProj.app.exception.ErrorResult;
import com.scurity.userProj.app.user.model.UserEntity;
import com.scurity.userProj.app.user.model.UserRole;
import com.scurity.userProj.app.user.model.dto.UserRequestDto;
import com.scurity.userProj.app.user.model.dto.UserResponseDto;
import com.scurity.userProj.app.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;

    private String userId = "a@a.com";
    private String pass = "1234";

    @Test
    void 유저생성_실패_중복(){
        //given
        Mockito.doReturn(Optional.of(user())).when(userRepository).findByUserId(userId);
        //when
        CustomException result = assertThrows(CustomException.class, () -> userService.createUser(UserRequestDto.builder().userId(userId).build()));
        //then
        assertThat(result.getErrorResult()).isEqualTo(ErrorResult.DUPLICATE_USER_ID);

    }

    @Test
    void 유저생성_성공(){
        //given
        Mockito.doReturn(user()).when(userRepository).save(ArgumentMatchers.any(UserEntity.class));
        //when
        UserResponseDto user = userService.createUser(UserRequestDto.builder().build());
        //then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPasswd()).isEqualTo(pass);

    }

    @Test
    void 유저조회_실패_없음(){
        //given

        //when

        //then


    }

    private UserResponseDto userResponse(){
        return UserResponseDto.builder()
                .userId(userId)
                .passwd(pass)
                .role(UserRole.USER)
                .build();
    }

    private UserEntity user(){
        return UserEntity.builder()
                .id(-1L)
                .userId(userId)
                .passwd(pass)
                .role(UserRole.USER)
                .build();
    }

}