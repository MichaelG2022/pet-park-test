package pet.park.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Amenity {
	// sets PK and autogenerate for Amenity table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long amenityId;	
	
	private String amenity;
	
	// add PetPark for JPA required recursion
	// Exclude hash code, equals, and toString
	// set relationship type and join column with amenities variable in PetPark class
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "amenities")
	private Set<PetPark> petParks = new HashSet<>();

} // end CLASS
