package com.example.model;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

 
@Entity
public class HealthClaim {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long healthClaimId;
	private String disease;
	private LocalDate dateofservice;
	
	private String hospitalname;
	private String address;
	private String doctorincharge;
	private String medicalbill;
	private String status;
	private double claimamt;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userPolicyId")
    private UserPolicy userPolicy;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public double getClaimamt() {
		return claimamt;
	}
	public void setClaimamt(double claimamt) {
		this.claimamt = claimamt;
	}
	public Long getHealthClaimId() {
		return healthClaimId;
	}
	public void setHealthClaimId(Long healthClaimId) {
		this.healthClaimId = healthClaimId;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public LocalDate getDateofservice() {
		return dateofservice;
	}
	public void setDateofservice(LocalDate dateofservice) {
		this.dateofservice = dateofservice;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDoctorincharge() {
		return doctorincharge;
	}
	public void setDoctorincharge(String doctorincharge) {
		this.doctorincharge = doctorincharge;
	}
	public String getMedicalbill() {
		return medicalbill;
	}
	public void setMedicalbill(String medicalbill) {
		this.medicalbill = medicalbill;
	}
	public UserPolicy getUserPolicy() {
		return userPolicy;
	}
	public void setUserPolicy(UserPolicy userPolicy) {
		this.userPolicy = userPolicy;
	}
	@Override
	public String toString() {
		return "HealthClaim [healthClaimId=" + healthClaimId + ", disease=" + disease + ", dateofservice="
				+ dateofservice + ", hospitalname=" + hospitalname + ", address=" + address + ", doctorincharge="
				+ doctorincharge + ", medicalbill=" + medicalbill + ", status=" + status + ", claimamt=" + claimamt
				+ ", userPolicy=" + userPolicy + "]";
	}
	public HealthClaim(Long healthClaimId, String disease, LocalDate dateofservice, String hospitalname,
			String address, String doctorincharge, String medicalbill, String status, double claimamt,
			UserPolicy userPolicy) {
		super();
		this.healthClaimId = healthClaimId;
		this.disease = disease;
		this.dateofservice = dateofservice;
		this.hospitalname = hospitalname;
		this.address = address;
		this.doctorincharge = doctorincharge;
		this.medicalbill = medicalbill;
		this.status = status;
		this.claimamt = claimamt;
		this.userPolicy = userPolicy;
	}
	public HealthClaim() {
		super();
	}
	
	
	
 
}
 