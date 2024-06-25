package com.example.controller;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.HealthClaim;
import com.example.model.UserPolicy;
import com.example.service.HealthClaimService;
import com.example.service.UserPolicyService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/health-claims")
public class HealthClaimController {
	  @Value("${react.public.folder}")
	    private String reactPublicFolder;

	private final HealthClaimService healthClaimService;
	 private final UserPolicyService userPolicyService;
    public HealthClaimController(HealthClaimService healthClaimService,UserPolicyService userPolicyService) {
        this.healthClaimService = healthClaimService;
        this.userPolicyService= userPolicyService;
    }
    
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createHealthClaim(
            @RequestParam("disease") String disease,
            @RequestParam("dateOfService") LocalDate dateOfService,
            @RequestParam("hospitalName") String hospitalName,
            @RequestParam("address") String address,
            @RequestParam("doctorInCharge") String doctorInCharge,
            @RequestParam("status") String status,
            @RequestParam("claimAmt") double claimAmt,
            @RequestParam("medicalBill") MultipartFile medicalBill,
            @RequestParam("userPolicyId") Long userPolicyId) {

        if (medicalBill.isEmpty()) {
            return new ResponseEntity<>("Photo of medical bill is required", HttpStatus.BAD_REQUEST);
        }

        try {
          
            String originalFilename = medicalBill.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;

            Path uploadPath = Paths.get(reactPublicFolder + "/assets/images");

          
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }


            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(medicalBill.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

           
            HealthClaim healthClaim = new HealthClaim();
            healthClaim.setDisease(disease);
            healthClaim.setDateofservice(dateOfService);
            healthClaim.setHospitalname(hospitalName);
            healthClaim.setAddress(address);
            healthClaim.setDoctorincharge(doctorInCharge);
            healthClaim.setStatus(status);
            healthClaim.setClaimamt(claimAmt);
            healthClaim.setMedicalbill("assets/images/" + uniqueFileName);

         
            Optional<UserPolicy> userPolicyOptional = userPolicyService.getUserPolicyById(userPolicyId);
            if (userPolicyOptional.isPresent()) {
                healthClaim.setUserPolicy(userPolicyOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user policy ID");
            }

        
            HealthClaim createdHealthClaim = healthClaimService.createHealthClaim(healthClaim);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHealthClaim);

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload photo of medical bill", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@GetMapping("/all")
	public List<HealthClaim> getAllHealthClaims() {
		return healthClaimService.getAllHealthClaims();
	}
	@GetMapping("/user-policies/{userPolicyId}")
    public List<HealthClaim> getHealthClaimsByUserPolicyId(@PathVariable Long userPolicyId) {
        return healthClaimService.findHealthClaimsByUserPolicyId(userPolicyId);
    }
	
	@PutMapping("/expire/{userPolicyId}")
    public List<HealthClaim> expireHealthClaimsByUserPolicyId(@PathVariable Long userPolicyId) {
        return healthClaimService.findHealthClaimsByUserPolicyIdAndExpire(userPolicyId);
    }
	}

