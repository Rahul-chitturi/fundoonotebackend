package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.Collaborator;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long>{
	
	@Query(value = "select * from collaborator where email = ? " ,  nativeQuery = true)
	Collaborator findOneByEmail(String email);
	
	@Modifying
	@Transactional
	@Query(value =  "delete from collaborator where id = ?" , nativeQuery =  true )
	 int deleteOneById(Long cId);
	
	@Query(value = " select * from collaborator where noteid = ?" , nativeQuery = true)
	List<Collaborator> getAllNoteCollaborators( Long noteId);
	
}
