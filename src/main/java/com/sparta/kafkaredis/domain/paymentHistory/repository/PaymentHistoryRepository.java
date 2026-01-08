package com.sparta.kafkaredis.domain.paymentHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.kafkaredis.common.entity.PaymentHistory;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
