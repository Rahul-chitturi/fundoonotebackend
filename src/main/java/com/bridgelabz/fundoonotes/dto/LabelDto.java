package com.bridgelabz.fundoonotes.dto;


import javax.validation.constraints.NotNull;

import com.bridgelabz.fundoonotes.model.Label;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {
	@NotNull
	private String name;
	
	public static LabelDto valueOf(Label label) {
	return  new LabelDto(label.getName());
	}
}