package com.sparta.kafkaredis.domain.payment.producer;

import static com.sparta.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;

import lombok.RequiredArgsConstructor;

/**
 * Kafka Producer 설정 클래스입니다.
 * <p>
 * 기존 코드에서 따로 지정하지 않았던 파티셔닝 전략을
 * 주문 번호(orderId)를 기준으로 파티션이 나눠 전달되도록
 * 두 번째 인수로 추가했습니다.
 * </p>
 *
 * @author 변채주
 * @version 1.0
 * @since 2026. 01. 08.
 */
@Service
@RequiredArgsConstructor
public class PaymentProducer {
	private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

	public void send(PaymentCompletedEvent event) {
		paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, event.getOrderId().toString(), event);

	}
}
