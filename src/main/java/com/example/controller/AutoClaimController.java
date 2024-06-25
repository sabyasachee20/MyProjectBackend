package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import com.example.model.AutoClaim;
import com.example.model.UserPolicy;
import com.example.service.AutoClaimService;
import com.example.service.UserPolicyService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auto-claims")
public class AutoClaimController {
	  @Value("${react.public.folder}")
	    private String reactPublicFolder;

	  private final AutoClaimService autoClaimService;
	  private final UserPolicyService userPolicyService;

	    public AutoClaimController(AutoClaimService autoClaimService,UserPolicyService userPolicyService) {
	        this.autoClaimService = autoClaimService;
	        this.userPolicyService= userPolicyService;
	    }
	    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<Object> createAutoClaim(
	            @RequestParam("vehicleModelNo") String vehicleModelNo,
	            @RequestParam("licensePlateNo") String licensePlateNo,
	            @RequestParam("exShowroomPrice") double exShowroomPrice,
	            @RequestParam("vehicleAge") int vehicleAge,
	            @RequestParam("incidentTime") LocalDate incidentTime,
	            @RequestParam("driverAge") int driverAge,
	            @RequestParam("damageDescription") String damageDescription,
	            @RequestParam("damageCost") double damageCost,
	            @RequestParam("status") String status,
	            @RequestParam("photoOfDamage") MultipartFile photoOfDamage,
	            @RequestParam("userPolicyId") Long userPolicyId) {

	        if (photoOfDamage.isEmpty()) {
	            return new ResponseEntity<>("Photo of damage is required", HttpStatus.BAD_REQUEST);
	        }

	        try {
	          
	            String originalFilename = photoOfDamage.getOriginalFilename();
	            String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;

	            Path uploadPath = Paths.get(reactPublicFolder + "/assets/images");

	         
	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }

	           
	            Path filePath = uploadPath.resolve(uniqueFileName);
	            Files.copy(photoOfDamage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	           
	            AutoClaim autoClaim = new AutoClaim();
	            autoClaim.setVehicleModelNo(vehicleModelNo);
	            autoClaim.setLicensePlateNo(licensePlateNo);
	            autoClaim.setExShowroomPrice(exShowroomPrice);
	            autoClaim.setVehicleAge(vehicleAge);
	            autoClaim.setIncidentTime(incidentTime);
	            autoClaim.setDriverAge(driverAge);
	            autoClaim.setDamageDescription(damageDescription);
	            autoClaim.setDamageCost(damageCost);
	            autoClaim.setStatus(status);
	            autoClaim.setPhotoOfDamage("assets/images/" + uniqueFileName);

	            
	            Optional<UserPolicy> userPolicyOptional = userPolicyService.getUserPolicyById(userPolicyId);
	            if (userPolicyOptional.isPresent()) {
	                autoClaim.setUserPolicy(userPolicyOptional.get());
	            } else {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user policy ID");
	            }

	            AutoClaim createdAutoClaim = autoClaimService.createAutoClaim(autoClaim);
	            return ResponseEntity.status(HttpStatus.CREATED).body(createdAutoClaim);

	        } catch (IOException e) {
	            return new ResponseEntity<>("Failed to upload photo of damage", HttpStatus.INTERNAL_SERVER_ERROR); 
	        }
	    }
	@GetMapping("/all")
	public List<AutoClaim> getAllAutoClaims() {
		return autoClaimService.getAllAutoClaims();
	}
	@GetMapping("/user-policies/{userPolicyId}")
	public List<AutoClaim> getAutoClaimsByUserPolicyId(@PathVariable Long userPolicyId) {
	    return autoClaimService.findAutoClaimsByUserPolicyId(userPolicyId);
	}
	@PutMapping("/expire/{userPolicyId}")
    public List<AutoClaim> expireAutoClaimsByUserPolicyId(@PathVariable Long userPolicyId) {
        return autoClaimService.findAutoClaimsByUserPolicyIdAndExpire(userPolicyId);
    }
}
