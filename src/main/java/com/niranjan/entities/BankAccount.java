package com.niranjan.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BankAccount {

	@Id
	private String id;
	private String bankName;
	private String accountNumber;
	private Double balance;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
	
	 @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL,orphanRemoval = true)
	 @JsonIgnore
	 private List<Income> incomes;

	 @OneToMany(mappedBy = "bankAccount",cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnore
	 private List<Expense> expenses;
}
