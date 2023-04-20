package com.example.atdd.membership.respository;

import com.example.atdd.membership.domain.Membership;
import com.example.atdd.membership.domain.MembershipType;
import com.example.atdd.membership.respository.MembershipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MembershipRepositoryTest {

    @Autowired
    MembershipRepository membershipRepository;

    @Test
    void MembershipRepository_is_notNull(){
        assertThat(membershipRepository).isNotNull();
    }

    @Test
    void 멤버쉽등록(){
        //given
        Membership membership = Membership.builder()
                .membershipType(MembershipType.NAVER)
                .userId("userid")
                .point(10000)
                .build();
        //when
        Membership saveMemberShip = membershipRepository.save(membership);
        //then

        assertThat(saveMemberShip).isInstanceOf(Membership.class);
        assertThat(saveMemberShip.getId()).isNotNull();
        assertThat(saveMemberShip.getPoint()).isEqualTo(10000);
        assertThat(saveMemberShip.getMembershipType()).isEqualTo(MembershipType.NAVER);
    }

    @Test
    void 멤버쉽존재테스트(){
        //given
        Membership membership = Membership.builder()
                .userId("userA")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();


        //when
        membershipRepository.save(membership);
        Optional<Membership> findOptionalUser = membershipRepository.findByUserIdAndMembershipType("userA", MembershipType.KAKAO);

        //then

        assertThat(findOptionalUser.get()).isInstanceOf(Membership.class);
        assertThat(findOptionalUser.get().getMembershipType()).isEqualTo(MembershipType.KAKAO);
        assertThat(findOptionalUser.get().getUserId()).isEqualTo("userA");
        assertThat(findOptionalUser.get().getCreateAt()).isNotNull();
    }

    @Test
    void 멤버쉽조회_0(){
        //given

        //when
        List<Membership> userId = membershipRepository.findAllByUserId("userId");
        //then
        assertThat(userId).isEmpty();
    }

    @Test
    void 멤버쉽조회_2(){
        //given
        Membership userA = Membership.builder()
                .userId("user")
                .membershipType(MembershipType.NAVER)
                .point(100)
                .build();
        Membership userB = Membership.builder()
                .userId("user")
                .membershipType(MembershipType.KAKAO)
                .point(10)
                .build();
        membershipRepository.save(userA);
        membershipRepository.save(userB);
        //when
        List<Membership> userId = membershipRepository.findAllByUserId("user");


        //then
        assertThat(userId.size()).isEqualTo(2);
    }
}