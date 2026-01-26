package com.currency.backend.controller;

import com.currency.backend.entity.ConversionHistory;
import com.currency.backend.service.CurrencyService;
import com.currency.backend.dto.ConversionRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    // ✅ POST /api/convert (ONLY ONE CONVERT API)
    @PostMapping("/convert")
    public ResponseEntity<ConversionHistory> convert(
            @Valid @RequestBody ConversionRequest request) {

        ConversionHistory result = service.convert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ✅ GET /api/history with pagination
    @GetMapping("/history")
    public Page<ConversionHistory> getHistory(
            @PageableDefault(size = 5, sort = "timestamp") Pageable pageable) {

        return service.getHistory(pageable);
    }
}
