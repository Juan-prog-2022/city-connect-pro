package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.*;
import com.bluesoftware.city_connect_pro.entities.CurrencyType;
import com.bluesoftware.city_connect_pro.entities.Professional;
import com.bluesoftware.city_connect_pro.entities.Specialty;

public class ProfessionalMapper {

    public static ProfessionalResponseDTO toResponse(Professional p) {
        return ProfessionalResponseDTO.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .skills(p.getSkills())
                .certifications(p.getCertifications())
                .city(p.getCity())
                .address(p.getAddress())
                .latitude(p.getLatitude())
                .longitude(p.getLongitude())
                .hourlyRate(p.getHourlyRate())
                .currency(p.getCurrency() != null ? p.getCurrency().name() : null)
                .yearsOfExperience(p.getYearsOfExperience())
                .averageRating(p.getAverageRating())
                .reviewCount(p.getReviewCount())
                .specialty(p.getSpecialty() != null ? p.getSpecialty().name() : null)
                .build();
    }

    public static Professional toEntity(ProfessionalRequestDTO request) {
        Professional p = new Professional();
        p.setFirstName(request.getFirstName());
        p.setLastName(request.getLastName());
        p.setDni(request.getDni());
        p.setEmail(request.getEmail());
        p.setPhone(request.getPhone());
        p.setSkills(request.getSkills());
        p.setCertifications(request.getCertifications());
        p.setCity(request.getCity());
        p.setAddress(request.getAddress());
        p.setLatitude(request.getLatitude());
        p.setLongitude(request.getLongitude());
        p.setHourlyRate(request.getHourlyRate());
        p.setYearsOfExperience(request.getYearsOfExperience());

        if (request.getCurrency() != null && !request.getCurrency().isBlank()) {
            p.setCurrency(CurrencyType.valueOf(request.getCurrency().toUpperCase()));
        } else {
            p.setCurrency(CurrencyType.ARS);
        }

        if (request.getSpecialty() != null && !request.getSpecialty().isBlank()) {
            p.setSpecialty(Specialty.valueOf(request.getSpecialty().toUpperCase()));
        }

        return p;
    }
}