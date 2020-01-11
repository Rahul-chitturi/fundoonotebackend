package com.bridgelabz.fundoonotes.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "note")
@NoArgsConstructor
@Getter
@Setter
@SqlResultSetMapping(name = "name",
classes = @ConstructorResult(
        targetClass = Note.class,
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "title"),
            @ColumnResult(name = "contant"),
            @ColumnResult(name = "is_pinned"),
            @ColumnResult(name = "is_archived"),
            @ColumnResult(name = "is_deleted"),
            @ColumnResult(name = "local_reminder"),
            @ColumnResult(name = "local_reminder_status"),
            @ColumnResult(name = "updated_at"),
            @ColumnResult(name = "note_color")
            }))
@NamedNativeQuery(name = "name",resultClass = Note.class ,query = "select * from note left join label_note on note.id = label_note.note_id where  label_note_id = ?" , resultSetMapping = "name")
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
	@JsonIgnore
	private Date createdAt;
	
	private Date updatedAt;
	
	public Note(String title, String contant) {
		super();
		this.title = title;
		this.contant = contant;
	}

@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userNote;
	
	@Column(columnDefinition = "varchar(10) default 'ffffff'")
	private String noteColor;
	
	private Date localReminder;
	
	private String localReminderStatus;

@JsonIgnore
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

	public Note(long id, String title, String contant, boolean isPinned, boolean isArchived, boolean isDeleted, Date updatedAt, String noteColor, Date localReminder, String localReminderStatus) {
		super();
		this.id = id;
		this.title = title;
		this.contant = contant;
		this.isPinned = isPinned;
		this.isArchived = isArchived;
		this.isDeleted = isDeleted;
		this.updatedAt = updatedAt;
		this.noteColor = noteColor;
		this.localReminder = localReminder;
		this.localReminderStatus = localReminderStatus;
	}

}
