package com.niranjan.service;

import com.niranjan.DTO.StatsDTO;

public interface StatsService {
	
	StatsDTO getStats();
	
	StatsDTO getStatsByBank(String bankName);
	
}
