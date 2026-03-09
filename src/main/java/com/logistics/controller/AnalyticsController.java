package com.logistics.controller;

import com.logistics.dto.AnalyticsResponse;
import com.logistics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * GET /analytics - Get real-time business intelligence metrics
     * All calculations done using Java Stream API (no SQL aggregations)
     */
    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        AnalyticsResponse analytics = analyticsService.getAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
