package com.example.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.TermClaim;
import com.example.model.UserPolicy;
import com.example.service.TermClaimService;
import com.example.service.UserPolicyService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/term-claims")
public class TermClaimController {
	 @Value("${react.public.folder}")
	    private String reactPublicFolder;
    private final TermClaimService termClaimService;
    private final UserPolicyService userPolicyService;

    public TermClaimController(TermClaimService termClaimService,UserPolicyService userPolicyService) {
        this.termClaimService = termClaimService;
        this.userPolicyService= userPolicyService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createTermClaim(
            @RequestParam("dateofdemise") LocalDate dateofdemise,
            @RequestParam("deathProof") MultipartFile deathProof,
            @RequestParam("nomineeProof") MultipartFile nomineeProof,
            @RequestParam("status") String status,
            @RequestParam("userPolicyId") Long userPolicyId) {

        if (deathProof.isEmpty() || nomineeProof.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Death proof and nominee proof are required");
        }

        try {
          
            String deathProofFilename = System.currentTimeMillis() + "_" + deathProof.getOriginalFilename();
            String nomineeProofFilename = System.currentTimeMillis() + "_" + nomineeProof.getOriginalFilename();

            Path uploadPath = Paths.get(reactPublicFolder + "/assets/images");

           
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            
            Path deathProofPath = uploadPath.resolve(deathProofFilename);
            Files.copy(deathProof.getInputStream(), deathProofPath, StandardCopyOption.REPLACE_EXISTING);

            Path nomineeProofPath = uploadPath.resolve(nomineeProofFilename);
            Files.copy(nomineeProof.getInputStream(), nomineeProofPath, StandardCopyOption.REPLACE_EXISTING);

           
            TermClaim termClaim = new TermClaim();
            termClaim.setDateofdemise(dateofdemise);
            termClaim.setDeathProof("assets/images/" + deathProofFilename);
            termClaim.setNomineeProof("assets/images/" + nomineeProofFilename);
            termClaim.setStatus(status);

            
            Optional<UserPolicy> userPolicyOptional = userPolicyService.getUserPolicyById(userPolicyId);
            if (userPolicyOptional.isPresent()) {
                termClaim.setUserPolicy(userPolicyOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user policy ID");
            }

           
            TermClaim createdTermClaim = termClaimService.createTermClaim(termClaim);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTermClaim);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload proof documents");
        }
    }
	@GetMapping("/all")
	public List<TermClaim> getAllTermClaims() {
		return termClaimService.getAllTermClaims();
	}
	@GetMapping("/user-policies/{userPolicyId}")
	public List<TermClaim> getTermClaimsByUserPolicyId(@PathVariable Long userPolicyId) {
	    return termClaimService.findTermClaimsByUserPolicyId(userPolicyId);
	}
	@PutMapping("/expire/{userPolicyId}")
    public List<TermClaim> expireTermClaimsByUserPolicyId(@PathVariable Long userPolicyId) {
        return termClaimService.findTermClaimsByUserPolicyIdAndExpire(userPolicyId);
    }
}
