package com.sparta.kafkaredis.domain.paymentHistory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.kafkaredis.common.entity.PaymentHistory;
import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import com.sparta.kafkaredis.domain.paymentHistory.repository.PaymentHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentHistoryService {
	private final PaymentHistoryRepository paymentHistoryRepository;

	@Transactional
	public void savePaymentHistory(PaymentCompletedEvent event) {
		PaymentHistory paymentHistory = PaymentHistory.from(event);

		paymentHistoryRepository.save(paymentHistory);
		log.info("DB에 결제 기록 저장 완료! - paymentId: {}, productId: {}", event.getPaymentId(), event.getProductId());
	}
}
