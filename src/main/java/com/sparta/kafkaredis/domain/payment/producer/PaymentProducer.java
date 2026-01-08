package com.sparta.kafkaredis.domain.payment.producer;

import static com.sparta.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProducer {
	private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

	public void send(PaymentCompletedEvent event) {
		paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, event.getOrderId().toString(), event);

	}
}
