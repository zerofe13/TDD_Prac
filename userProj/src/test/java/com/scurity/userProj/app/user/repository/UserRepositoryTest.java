package com.scurity.userProj.app.user.repository;

import com.scurity.userProj.app.user.model.UserRole;
import com.scurity.userProj.app.user.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void 유저저장(){

        //given
        UserEntity user = UserEntity.builder()
                .userId("a@a.com")
                .passwd("1234")
                .role(UserRole.USER)
                .build();

        //when
        UserEntity saveUser = userRepository.save(user);

        //then
        assertThat(saveUser.getUserId()).isEqualTo("a@a.com");
    }

    @Test
    void 유저조회(){
        //given
        UserEntity user = UserEntity.builder()
                .userId("a@a.com")
                .passwd("1234")
                .role(UserRole.USER)
                .build();
        UserEntity saveUser = userRepository.save(user);

        //when
        Optional<UserEntity> findUser = userRepository.findByUserId("a@a.com");

        //then
        assertThat(findUser.get().getUserId()).isEqualTo("a@a.com");
        assertThat(findUser.get().getPasswd()).isEqualTo("1234");
    }

}