package com.medimind.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingRequest {
    @NotNull
    private UUID doctorId;
    
    @NotNull
    @Future
    private LocalDateTime slotStartTime;
    
    private String symptoms;
}
