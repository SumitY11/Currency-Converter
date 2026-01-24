package com.currency.backend.repository;

import com.currency.backend.entity.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionHistoryRepository
        extends JpaRepository<ConversionHistory, Long> {
}