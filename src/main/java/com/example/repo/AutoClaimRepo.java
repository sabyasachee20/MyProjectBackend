package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.model.AutoClaim;

public interface AutoClaimRepo extends JpaRepository<AutoClaim, Long> {
	@Query(value = "SELECT * FROM auto_claim WHERE user_policy_id = :userPolicyId AND status='Accepted'", nativeQuery = true)
List<AutoClaim> findAutoClaimsByUserPolicyId(Long userPolicyId);

}
