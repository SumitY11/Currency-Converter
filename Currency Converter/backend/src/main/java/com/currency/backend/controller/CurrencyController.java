package com.currency.backend.controller;

import com.currency.backend.entity.ConversionHistory;
import com.currency.backend.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping("/convert")
    public ConversionHistory convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount
    ) {

        return service.convert(from, to, amount);
    }
}
