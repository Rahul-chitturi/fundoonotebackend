package com.bridgelabz.fundoonotes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Collaborator {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;

	private String name;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userNote;
	
}
