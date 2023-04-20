package com.example.atdd.membership.DTO;

import com.example.atdd.membership.domain.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class MembershipResponse {
    private final Long Id;
    private final String userId;
    private final MembershipType membershipType;
    private final Integer point;
}
