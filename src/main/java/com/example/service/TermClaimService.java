package com.example.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.model.TermClaim;
import com.example.model.UserPolicy;
import com.example.repo.TermClaimRepo;
@Service
public class TermClaimService {
    private final TermClaimRepo termClaimRepository;
    private final UserPolicyService userPolicyService;

    public TermClaimService(TermClaimRepo termClaimRepository,UserPolicyService userPolicyService) {

        this.termClaimRepository = termClaimRepository;
        this.userPolicyService=userPolicyService;
    }
 
	    public TermClaim createTermClaim(TermClaim termClaim) {
	    	 UserPolicy userPolicy = userPolicyService.readOne(termClaim.getUserPolicy().getUserPolicyId());
			  if (userPolicy == null) {
			        throw new IllegalArgumentException("UserPolicy not found for ID: " + termClaim.getUserPolicy().getUserPolicyId());
			    }

			  if (!"Active".equals(userPolicy.getStatus())) {
				    throw new IllegalArgumentException("Your purchase status isn't Approved");
				}
		        if (termClaim.getDateofdemise().isAfter(userPolicy.getEndDate())) {
		            throw new IllegalArgumentException("Term claim date must be on or before the end date of the user policy.");
		        }
		        if (termClaim.getDateofdemise().isBefore(userPolicy.getStartDate())) {
		            throw new IllegalArgumentException("Term claim date must be on or after the start date of the user policy.");
		        }
		        termClaim.setStatus("Pending");
	        return termClaimRepository.save(termClaim);
	    }
	    public List<TermClaim> getAllTermClaims() {
	        return termClaimRepository.findAll();
	    }
	    public List<TermClaim> findTermClaimsByUserPolicyId( Long userPolicyId) {
	    	 return termClaimRepository.findTermClaimsByUserPolicyId(userPolicyId);
	    }
	    public List<TermClaim> findTermClaimsByUserPolicyIdAndExpire(Long userPolicyId) {
	        List<TermClaim> termClaims = termClaimRepository.findTermClaimsByUserPolicyId(userPolicyId);
	        for (TermClaim termClaim : termClaims) {
	            termClaim.setStatus("Expired");
	            // Save each updated entity
	            termClaimRepository.save(termClaim);
	        }
	        return termClaims;
	    }
}
