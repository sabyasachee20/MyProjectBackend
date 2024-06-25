package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.model.HealthClaim;

public interface HealthClaimRepo extends JpaRepository<HealthClaim,Long>{
	@Query(value = "SELECT * FROM health_claim WHERE user_policy_id = :userPolicyId and status='Accepted'", nativeQuery = true)
    List<HealthClaim> findHealthClaimsByUserPolicyId( Long userPolicyId);

}
