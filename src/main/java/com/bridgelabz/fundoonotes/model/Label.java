package com.bridgelabz.fundoonotes.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Label {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lableId;

	private String name;
    @JsonIgnore
	@ManyToMany(mappedBy = "labels")
	private List<Note> noteList;
	

    @JsonIgnore
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