package com.sparta.kafkaredis.domain.productRanking.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.sparta.kafkaredis.common.model.redis.RankingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {
	private final StringRedisTemplate stringRedisTemplate;

	public static final String PRODUCT_RANKING_DAILY_KEY = "product:ranking:";

	public void increaseProductRanking(long productId, LocalDate currentDate) {
		String key = PRODUCT_RANKING_DAILY_KEY + currentDate;

		stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(productId), 1);
	}

	public List<RankingDto> findProductRankingTop3InToday() {
		LocalDate currentDate = LocalDate.now();

		String key = PRODUCT_RANKING_DAILY_KEY + currentDate;

		Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
			.reverseRangeWithScores(key, 0, 2);

		if(result == null) {
			return Collections.emptyList();
		}

		return result.stream()
			.map(tuple -> new RankingDto(tuple.getValue(), tuple.getScore()))
			.toList();
	}
}
