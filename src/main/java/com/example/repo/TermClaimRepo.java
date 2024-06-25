package com.example.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.model.TermClaim;

public interface TermClaimRepo extends JpaRepository<TermClaim, Long> {
	@Query(value = "SELECT * FROM term_claim WHERE user_policy_id = :userPolicyId and status='Accepted'", nativeQuery = true)
    List<TermClaim> findTermClaimsByUserPolicyId( Long userPolicyId);

}
