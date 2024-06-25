package com.example.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.model.HealthClaim;
import com.example.model.UserPolicy;
import com.example.repo.HealthClaimRepo;

@Service
public class HealthClaimService {
    private final HealthClaimRepo healthClaimRepository;
    private final UserPolicyService userPolicyService;

    public HealthClaimService(HealthClaimRepo healthClaimRepository,UserPolicyService userPolicyService) {
        this.healthClaimRepository = healthClaimRepository;
        this.userPolicyService=userPolicyService;
    }
	  public HealthClaim createHealthClaim(HealthClaim healthClaim) {
		  UserPolicy userPolicy = userPolicyService.readOne(healthClaim.getUserPolicy().getUserPolicyId());
		  if (userPolicy == null) {
		        throw new IllegalArgumentException("UserPolicy not found for ID: " + healthClaim.getUserPolicy().getUserPolicyId());
		    }
		  if (!"Active".equals(userPolicy.getStatus())) {
			    throw new IllegalArgumentException("Your purchase status isn't Approved");
			}

	        if (healthClaim.getDateofservice().isAfter(userPolicy.getEndDate())) {
	            throw new IllegalArgumentException("Health claim date must be on or before the end date "+healthClaim.getUserPolicy().getEndDate()+" of the user policy.");
	        }
	        if (healthClaim.getDateofservice().isBefore(userPolicy.getStartDate())) {
	            throw new IllegalArgumentException("Health claim date must be on or after the start date "+healthClaim.getUserPolicy().getStartDate()+" of the user policy.");
	        }
	        if (healthClaim.getClaimamt() >= userPolicy.getLeftcoverage()) {
	            throw new IllegalArgumentException("Claim amount exceeds the left coverage of the user policy.");
	        }
	        healthClaim.setStatus("Pending");
	        return healthClaimRepository.save(healthClaim);
	    }
	    public List<HealthClaim> getAllHealthClaims() {
	        return healthClaimRepository.findAll();
	    }
	    public List<HealthClaim> findHealthClaimsByUserPolicyId(Long userPolicyId) {
	        return healthClaimRepository.findHealthClaimsByUserPolicyId(userPolicyId);
	    }
	    public List<HealthClaim> findHealthClaimsByUserPolicyIdAndExpire(Long userPolicyId) {
	        List<HealthClaim> healthClaims = healthClaimRepository.findHealthClaimsByUserPolicyId(userPolicyId);
	        for (HealthClaim healthClaim : healthClaims) {
	            healthClaim.setStatus("Expired");
	            // Save each updated entity
	            healthClaimRepository.save(healthClaim);
	        }
	        return healthClaims;
	    }
 
}
