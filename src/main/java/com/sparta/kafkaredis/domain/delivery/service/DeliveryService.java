package com.sparta.kafkaredis.domain.delivery.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.sparta.kafkaredis.common.entity.Delivery;
import com.sparta.kafkaredis.common.enums.DeliveryStatus;
import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import com.sparta.kafkaredis.domain.delivery.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
	private final DeliveryRepository deliveryRepository;
	private final DeliveryCacheService deliveryCacheService;

	public void createDeliveryFromPayment(PaymentCompletedEvent event) {
		LocalDateTime paidAt = LocalDateTime.parse(
			event.getPaidAt(),
			DateTimeFormatter.ISO_LOCAL_DATE_TIME
		);

		Delivery delivery = Delivery.builder()
			.orderId(event.getOrderId())
			.paymentId(event.getPaymentId())
			.productId(event.getProductId())
			.userId(event.getUserId())
			.status(DeliveryStatus.PREPARING)
			.paidAt(paidAt)
			.statusUpdateAt(paidAt)
			.build();

		Delivery saved = deliveryRepository.save(delivery);

		log.info("배송 준비 생성 - orderId: {}, status: {}", saved.getOrderId(), saved.getStatus());

		deliveryCacheService.cacheDelivery(saved);
	}
}
