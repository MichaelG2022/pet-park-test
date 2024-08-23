package pet.park.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.GeoLocation;
import pet.park.entity.PetPark;

@Data
@NoArgsConstructor
public class ContributorData {

	private Long contributorId;
	private String contributorName;
	private String contributorEmail;
	private Set<PetParkResponse> petParks = new HashSet<>();

	// Converts a Contributor object into a ContributorData object
	public ContributorData(Contributor contributor) {
		contributorId = contributor.getContributorId();
		contributorName = contributor.getContributorName();
		contributorEmail = contributor.getContributorEmail();

		// loop to get petParks from contributor
		for (PetPark petPark : contributor.getPetParks()) {
			petParks.add(new PetParkResponse(petPark));
		}

	}

	@Data
	@NoArgsConstructor
	static class PetParkResponse {
		private Long petParkId;
		private String parkName;
		private String directions;
		private String stateOrProvince;
		private String country;
		private GeoLocation geoLocation;
		private Set<String> amenities = new HashSet<>();

		// for converting PetPark object to PetParkResponse object
		PetParkResponse(PetPark petPark) {
			petParkId = petPark.getPetParkId();
			parkName = petPark.getParkName();
			directions = petPark.getDirections();
			stateOrProvince = petPark.getStateOrProvince();
			country = petPark.getCountry();
			geoLocation = new GeoLocation(petPark.getGeoLocation());

			// loop to get amenities from petPark
			for (Amenity amenity : petPark.getAmenities()) {
				amenities.add(amenity.getAmenity());
			}

		} // end PetParkResponse constructor

	} // end PetParkResponse CLASS

} // end ContributorData CLASS
