package com.dev.patientpractice.service;

import com.dev.patientpractice.dto.request.visit.VisitCreationRequest;
import com.dev.patientpractice.dto.request.visit.VisitModificationRequest;
import com.dev.patientpractice.dto.response.visit.VisitInquiryResponse;
import com.dev.patientpractice.entity.Hospital;
import com.dev.patientpractice.entity.Patient;
import com.dev.patientpractice.entity.Visit;
import com.dev.patientpractice.exception.ErrorCode;
import com.dev.patientpractice.exception.PatientApplicationException;
import com.dev.patientpractice.repository.HospitalRepository;
import com.dev.patientpractice.repository.PatientRepository;
import com.dev.patientpractice.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VisitService {

    public static final String VISIT_STATUS_CODE = "방문상태코드";
    private final CodeService codeService;
    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    @Transactional
    public void createVisit(VisitCreationRequest body) {
        codeService.checkCode(VISIT_STATUS_CODE, body.getVisitStatusCode());

        Hospital hospital = hospitalRepository.findByIdAndDeleted(body.getHospitalId(), false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.HOSPITAL_NOT_FOUND, String.format("병원 ID: %s", body.getHospitalId())));

        Patient patient = patientRepository.findByIdAndDeleted(body.getPatientId(), false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자 ID: %s", body.getPatientId())));

        Visit visit = body.toEntity(hospital, patient);
        visitRepository.save(visit);
    }

    @Transactional
    public void deleteVisit(Long visitId) {
        Visit visit = visitRepository.findByIdAndVisitStatusCodeIn(visitId, List.of("1", "2"))
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자방문 ID: %s", visitId)));
        visit.delete();
    }

    @Transactional
    public void updateVisit(Long visitId, VisitModificationRequest body) {
        if (isExistsVisitStatusCode(body)) {
            codeService.checkCode(VISIT_STATUS_CODE, body.getVisitStatusCode());
        }

        Visit visit = visitRepository.findByIdAndVisitStatusCodeIn(visitId, List.of("1", "2"))
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.VISIT_NOT_FOUND, String.format("환자방문 ID: %s", visitId)));
        visit.update(body);
    }

    public boolean isExistsVisitStatusCode(VisitModificationRequest body) {
        return StringUtils.hasText(body.getVisitStatusCode());
    }

    @Transactional(readOnly = true)
    public VisitInquiryResponse getVisits(Long patientId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").descending());
        Page<Visit> visits = visitRepository.findAllByPatient_Id(patientId, pageable);
        return VisitInquiryResponse.of(visits.getContent(), pageable, visits.getTotalElements());
    }
}
