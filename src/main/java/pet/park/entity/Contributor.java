package pet.park.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Contributor {
	// sets contributorId to be PK and autogenerated
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contributorId;
	
	private String contributorName;
	
	// Tells JPA to create an index on email to prevent duplicates
	@Column(unique = true)
	private String contributorEmail;
	
	//one to many relationship with PetPark
	/*
	 * JPA requires recursion to be able to manage relationships properly, so Contributor has PetPark and PetPark has Contributor.
	 * 
	 * To prevent the recursion from causing Jackson to go nuts, we must exclude EqualsAndHashCode and ToString
	 * 
	 * OneToMany annotation maps contributor variable in PetParks to PetParks in Contributor and does On Delete Cascade
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "contributor", cascade = CascadeType.ALL)
	private Set<PetPark> petParks = new HashSet<>();

} // end CLASS
