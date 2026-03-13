package com.niranjan.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransferDTO {

    private String fromBankName;
    private String toBankName;
    private Double amount;
    private String description;
    private LocalDate date;

}
