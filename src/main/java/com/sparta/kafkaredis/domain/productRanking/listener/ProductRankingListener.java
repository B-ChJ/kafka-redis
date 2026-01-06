package com.sparta.kafkaredis.domain.productRanking.listener;

import static com.sparta.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sparta.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import com.sparta.kafkaredis.domain.productRanking.service.ProductRankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductRankingListener {
	private final ProductRankingService productRankingService;

	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "product-ranking-group",
		containerFactory = "paymentKafkaListenerContainerFactory"
	)
	public void consumer(PaymentCompletedEvent event) {
		log.info("성공적으로 값을 가져왔습니다. : [상품 랭킹 조회 리스너]");

		LocalDateTime paidAt = LocalDateTime.parse(event.getPaidAt());
		LocalDate currentDate = paidAt.toLocalDate();

		productRankingService.increaseProductRanking(event.getProductId(), currentDate);

	}
}
