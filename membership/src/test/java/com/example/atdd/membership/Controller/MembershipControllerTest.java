package com.example.atdd.membership.Controller;
import com.example.atdd.membership.DTO.MembershipRequest;
import com.example.atdd.membership.DTO.MembershipResponse;
import com.example.atdd.membership.domain.MembershipType;
import com.example.atdd.membership.exception.MembershipErrorResult;
import com.example.atdd.membership.exception.MembershipException;
import com.example.atdd.membership.service.MembershipService;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.example.atdd.membership.domain.MembershipConstants.USER_ID_HEADER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MembershipControllerTest {
    @InjectMocks
    MembershipController membershipController;
    @Mock
    MembershipService membershipService;
    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
                .setControllerAdvice(new MembershipControllerAdvice())
                .build();
        gson = new Gson();
    }

    @Test
    void 멤버쉽가입실패_사용자식별값없음() throws Exception {
        //given
        final String url = "/api/v1/membsership";
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isNotFound());
    }
    @Test
    void 등록실패_포인트가Null() throws Exception {
        //given
        final String url = "/api/v1/membership";
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    void 등록실패_포인트가음수() throws Exception {
        //given
        final String url = "/api/v1/membership";
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    void 등록실패_타입이null() throws Exception {
        //given
        final String url = "/api/v1/membership";
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(100, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 등록실패_service에서Throw() throws Exception {
        //given
        final String url = "/api/v1/membership";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATE_MEMBERSHIP_REGISTER)).when(membershipService)
                .addMembership("12345",MembershipType.NAVER,10000);


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    void 멤버쉽_등록_성공() throws Exception {
        //given
        final String url = "/api/v1/membership";
        doReturn(MembershipResponse.builder().Id(-1L).point(10000).userId("12345").membershipType(MembershipType.NAVER).build())
                .when(membershipService)
                .addMembership("12345",MembershipType.NAVER,10000);
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(membershipRequest(10000,MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isCreated());
        MembershipResponse response = gson.fromJson(resultActions
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8)
                , MembershipResponse.class
        );

        Assertions.assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        Assertions.assertThat(response.getPoint()).isEqualTo(10000);
    }


    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    void 등록실패_잘못된_파라미터(Integer point,MembershipType membershipType) throws Exception {
        //given
        final String url = "/api/v1/membership";


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 멤버쉽조회() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
                MembershipResponse.builder().build(),
                MembershipResponse.builder().build(),
                MembershipResponse.builder().build()
        )).when(membershipService).findAllByUserId("12345");
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
        );

        //then

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 멤버쉽조회실패_없음() throws Exception {
        //given
        String url = "/api/v1/membership/-1";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService)
                .getMembership(-1L,"fail");
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "fail")
        );
        //then
        resultActions.andExpect(status().isNotFound());
    }
    @Test
    void 멤버쉽조회성공() throws Exception {
        //given
        String url = "/api/v1/membership/-1";
        doReturn(MembershipResponse.builder().build()).when(membershipService).getMembership(-1L,"userA");
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "userA")
        );
        //then
        resultActions.andExpect(status().isOk());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, MembershipType.NAVER),
                Arguments.of(-1, MembershipType.NAVER),
                Arguments.of(10000, null)
        );
    }
    private MembershipRequest membershipRequest(Integer point, MembershipType membershipType){
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }
}