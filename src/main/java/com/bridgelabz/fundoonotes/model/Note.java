package com.bridgelabz.fundoonotes.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "note")
@NoArgsConstructor
@Getter
@Setter
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	private String contant;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isPinned;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isArchived;
	
	
	@Column(columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	private Date createdAt;
	
	private Date updatedAt;
	
	public Note(String title, String contant) {
		super();
		this.title = title;
		this.contant = contant;
	}

	@ManyToOne
	@JoinColumn(name = "userId")
	private User userNote;
	
	@Column(columnDefinition = "varchar(10) default 'ffffff'")
	private String NoteColor;
	
	private Date localReminder;
	
	private String localReminderStatus;


	@ManyToMany
	@JoinTable(name = "Label_Note", joinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "label_Note_id", referencedColumnName = "lableId"))
	private List<Label> labels;

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setCreatedAt() {
		setUpdatedAt();
		this.createdAt = new Date();
	}


	public void setUpdatedAt() {
		this.updatedAt = new Date();
	}
}
