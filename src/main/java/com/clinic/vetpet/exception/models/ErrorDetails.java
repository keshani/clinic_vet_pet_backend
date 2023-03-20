package com.clinic.vetpet.exception.models;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private Instant timestamp;
    private String message;
    private String details;
}
