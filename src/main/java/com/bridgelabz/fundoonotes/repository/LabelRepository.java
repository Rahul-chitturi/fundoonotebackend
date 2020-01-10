package com.bridgelabz.fundoonotes.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bridgelabz.fundoonotes.model.Label;

public interface LabelRepository extends CrudRepository<Label, Long>{

	@Query(value = " select * from label where name = ?" , nativeQuery =  true)
	Label findOneByName(String labelName);

	@Query(value = " insert into label (name, user_id) values (?, ?) " , nativeQuery =  true)
	void insertLabelData(String name , Long userId);
	
}