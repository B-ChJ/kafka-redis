package com.sparta.kafkaredis.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.kafkaredis.common.entity.Delivery;
import com.sparta.kafkaredis.common.enums.DeliveryStatus;
import com.sparta.kafkaredis.domain.delivery.repository.DeliveryRepository;
import com.sparta.kafkaredis.domain.delivery.service.DeliveryCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryStatusScheduler {
	private final DeliveryRepository deliveryRepository;
	private final DeliveryCacheService deliveryCacheService;

	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void updatePreparingToShipping() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime threshold = now.minusSeconds(10);

		List<Delivery> preparingList = deliveryRepository.findByStatusAndStatusUpdateAtBefore(
			DeliveryStatus.PREPARING,
			threshold
		);

		if(preparingList.isEmpty()) {
			return;
		}

		log.info("PREPARING to SHIPPING 전환 대상 : {} 건", preparingList.size());

		for(Delivery delivery : preparingList) {
			delivery.markShipping(now);
			deliveryRepository.save(delivery);
			deliveryCacheService.cacheDelivery(delivery);
		}
	}

	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void updateShippingToCompleted() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime threshold = now.minusSeconds(10);

		List<Delivery> preparingList = deliveryRepository.findByStatusAndStatusUpdateAtBefore(
			DeliveryStatus.SHIPPING,
			threshold
		);

		if(preparingList.isEmpty()) {
			return;
		}

		log.info("SHIPPING to COMPLETED 전환 대상 : {} 건", preparingList.size());

		for(Delivery delivery : preparingList) {
			delivery.markCompleted(now);
			deliveryRepository.save(delivery);
			deliveryCacheService.cacheDelivery(delivery);
		}
	}
}
