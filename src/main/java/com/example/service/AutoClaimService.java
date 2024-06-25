package com.example.service;
import java.util.List;
import org.springframework.stereotype.Service;

import com.example.model.AutoClaim;
import com.example.model.UserPolicy;
import com.example.repo.AutoClaimRepo;

@Service
public class AutoClaimService {
	  private final AutoClaimRepo autoClaimRepository;
	  private final UserPolicyService userPolicyService;

	    public AutoClaimService(AutoClaimRepo autoClaimRepository,UserPolicyService userPolicyService) {
	        this.autoClaimRepository = autoClaimRepository;
	        this.userPolicyService = userPolicyService;
	    }
 
	    public AutoClaim createAutoClaim(AutoClaim autoClaim) {
	    	 UserPolicy userPolicy = userPolicyService.readOne(autoClaim.getUserPolicy().getUserPolicyId());
	         if (userPolicy == null) {
	             throw new IllegalArgumentException("UserPolicy not found for ID: " + autoClaim.getUserPolicy().getUserPolicyId());
	         }
	         if (!"Active".equals(userPolicy.getStatus())) {
	        	    throw new IllegalArgumentException("Your purchase status isn't Approved");
	        	}
	         if (autoClaim.getIncidentTime().isAfter(userPolicy.getEndDate())) {
	             throw new IllegalArgumentException("Auto claim incident time must be on or before the end date:"+autoClaim.getUserPolicy().getEndDate() +"of the user policy.");
	         }
	         if (autoClaim.getIncidentTime().isBefore(userPolicy.getStartDate())) {
	             throw new IllegalArgumentException("Auto claim incident time must be on or after the start date:"+autoClaim.getUserPolicy().getStartDate()+" of the user policy.");
	         }
	         if (autoClaim.getDamageCost() >= userPolicy.getLeftcoverage()) {
	             throw new IllegalArgumentException("Damage cost exceeds the left coverage of the user policy.");
	         }
	         autoClaim.setStatus("Pending");
	        return autoClaimRepository.save(autoClaim);
	    }
	    public List<AutoClaim> getAllAutoClaims() {
	        return autoClaimRepository.findAll();
	    }
	    public List<AutoClaim> findAutoClaimsByUserPolicyId(Long userPolicyId) {
	        return autoClaimRepository.findAutoClaimsByUserPolicyId(userPolicyId);
	    }
	    public List<AutoClaim> findAutoClaimsByUserPolicyIdAndExpire(Long userPolicyId) {
	        List<AutoClaim> autoClaims = autoClaimRepository.findAutoClaimsByUserPolicyId(userPolicyId);
	        for (AutoClaim autoClaim : autoClaims) {
	            autoClaim.setStatus("Expired");
	            // Save each updated entity
	            autoClaimRepository.save(autoClaim);
	        }
	        return autoClaims;
	    }
 
}
