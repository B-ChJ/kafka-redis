package com.sparta.kafkaredis.domain.delivery.service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.sparta.kafkaredis.common.entity.Delivery;
import com.sparta.kafkaredis.common.enums.DeliveryStatus;
import com.sparta.kafkaredis.domain.delivery.model.response.DeliveryResponse;
import com.sparta.kafkaredis.domain.delivery.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryCacheService {
	private final StringRedisTemplate stringRedisTemplate;
	private final DeliveryRepository deliveryRepository;

	public void cacheDelivery(Delivery delivery) {
		String key = "delivery_status:" + delivery.getId();

		stringRedisTemplate.opsForHash().putAll(key, Map.of(
			"status", delivery.getStatus().name(),
			"orderId", delivery.getOrderId().toString(),
			"productId", delivery.getProductId().toString(),
			"statusUpdateAt", delivery.getStatusUpdateAt().toString()
		));

		String userKey = "user_deliveries:" + delivery.getUserId();

		double score = delivery.getStatusUpdateAt().toEpochSecond(ZoneOffset.UTC);

		stringRedisTemplate.opsForZSet()
			.add(userKey, delivery.getId().toString(), score);

		log.info("캐시 업데이트 - key: {}, status: {}",
			key, delivery.getStatus());
	}

	public List<DeliveryResponse> getUserDeliveries(Long userId) {
		String userKey = "user_deliveries:" + userId;

		Set<String> deliveryIdList =
			stringRedisTemplate.opsForZSet().reverseRangeByScore(userKey, 0, 19);

		if(deliveryIdList == null || deliveryIdList.isEmpty()) {
			return deliveryRepository.findByUserIdOrderByStatusUpdateAtDesc(userId)
				.stream()
				.map(d -> new DeliveryResponse(
					d.getId(),
					d.getOrderId(),
					d.getProductId(),
					d.getStatus(),
					d.getStatusUpdateAt().toString()
				))
				.collect(Collectors.toList());
		}
		List<DeliveryResponse> result = new ArrayList<>();

		for(String deliveryId : deliveryIdList) {
			String key = "delivery_status:" + deliveryId;

			Map<Object, Object> cached =
				stringRedisTemplate.opsForHash().entries(key);

			if(cached.isEmpty()) {
				Delivery delivery = deliveryRepository.findById(userId).orElseThrow();
				result.add(new DeliveryResponse(
					delivery.getId(),
					delivery.getOrderId(),
					delivery.getProductId(),
					delivery.getStatus(),
					delivery.getStatusUpdateAt().toString()
				));
				continue;
			}

			DeliveryResponse response = new DeliveryResponse(
				Long.valueOf(deliveryId),
				Long.valueOf(cached.get("orderOd").toString()),
				Long.valueOf(cached.get("productId").toString()),
				DeliveryStatus.valueOf(cached.get("status").toString()),
				cached.get("statusUpdateAt").toString()
			);

			result.add(response);
		}

		return result;
	}
}
