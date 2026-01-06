package com.sparta.kafkaredis.domain.payment.model.request;

import com.sparta.kafkaredis.common.enums.Category;

import lombok.Getter;

@Getter
public class CompletePaymentRequest {

	private Long orderId;
	private Long paymentId;
	private Long productId;
	private Long userId;
	private Category category;
	private int quantity;
}