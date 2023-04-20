package com.example.atdd.membership.Controller;

import com.example.atdd.membership.DTO.MembershipRequest;
import com.example.atdd.membership.DTO.MembershipResponse;
import com.example.atdd.membership.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.atdd.membership.domain.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;
    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER)final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest
            ){
        MembershipResponse response = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipResponse>> getMembershipList(@RequestHeader(USER_ID_HEADER) String userId){
        List<MembershipResponse> allByUserId = membershipService.findAllByUserId(userId);
        return ResponseEntity.ok(allByUserId);
    }

    @GetMapping("/api/v1/membership/{id}")
    public ResponseEntity<MembershipResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) String userId,
            @PathVariable Long id){
        return ResponseEntity.ok(membershipService.getMembership(id,userId));
    }

}
