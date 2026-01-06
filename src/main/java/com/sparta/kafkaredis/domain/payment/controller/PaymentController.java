package com.sparta.kafkaredis.domain.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.kafkaredis.domain.payment.model.request.CompletePaymentRequest;
import com.sparta.kafkaredis.domain.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping("/completion")
	public ResponseEntity<Void> paymentCompleted(@RequestBody CompletePaymentRequest request) {
		paymentService.paymentComplete(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
