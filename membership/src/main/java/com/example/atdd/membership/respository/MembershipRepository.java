package com.example.atdd.membership.respository;

import com.example.atdd.membership.domain.Membership;
import com.example.atdd.membership.domain.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership,Long> {
    Optional<Membership> findByUserIdAndMembershipType(String userId, MembershipType membershipType);

    List<Membership> findAllByUserId(String userId);

}
