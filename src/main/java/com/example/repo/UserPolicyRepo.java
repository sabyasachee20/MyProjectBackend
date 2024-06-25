package com.example.repo;
 
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.UserPolicy;
 
public interface UserPolicyRepo extends JpaRepository<UserPolicy,Long> {
	 List<UserPolicy> findByEndDate(LocalDate endDate);
}