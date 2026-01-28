package com.currency.backend.service;

import com.currency.backend.dto.ConversionRequest;
import com.currency.backend.entity.ConversionHistory;
import com.currency.backend.repository.ConversionHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 🔹 MAIN CONVERSION
    public ConversionHistory convert(String from, String to, double amount) {

        double rate = getConversionRate(from, to);
        double convertedAmount = amount * rate;

        ConversionHistory history = new ConversionHistory();
        history.setFromCurrency(from);
        history.setToCurrency(to);
        history.setAmount(amount);
        history.setRate(rate);
        history.setConvertedAmount(convertedAmount);
        history.setTimestamp(LocalDateTime.now());

        return repository.save(history);
    }

    // 🔹 DTO VERSION
    public ConversionHistory convert(ConversionRequest request) {
        return convert(
                request.getFrom(),
                request.getTo(),
                request.getAmount()
        );
    }

    // 🔹 PAGINATED HISTORY
    public Page<ConversionHistory> getHistory(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // 🔹 GET CONVERSION RATE FROM API
    private double getConversionRate(String from, String to) {
        // Call external API
        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class, from);

        if (response == null || !response.containsKey("rates")) {
            throw new RuntimeException("Failed to fetch conversion rates from API");
        }

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        if (!rates.containsKey(to)) {
            throw new RuntimeException("Target currency not found: " + to);
        }

        return rates.get(to);
    }
}
