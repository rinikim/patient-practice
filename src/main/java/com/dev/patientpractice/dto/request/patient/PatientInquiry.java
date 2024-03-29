package com.dev.patientpractice.dto.request.patient;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class PatientInquiry {
    private String name;  // 환자명
    private String registrationNumber;  // 환자등록번호
    private String genderCode;  // 성별
    private String birthDate;  // 생년월일
    private String phoneNumber;  // 휴대전화번호
    private LocalDate latestVisit;  // 최근방문

    public PatientInquiry(String name, String registrationNumber, String genderCode, String birthDate, String phoneNumber, LocalDateTime latestVisit) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.latestVisit = Objects.nonNull(latestVisit) ? latestVisit.toLocalDate() : null;
    }
}