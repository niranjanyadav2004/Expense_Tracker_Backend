package com.niranjan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niranjan.service.StatsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StatsController {

	@Autowired
	private final StatsService service;
	

	@GetMapping("/overall")
    public ResponseEntity<?> getOverallStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @GetMapping("/bank/{bankName}")
    public ResponseEntity<?> getStatsByBank(@PathVariable String bankName) {
        return ResponseEntity.ok(service.getStatsByBank(bankName));
    }
	
}
