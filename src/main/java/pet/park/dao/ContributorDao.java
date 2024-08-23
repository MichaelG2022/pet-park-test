package pet.park.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.park.entity.Contributor;

public interface ContributorDao extends JpaRepository<Contributor, Long> {

	/*
	 * The name of this method is important. find by means find 1 single entity.
	 * 
	 * contributorEmail is in fact a valid Java field value in Contributor entity,
	 * so the SQL statement will written for us to find the contributor email
	 */
	Optional<Contributor> findByContributorEmail(String contributorEmail);

} // end INTERFACE
