package com.sparta.kafkaredis.common.model.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingDto {
	private String title;
	private double score;
}
