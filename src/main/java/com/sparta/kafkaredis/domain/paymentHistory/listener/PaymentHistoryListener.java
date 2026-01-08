package com.sparta.kafkaredis.domain.paymentHistory.listener;

import static com.sparta.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import com.sparta.kafkaredis.domain.paymentHistory.service.PaymentHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentHistoryListener {
	private final PaymentHistoryService paymentHistoryService;

	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "payment-history-group",
		containerFactory = "paymentHistoryKafkaListenerContainerFactory"
	)
	public void consume(PaymentCompletedEvent event) {
		log.info("결제 완료 이벤트 수신! paymentID: {}, productID: {}", event.getPaymentId(), event.getProductId());

		paymentHistoryService.savePaymentHistory(event);
	}
}
