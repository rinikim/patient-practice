package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.visit.VisitCreationRequest;
import com.dev.patientpractice.dto.request.visit.VisitModificationRequest;
import com.dev.patientpractice.dto.response.Response;
import com.dev.patientpractice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/v1/visits")
@RestController
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public Response createVisit(@RequestBody @Valid VisitCreationRequest body) {
        visitService.createVisit(body);
        return Response.success(null);
    }

    @PatchMapping("/{visitId}")
    public Response updateVisit(@PathVariable Long visitId,
                                @RequestBody @Valid VisitModificationRequest body) {
        visitService.updateVisit(visitId, body);
        return Response.success(null);
    }

    @DeleteMapping("/{visitId}")
    public Response deleteVisit(@PathVariable Long visitId) {
        visitService.deleteVisit(visitId);
        return Response.success(null);
    }
}
