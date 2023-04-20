package com.example.atdd.membership.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {
    DUPLICATE_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST,"Duplicated Membership Register Request"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"Unknown exception"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND,"MemberShip not found")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
