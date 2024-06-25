package com.example.model;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
@Entity
@Table(uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"userPolicyId"})
	})
public class TermClaim {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long termClaimId;
	  
	  private LocalDate dateofdemise;
	    private String deathProof;
	    private String nomineeProof;
	    private String status;
 
	    @OneToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "userPolicyId")
	    private UserPolicy userPolicy;
 
		public Long getTermClaimId() {
			return termClaimId;
		}
 
		public void setTermClaimId(Long termClaimId) {
			this.termClaimId = termClaimId;
		}
 
		public String getDeathProof() {
			return deathProof;
		}
 
		public void setDeathProof(String deathProof) {
			this.deathProof = deathProof;
		}
 
		public String getNomineeProof() {
			return nomineeProof;
		}
 
		public void setNomineeProof(String nomineeProof) {
			this.nomineeProof = nomineeProof;
		}
 
		public UserPolicy getUserPolicy() {
			return userPolicy;
		}
 
		public void setUserPolicy(UserPolicy userPolicy) {
			this.userPolicy = userPolicy;
		}

		public LocalDate getDateofdemise() {
			return dateofdemise;
		}

		public void setDateofdemise(LocalDate dateofdemise) {
			this.dateofdemise = dateofdemise;
		}
		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		

		@Override
		public String toString() {
			return "TermClaim [termClaimId=" + termClaimId + ", dateofdemise=" + dateofdemise + ", deathProof="
					+ deathProof + ", nomineeProof=" + nomineeProof + ", status=" + status + ", userPolicy="
					+ userPolicy + "]";
		}
		

		public TermClaim(Long termClaimId, LocalDate dateofdemise, String deathProof, String nomineeProof,
				String status, UserPolicy userPolicy) {
			super();
			this.termClaimId = termClaimId;
			this.dateofdemise = dateofdemise;
			this.deathProof = deathProof;
			this.nomineeProof = nomineeProof;
			this.status = status;
			this.userPolicy = userPolicy;
		}

		public TermClaim() {
			super();
		}
 

 
}