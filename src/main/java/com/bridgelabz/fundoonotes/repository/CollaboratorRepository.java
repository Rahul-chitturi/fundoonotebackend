package com.bridgelabz.fundoonotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.Collaborator;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long>{
	
	@Query(value = "select * from collaborator where email = ? " ,  nativeQuery = true)
	Collaborator findOneByEmail(String email);

}
