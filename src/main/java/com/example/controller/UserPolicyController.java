package com.example.controller;
import com.example.model.UserPolicy;
import com.example.service.UserPolicyService;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user-policies")
public class UserPolicyController {
    private final UserPolicyService userPolicyService;

    public UserPolicyController(UserPolicyService userPolicyService) {
        this.userPolicyService = userPolicyService;
    }
@PostMapping("/create")
public UserPolicy createUserPolicy(@RequestBody UserPolicy userPolicy) {
	  return userPolicyService.createUserPolicy(userPolicy);
}
@GetMapping("/all")
public List<UserPolicy> getAllUserPolicies() {
    return userPolicyService.getAllUserPolicies();
}

@RequestMapping(value="/readOne/{id}",method=RequestMethod.GET)
public UserPolicy readOne(@PathVariable Long id){
	return userPolicyService.readOne(id);
}

@PutMapping("/renew/{id}")
public UserPolicy renewUserPolicy(@PathVariable Long id) {
    return userPolicyService.renew(id);
}
}
