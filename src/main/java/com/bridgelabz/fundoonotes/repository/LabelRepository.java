package com.bridgelabz.fundoonotes.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bridgelabz.fundoonotes.model.Label;

public interface LabelRepository extends CrudRepository<Label, Long>{

	@Query(value = " select * from label  where name = ?" , nativeQuery =  true)
	Label findOneByName(String labelName);

	@Modifying
	@Transactional
	@Query(value = " insert into label (name, user_id) values (?, ?) " , nativeQuery =  true)
	void insertLabelData(String name , Long userId);
	
	@Query(value = " select * from label where lable_id = ? and user_id =?" , nativeQuery =  true)
	Label findoneById(Long id , Long userId);

	@Modifying
	@Transactional
	@Query(value = " delete from label where lable_id = ? and user_id = ? " , nativeQuery =  true)
	int remove(Long lableId , Long userId);
	
	@Modifying
	@Transactional
	@Query(value = " delete from label_note where label_note_id = ? and note_id = ?  " , nativeQuery =  true)
	void removeInMapTable(Long lableId , Long noteId);

	@Modifying
	@Transactional
	@Query(value = " delete from label_note where label_note_id = ?  " , nativeQuery =  true)
	int removeInMapTablePermanently(Long lableId);

	@Modifying
	@Transactional
	@Query(value = " update table note set name = ? where label_id = ?  " , nativeQuery =  true)
	void update(String name , Long id);

//	@Query(value = " select * from label , label_note where lable_id = ? and user_id =?" , nativeQuery =  true)
//	Label findoneByLabelIdAndNoteId(Long lableId, Long noteId);
//	
	@Query(value = " select * from  label_note where label_note_id = ? and note_id =?" , nativeQuery =  true)
    Object findoneByLabelIdAndNoteId(Long lableId, Long noteId);
	
}