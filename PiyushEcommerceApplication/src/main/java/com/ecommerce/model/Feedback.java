package com.ecommerce.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToOne;
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer feedbackId;

	@Min(value = 1,message = "{feedback.rating.invalid}")
	@Max(value = 5,message = "{feedback.rating.invalid}")
	private Integer productRating;
	
	@Min(value = 1,message = "{feedback.rating.invalid}")
	@Max(value = 5,message = "{feedback.rating.invalid}")
	private Integer deliveryRating;
	
	@Min(value = 1,message = "{feedback.rating.invalid}")
	@Max(value = 5,message = "{feedback.rating.invalid}")
	private Integer overallRating;
	
	@NotNull(message = "Comment should not be null")
	@NotBlank(message = "Comment should not be blank...!")
	private String comments;
	
	@JsonIgnore
	private LocalDate feedbackdate;
	
	@JsonIgnore
	private String status;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	private Customer customer;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	private Order order;

}
