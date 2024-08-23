package pet.park.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.park.controller.model.ContributorData;
import pet.park.controller.model.PetParkData;
import pet.park.service.ParkService;

@RestController
@RequestMapping("/pet_park")
@Slf4j
public class ParkController {

	@Autowired
	private ParkService parkService;

// *********************** POST CONTRIBUTOR **********************************************  
	// /pet_park/contributor
	@PostMapping("/contributor")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ContributorData insertContributor(@RequestBody ContributorData contributorData) {
		log.info("Creating contributor {}", contributorData);
		return parkService.saveContributor(contributorData);
	} // end insertContributor

// *********************** UPDATE CONTRIBUTOR ********************************************
	// /pet_park/contributor/contributorId}
	// @PathVariable takes contributorId as parameter
	@PutMapping("/contributor/{contributorId}")
	public ContributorData upDateContributor(@PathVariable Long contributorId,
			@RequestBody ContributorData contributorData) {
		contributorData.setContributorId(contributorId);
		log.info("Updating contributor with ID={}", contributorId);
		return parkService.saveContributor(contributorData);
	} // end upDateContributor

// *********************** GET ALL CONTRIBUTORS ******************************************
	// /pet_park/contributor
	@GetMapping("/contributor")
	public List<ContributorData> retrieveAllContributors() {
		log.info("Retrieve all contributors called");
		return parkService.retrieveAllContributors();
	} // end retrieveAllContributors

// *********************** GET SINGLE CONTRIBUTOR ****************************************
	// /pet_park/contributor/contributorId}
	// @PathVariable takes contributorId as parameter
	@GetMapping("/contributor/{contributorId}")
	public ContributorData retrieveContributorById(@PathVariable Long contributorId) {
		log.info("Retrieving contributor with ID={}", contributorId);
		return parkService.retrieveContributorById(contributorId);
	} // end retrieveContributorById

// *********************** DELETE ALL CONTRIBUTORS -  NOT ALLOWED ************************
	// /pet_park/contributor
	@DeleteMapping("/contributor")
	public void deleteAllContributors() {
		log.info("Attempting to delete all contributors");
		throw new UnsupportedOperationException("Deleting all contributors is not allowed.");
	} // end deleteAllContributors

// *********************** DELETE SINGLE CONTRIBUTOR *************************************
	// /pet_park/contributor/contributorId}
	// @PathVariable takes contributorId as parameter
	@DeleteMapping("/contributor/{contributorId}")
	public Map<String, String> deleteContributorById(@PathVariable Long contributorId) {
		log.info("Deleting contributor with ID={}", contributorId);

		parkService.deleteContributorById(contributorId);

		return Map.of("message", "Deletion of contributor with ID=" + contributorId + " was successful.");
	} // end deleteContributorById

// *********************** POST PET PARK *************************************************
	// /pet_park/contributor/contributorId/park}
	@PostMapping("/contributor/{contributorId}/park")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetParkData savePetPark(@PathVariable Long contributorId, @RequestBody PetParkData petParkData) {
		log.info("Creating park {} for contributor with ID={}", petParkData, contributorId);

		return parkService.savePetPark(contributorId, petParkData);

	} // end savePetPark

// *********************** PUT PET PARK **************************************************
	// /pet_park/contributor/contributorId/park/parkId}
	@PutMapping("/contributor/{contributorId}/park/{parkId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PetParkData updatePetPark(@PathVariable Long contributorId, @PathVariable Long parkId,
			@RequestBody PetParkData petParkData) {
		
		petParkData.setPetParkId(parkId);
		
		log.info("Creating park {} for contributor with ID={}", petParkData, contributorId);

		return parkService.savePetPark(contributorId, petParkData);

	} // end savePetPark
	
// *********************** GET PET PARK **************************************************
	// /pet_park/contributor/contributorId/park/parkId}
	@GetMapping("/contributor/{contributorId}/park/{parkId}")
	public PetParkData retrievePetParkById(@PathVariable Long contributorId, @PathVariable Long parkId) {
		log.info("Retrieving pet park with ID={} for contributor with ID={}", parkId, contributorId);
		
		return parkService.retrievePetParkById(contributorId, parkId);
		
	} // end retrievePetParkById

} // end CLASS
