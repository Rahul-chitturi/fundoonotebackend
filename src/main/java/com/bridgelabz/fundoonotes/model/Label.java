package com.bridgelabz.fundoonotes.model;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;

import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(name = "tofindlabelfornote",
classes = @ConstructorResult(
        targetClass = Label.class,
        columns = {
            @ColumnResult(name = "lable_id", type = Long.class),
            @ColumnResult(name = "name"),
            }))
@NamedNativeQuery(name = "tofindlabelfornote",resultClass = Note.class ,query = "select * from label where lable_id in (select label_note_id from label_note where note_id = ?)" , resultSetMapping = "tofindlabelfornote")
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


	public Label(Long lableId, String name) {
		super();
		this.lableId = lableId;
		this.name = name;
	}



}