package com.sparta.kafkaredis.common.entity;

import java.time.LocalDateTime;

import com.sparta.kafkaredis.common.enums.DeliveryStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderId;
	private Long paymentId;
	private Long productId;
	private Long userId;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	private LocalDateTime paidAt;
	private LocalDateTime statusUpdateAt;

	public void markShipping(LocalDateTime now) {
		this.status = DeliveryStatus.SHIPPING;
		this.statusUpdateAt = now;
	}

	public void markCompleted(LocalDateTime now) {
		this.status = DeliveryStatus.COMPLETED;
		this.statusUpdateAt = now;
	}
}
