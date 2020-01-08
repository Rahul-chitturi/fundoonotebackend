package com.bridgelabz.fundoonotes.repository;

import org.springframework.data.repository.CrudRepository;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;

public interface LabelRepository extends CrudRepository<Label, Long>{

	LabelDto findOneByName(String labelName);

}