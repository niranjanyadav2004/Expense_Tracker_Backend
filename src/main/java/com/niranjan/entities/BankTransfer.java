package com.niranjan.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankTransfer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_bank_account_id", nullable = false)
	@JsonIgnore
	private BankAccount fromBank;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_bank_account_id", nullable = false)
	@JsonIgnore
	private BankAccount toBank;

    private Double amount;

    private LocalDate date;

    private String description;
	
}
