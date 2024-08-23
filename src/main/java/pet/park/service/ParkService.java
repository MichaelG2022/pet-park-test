package pet.park.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.park.controller.model.ContributorData;
import pet.park.controller.model.PetParkData;
import pet.park.dao.AmenityDao;
import pet.park.dao.ContributorDao;
import pet.park.dao.PetParkDao;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.PetPark;

@Service
public class ParkService {

	@Autowired
	private ContributorDao contributorDao;

	@Autowired
	private AmenityDao amenityDao;

	@Autowired
	private PetParkDao petParkDao;

	@Transactional(readOnly = false)
	public ContributorData saveContributor(ContributorData contributorData) {
		// Need to create an empty Contributor (entity) object, or find one in the DB.
		// First step is to check if contributorId is null or not.
		Long contributorId = contributorData.getContributorId();
		// instantiate contributor object
		Contributor contributor = findOrCreateContributor(contributorId, contributorData.getContributorEmail());

		// passes in contributor as destination and contributorData as source
		/*
		 * new ContributorData is a method in ContributorData that converts a
		 * contributor object into a contributorData object.
		 */
		setFieldsInContributor(contributor, contributorData);

		return new ContributorData(contributorDao.save(contributor));

	} // end saveContributor

	private void setFieldsInContributor(Contributor contributor, ContributorData contributorData) {
		contributor.setContributorEmail(contributorData.getContributorEmail());
		contributor.setContributorName(contributorData.getContributorName());
	} // end setFieldsInContributor

	private Contributor findOrCreateContributor(Long contributorId, String contributorEmail) {
		// instantiate contributor object
		Contributor contributor;

		// if contributorId is null, create a new contributor, otherwise find the
		// correct object in the DB
		// Use contributorDao to find if contributor email already exists
		// To do that we must create a custom method in contributorDao
		if (Objects.isNull(contributorId)) {
			Optional<Contributor> opContrib = contributorDao.findByContributorEmail(contributorEmail);

			if (opContrib.isPresent()) {
				throw new DuplicateKeyException("Contributor with email " + contributorEmail + " already exists.");
			} // end IF

			contributor = new Contributor();
		} else {
			contributor = findContributorById(contributorId);
		}
		return contributor;
	} // end findOrCreateContributor

	// Use a DAO interface that extends an interface in JPA
	// use empty Lambda for Optional
	@Transactional(readOnly = true)
	private Contributor findContributorById(Long contributorId) {
		// @formatter:off
		return contributorDao.findById(contributorId)
			.orElseThrow(() -> new NoSuchElementException(
				"Contributor with ID=" + contributorId + " was not found."));
		// @formatter:on
	} // end findContributorById

// ****************** STANDARD METHOD FOR GET ************************************
//	@Transactional(readOnly = true)
//	public List<ContributorData> retrieveAllContributors() {
//		List<Contributor> contributors = contributorDao.findAll();
//		List<ContributorData> response = new LinkedList<>();
//		
//		// convert list of contributor entities into a list of contributor data 
//		for(Contributor contributor : contributors) {
//			response.add(new ContributorData(contributor));
//		}		
//		return response;
//	} // end retrieveAllContributors

// ****************** STREAMING METHOD FOR GET ************************************
	// provides a stream of contributor and turns it into a stream of contributor
	// data
	@Transactional(readOnly = true)
	public List<ContributorData> retrieveAllContributors() {
		// @formatter:off
		return contributorDao.findAll()
			.stream()
			//.map(cont -> new ContributorData(cont))
			// using constructor method reference instead of Lambda
			.map(ContributorData::new)
			.toList();		
		// @formatter:on
	} // end retrieveAllContributors

	// makes use of findContributorById method written for week 13
	@Transactional(readOnly = true)
	public ContributorData retrieveContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		return new ContributorData(contributor);
	} // end retrieveContributorById

	/*
	 * First have to find contributor by ID.
	 * 
	 * Then call delete method in Dao
	 */
	@Transactional(readOnly = false)
	public void deleteContributorById(Long contributorId) {
		Contributor contributor = findContributorById(contributorId);
		contributorDao.delete(contributor);
	} // end deleteContributorById

	@Transactional(readOnly = false)
	public PetParkData savePetPark(Long contributorId, PetParkData petParkData) {
		Contributor contributor = findContributorById(contributorId);

		// Had to create AmenityDao and Autowire it at the top
		Set<Amenity> amenities = amenityDao.findAllByAmenityIn(petParkData.getAmenities());

		PetPark petPark = findOrCreatePetPark(petParkData.getPetParkId());
		// Set fields in petPark as target using petParkData as source
		setParkParkFields(petPark, petParkData);
		
		// Set relationships - Pet park to contributor and contributor to pet park
		petPark.setContributor(contributor);
		contributor.getPetParks().add(petPark);
		
		//Set relationships - Amenity to Pet park and Pet park to Amenity
		for(Amenity amenity : amenities ) {
			amenity.getPetParks().add(petPark);
			petPark.getAmenities().add(amenity);
		} // end FOR
		
		// Create object to return
		PetPark dbPetPark = petParkDao.save(petPark);
		
		return new PetParkData(dbPetPark);
	} // end savePetPark

	private void setParkParkFields(PetPark petPark, PetParkData petParkData) {
		petPark.setCountry(petParkData.getCountry());
		petPark.setDirections(petParkData.getDirections());
		petPark.setGeoLocation(petParkData.getGeoLocation());
		petPark.setParkName(petParkData.getParkName());
		petPark.setPetParkId(petParkData.getPetParkId());
		petPark.setStateOrProvince(petParkData.getStateOrProvince());
		
	} // end setParkParkFields

	private PetPark findOrCreatePetPark(Long petParkId) {
		// instantiate petPark object
		PetPark petPark;

		if (Objects.isNull(petParkId)) {
			petPark = new PetPark();
		} else {
			petPark = findPetParkById(petParkId);
		}
		return petPark;
	} // end findOrCreatePetPark

	// Had to create PetParkDao and Autowire it at the top
	private PetPark findPetParkById(Long petParkId) {
		return petParkDao.findById(petParkId)
				.orElseThrow(() -> new NoSuchElementException("Pet park with ID=" + petParkId + " does not exist."));
	} // end findPetParkById

	@Transactional(readOnly = true)
	public PetParkData retrievePetParkById(Long contributorId, Long parkId) {
		// validate contributor ID and pet park ID
		findContributorById(contributorId);
		PetPark petPark = findPetParkById(parkId);
		
		// check to make sure contributor ID matches the ID in pet park
		if(petPark.getContributor().getContributorId() != contributorId) {
			throw new IllegalStateException("Pet park with ID=" + parkId + " is not owned by contributor with ID=" + contributorId);			
		} // end IF
		
		return new PetParkData(petPark);
	} // end retrievePetParkById

} // end CLASS
