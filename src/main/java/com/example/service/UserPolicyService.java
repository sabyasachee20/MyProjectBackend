package com.example.service;
import com.example.model.Policy;
import com.example.model.User;
import com.example.model.UserPolicy;
import com.example.repo.UserPolicyRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
 
@Service
public class UserPolicyService {
    private final UserPolicyRepo userPolicyRepository;

    public UserPolicyService( UserPolicyRepo userPolicyRepository) {
        this.userPolicyRepository = userPolicyRepository;
    }
 
	    public UserPolicy createUserPolicy(UserPolicy userPolicy) {
	        User user = userPolicy.getUser();
	        if (user != null && user.getUserId() == null) {
	        	throw new IllegalStateException("User must be persisted before associating with UserPolicy");
	        }
 
	        Policy policy = userPolicy.getPolicy();
	        if (policy != null && policy.getPolicyId() == null) {
	        	 throw new IllegalStateException("Policy must be persisted before associating with UserPolicy");
	        }
	        
	        userPolicy.setLeftcoverage(userPolicy.getPremium());
	        return userPolicyRepository.save(userPolicy);
	    }
 
    public List<UserPolicy> getAllUserPolicies() {
        return userPolicyRepository.findAll();
    }
    public Optional<UserPolicy> getUserPolicyById(Long id) {
        return userPolicyRepository.findById(id);
    }
    public UserPolicy readOne(Long id) {
    	return userPolicyRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid account ID"));
	}
    public UserPolicy renew(Long id) {
        UserPolicy c = userPolicyRepository.findById(id)
                           .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
        
        if (LocalDate.now().isAfter(c.getEndDate())) {
            throw new IllegalStateException("Renewal is not possible after the policy end date");
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate =c.getEndDate();

        if (!currentDate.equals(endDate)) {
            throw new IllegalStateException("Renewal is only possible on Last Policy Serving Day. Come on "+c.getEndDate()+" to renew your policy");
        }
        
        c.setStartDate(c.getEndDate());
        c.setLeftcoverage(c.getCoverage());
        LocalDate newEndDate = c.getEndDate().plusYears(c.getTerm());
        c.setEndDate(newEndDate);
        c.setPremiumCount(0);
        userPolicyRepository.save(c);

        return c;
    }

    
    public List<UserPolicy> findPoliciesEndingTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return userPolicyRepository.findByEndDate(tomorrow);
    }

    
}