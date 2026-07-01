package com.medimind.controller;

import com.medimind.dto.BookingRequest;
import com.medimind.dto.SlotResponse;
import com.medimind.entity.Appointment;
import com.medimind.entity.User;
import com.medimind.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<List<SlotResponse>> getSlots(
            @PathVariable UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bookingService.getAvailableSlots(doctorId, date));
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> book(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.bookAppointment(user, request));
    }
}
