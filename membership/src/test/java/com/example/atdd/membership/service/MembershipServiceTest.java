package com.example.atdd.membership.service;

import com.example.atdd.membership.DTO.MembershipResponse;
import com.example.atdd.membership.domain.Membership;
import com.example.atdd.membership.domain.MembershipType;
import com.example.atdd.membership.exception.MembershipErrorResult;
import com.example.atdd.membership.exception.MembershipException;
import com.example.atdd.membership.respository.MembershipRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    MembershipService membershipService;
    @Mock
    MembershipRepository membershipRepository;

    private final String userA = "userA";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;

    @Test
    void 멤버쉽등록실패_이미존재함(){
        //given
        doReturn(Optional.of(membership())).when(membershipRepository).findByUserIdAndMembershipType(userA,membershipType);
        //when
        final MembershipException result = assertThrows(MembershipException.class,()->membershipService.addMembership(userA,membershipType,point));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATE_MEMBERSHIP_REGISTER);
    }
    @Test
    void 멤버쉽등록_성공(){
        //given
        doReturn(Optional.ofNullable(null)).when(membershipRepository).findByUserIdAndMembershipType(userA,membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));
        //when
        MembershipResponse saveMembership = membershipService.addMembership(userA, membershipType, point);
        //then
        assertThat(saveMembership).isInstanceOf(MembershipResponse.class);
        assertThat(saveMembership.getId()).isNotNull();
        assertThat(saveMembership.getMembershipType()).isEqualTo(membershipType);
        assertThat(saveMembership.getUserId()).isEqualTo(userA);

        //verify (mokito 를 통해 mock객체의 메소드가 호출된 횟수)
        verify(membershipRepository,times(1)).findByUserIdAndMembershipType(userA,membershipType);
        verify(membershipRepository,times(1)).save(any(Membership.class));
    }

    @Test
    void 멤버쉽목록조회(){
        //given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId("user");


        //when
        List<MembershipResponse> result = membershipService.findAllByUserId("user");

        //then
        assertThat(result.size()).isEqualTo(3);


    }
    @Test
    void 멤버쉽조회실패_해당멤버쉽없음(){
        //given
        doReturn(Optional.empty()).when(membershipRepository).findById(-1L);
        //when
        MembershipException membershipException = assertThrows(MembershipException.class, () -> membershipService.getMembership(-1L, userA));

        //then
        assertThat(membershipException.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);

    }
    @Test
    void 멤버쉽조회실패_잘못된userId(){
        //given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(-1L);
        //when
        MembershipException membershipException = assertThrows(MembershipException.class, () -> membershipService.getMembership(-1L, userA));
        //then
        assertThat(membershipException.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }
    @Test
    void 조회성공(){
        //given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(-1L);
        //when
        MembershipResponse membership = membershipService.getMembership(-1L, userA);
        //then
        assertThat(membership.getId()).isEqualTo(-1L);
        assertThat(membership.getUserId()).isEqualTo(userA);
    }

    private Membership membership(){
        return Membership.builder()
                .id(-1L)
                .userId(userA)
                .membershipType(MembershipType.NAVER)
                .point(point)
                .build();
    }
}