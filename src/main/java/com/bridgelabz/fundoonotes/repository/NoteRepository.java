package com.bridgelabz.fundoonotes.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;


public interface NoteRepository extends JpaRepository<Note, Long>{

	@Modifying
	@Transactional
	@Query(value = "insert into note (contant, created_at,  title, updated_at, user_id) values ( ?, ?, ?, ?, ?)" , nativeQuery = true)
	int insertData(String contant ,  Date createdAt  , String name , Date updatedAt , long id);
	
	@Modifying
	@Transactional
	@Query(value = "update note set note_color = ? where user_id = ?  AND id = ?" , nativeQuery = true)
    void updateColor(String color , long userId , long noteId);
	

	@Query(value = "select * from note where id = ?", nativeQuery = true)
	Note checkById(long id);
	
	@Query(value = "select * from note where id = ? and user_id = ?", nativeQuery = true)
	Note FindByNotedIdAndUserId(long noteId, long userId);
	
	@Modifying
	@Transactional
	@Query(value = "update note set is_pinned = ? where user_id = ? AND id = ?" ,  nativeQuery = true)
	void setPinned(boolean b, long userId, long noteId);

	@Modifying
	@Transactional
	@Query(value = "update note set is_archived = ? where user_id = ? AND id = ?" ,  nativeQuery = true)
	void setArchive(boolean b, long userId, long noteId);

	@Modifying
	@Transactional
	@Query(value = "update note set is_deleted = ? where user_id = ? AND id = ?" ,  nativeQuery = true)
	void setDelete(boolean b, long userId, long noteId);

	@Modifying
	@Transactional
	@Query(value = "delete from note  where user_id = ? AND id = ?" ,  nativeQuery = true)
	void deleteForever(long userId, long noteId);

	@Modifying
	@Transactional
	@Query(value = "update note set contant = ? , title = ? , updated_at = ? where user_id = ? AND id = ?" ,  nativeQuery = true)
	void updateData(String contant, String title, Date updatedAt , long userId, long noteId );

	@Modifying
	@Transactional
	@Query(value = "update note set local_reminder_status = ? , local_reminder = ? , updated_at = ? where user_id = ? AND id = ?" ,  nativeQuery = true)
	void reminder(String localReminderStatus, Date localReminder, Date updatedAt, long id, long noteId);

	@Modifying
	@Transactional
	@Query(value = "insert into label_note(note_id ,  label_note_id)  values(?, ?)" ,  nativeQuery = true )
	int insertDataToMap(Long noteId  , Long labelNoteId  );
	
	@Query(value = "select * from note where user_id = ?", nativeQuery = true)
	List<Note> findAll(Long userId);
	
	@Query(value = "select * from note where user_id = ? order by title DESC ", nativeQuery = true)
	List<Note> findAllInDesc(Long userId );

	@Query(name="tofindlabelfornote", nativeQuery = true)
	List<Label> getLabelByNoteId(long noteId);

	@Query(value = "select * from note where user_id = ? order by updated_at Desc", nativeQuery = true)
	List<Note> findAllInDescbyDate(Long userId); 
	
	@Query(value = "select * from note where user_id = ? order by updated_at ", nativeQuery = true)
	List<Note> findAllInbyDate(Long userId); 
	
	@Query(value = "select * from note where title = ?", nativeQuery = true)
	Note findbytitle(String title);
}
