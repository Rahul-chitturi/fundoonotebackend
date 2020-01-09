package com.bridgelabz.fundoonotes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Label {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long labelId;

	private String name;


//	private List<NoteModel> noteList;
	
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userLabel;

//	public List<NoteModel> getNoteList() {
//		return noteList;
//	}
//
//	public void setNoteList(List<NoteModel> noteList) {
//		this.noteList = noteList;
//	}

}