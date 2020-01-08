package com.bridgelabz.fundoonotes.dto;
/**
 * Lable Dto for lable model
 * @author user
 * @version 1.0
 * @Date 2020-01-08
 */
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data

public class LabelDto {
	@NotBlank
	private String name;
}