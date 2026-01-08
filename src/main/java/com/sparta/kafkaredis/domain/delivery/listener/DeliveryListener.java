package com.sparta.kafkaredis.domain.delivery.listener;

import static com.sparta.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import com.sparta.kafkaredis.domain.delivery.service.DeliveryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryListener {
	private final DeliveryService deliveryService;

	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "delivery-group",
		containerFactory = "deliveryKafkaListenerContainerFactory"
	)
	public void consume(PaymentCompletedEvent event) {
		log.info("결제 완료 이벤트 수신 - orderId: {}, paymentId: {}, userId: {}",
			event.getOrderId(), event.getPaymentId(), event.getUserId());

		deliveryService.createDeliveryFromPayment(event);
	}
}
