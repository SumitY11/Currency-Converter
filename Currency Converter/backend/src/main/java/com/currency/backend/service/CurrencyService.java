package com.currency.backend.service;

import com.currency.backend.entity.ConversionHistory;
import com.currency.backend.repository.ConversionHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CurrencyService {

    private final ConversionHistoryRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL =
            "https://api.exchangerate-api.com/v4/latest/{base}";

    public CurrencyService(ConversionHistoryRepository repository) {
        this.repository = repository;
    }

    public ConversionHistory convert(
            String from,
            String to,
            double amount
    ) {

        // 1️⃣ Call external API
        Map response = restTemplate.getForObject(
                API_URL,
                Map.class,
                from
        );

        Map<String, Double> rates =
                (Map<String, Double>) response.get("rates");

        double rate = rates.get(to);
        double convertedAmount = amount * rate;

        // 2️⃣ Save conversion
        ConversionHistory history = new ConversionHistory();
        history.setFromCurrency(from);
        history.setToCurrency(to);
        history.setAmount(amount);
        history.setRate(rate);
        history.setConvertedAmount(convertedAmount);
        history.setTimestamp(LocalDateTime.now());

        return repository.save(history);
    }
}
