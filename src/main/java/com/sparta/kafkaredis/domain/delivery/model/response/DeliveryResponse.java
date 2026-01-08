package com.sparta.kafkaredis.domain.delivery.model.response;

import com.sparta.kafkaredis.common.enums.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryResponse {
	private Long deliveryId;
	private Long orderId;
	private Long productId;
	private DeliveryStatus deliveryStatus;
	private String statusUpdateAt;

}
