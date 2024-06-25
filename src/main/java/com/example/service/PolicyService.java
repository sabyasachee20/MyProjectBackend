package com.example.service;
 
import com.example.model.Policy;
import com.example.repo.PolicyRepo;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class PolicyService {
 
    private final PolicyRepo policyRepository;

    public PolicyService(PolicyRepo policyRepository) {
        this.policyRepository = policyRepository;
    }
    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }
 
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }
 
    public Optional<Policy> getPolicyById(Long policyId) {
        return policyRepository.findById(policyId);
    }
}