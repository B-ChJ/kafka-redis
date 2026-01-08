package com.sparta.kafkaredis.domain.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.kafkaredis.common.entity.Delivery;
import com.sparta.kafkaredis.common.enums.DeliveryStatus;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	List<Delivery> findByUserIdOrderByStatusUpdateAtDesc(Long userId);

	List<Delivery> findByStatusAndStatusUpdateAtBefore(DeliveryStatus status, LocalDateTime dateTime);
}
