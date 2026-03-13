package com.niranjan.service;

import java.util.List;

import com.niranjan.DTO.TransferDTO;
import com.niranjan.DTO.TransferResponse;

public interface BankTransferService {

	void transferAmount(TransferDTO transferDTO);
	List<TransferResponse> getTransfersByBankName(String bankName);
	List<TransferResponse> getAllTranfer();
	TransferResponse updateTransfer(Long id, TransferDTO transferDTO);
	void deleteTransfer(Long id);
}
