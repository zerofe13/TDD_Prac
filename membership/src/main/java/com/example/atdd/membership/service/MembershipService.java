package com.example.atdd.membership.service;

import com.example.atdd.membership.DTO.MembershipResponse;
import com.example.atdd.membership.domain.Membership;
import com.example.atdd.membership.domain.MembershipType;
import com.example.atdd.membership.exception.MembershipErrorResult;
import com.example.atdd.membership.exception.MembershipException;
import com.example.atdd.membership.respository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {

        Optional<Membership> findOptionalUser = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        findOptionalUser.ifPresent(m->{throw new MembershipException(MembershipErrorResult.DUPLICATE_MEMBERSHIP_REGISTER);});

        Membership saveMemberShip = membershipRepository.save(Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point).
                build());

        return MembershipResponse.builder()
                .Id(saveMemberShip.getId())
                .userId(saveMemberShip.getUserId())
                .membershipType(saveMemberShip.getMembershipType())
                .point(saveMemberShip.getPoint())
                .build();
    }
    
    public List<MembershipResponse> findAllByUserId(String userId){
        return membershipRepository.findAllByUserId(userId).stream()
                .map(m-> MembershipResponse.builder()
                            .userId(m.getUserId())
                            .membershipType(m.getMembershipType())
                            .point(m.getPoint())
                            .build()
                ).collect(Collectors.toList());
    }

    public MembershipResponse getMembership(Long Id,String userId){
        Optional<Membership> optionalMembership = membershipRepository.findById(Id);
        Membership membership = optionalMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if(!membership.getUserId().equals(userId)){
            throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
        }
        return MembershipResponse.builder()
                .Id(membership.getId())
                .userId(membership.getUserId())
                .membershipType(membership.getMembershipType())
                .point(membership.getPoint())
                .build();
    }
}
