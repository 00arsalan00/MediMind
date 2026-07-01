package com.medimind.service;

import com.medimind.dto.BookingRequest;
import com.medimind.dto.SlotResponse;
import com.medimind.entity.*;
import com.medimind.exception.SlotAlreadyBookedException;
import com.medimind.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorLeaveRepository doctorLeaveRepository;

    public List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date) {
        DoctorProfile doctor = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctorLeaveRepository.findByDoctorAndLeaveDate(doctor, date).isPresent()) {
            return new ArrayList<>();
        }

        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(LocalTime.MAX);
        List<Appointment> existingAppointments = appointmentRepository.findAllByDoctorAndAppointmentDateTimeBetween(doctor, dayStart, dayEnd);
        
        List<LocalDateTime> bookedTimes = existingAppointments.stream()
                .map(Appointment::getAppointmentDateTime)
                .collect(Collectors.toList());

        List<SlotResponse> slots = new ArrayList<>();
        LocalTime current = doctor.getWorkingHoursStart();
        int duration = doctor.getSlotDurationMinutes() != null ? doctor.getSlotDurationMinutes() : 30;

        while (current.plusMinutes(duration).isBefore(doctor.getWorkingHoursEnd()) || current.plusMinutes(duration).equals(doctor.getWorkingHoursEnd())) {
            LocalDateTime slotStart = LocalDateTime.of(date, current);
            boolean isBooked = bookedTimes.contains(slotStart);
            
            slots.add(SlotResponse.builder()
                    .startTime(slotStart)
                    .endTime(slotStart.plusMinutes(duration))
                    .available(!isBooked)
                    .build());
            
            current = current.plusMinutes(duration);
        }

        return slots;
    }

    @Transactional
    public Appointment bookAppointment(User user, BookingRequest request) {
        // 1. Fetch Profiles
        PatientProfile patient = patientProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));
        
        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        appointmentRepository.findByDoctorAndDateTimeWithLock(doctor.getId(), request.getSlotStartTime())
                .ifPresent(a -> {
                    throw new SlotAlreadyBookedException("This slot is already booked");
                });

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(request.getSlotStartTime())
                .patientSymptoms(request.getSymptoms())
                .status(AppointmentStatus.PENDING)
                .build();

        return appointmentRepository.save(appointment);
    }
}
