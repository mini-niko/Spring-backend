package br.minikoapi.dtos;

import org.springframework.http.HttpStatus;

public record ExceptionDTO(String status, String message) {
}
