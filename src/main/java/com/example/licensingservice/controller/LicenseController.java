package com.example.licensingservice.controller;

import com.example.licensingservice.context.UserContextHolder;
import com.example.licensingservice.model.License;
import com.example.licensingservice.service.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LicenseController {

    public static final Logger logger = LoggerFactory.getLogger(LicenseController.class);

    @Autowired
    private LicenseController(final LicenseService licenseService){
        this.licenseService = licenseService;
    }

    private LicenseService licenseService;

    @GetMapping("test")
    public HttpEntity<String> hometest(){
        return new ResponseEntity<>("Rahil", HttpStatus.ACCEPTED);
    }

    @GetMapping(value="/{organizationId}/{licenseId}/{clientType}")
    public License getLicensesWithClient(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId")
                    String licenseId,
            @PathVariable("clientType")
                    String clientType) {
        return licenseService.getLicense(organizationId,
                licenseId, clientType);
    }
    @GetMapping(value = "/v1/organizations/{organizationId}/licenses")
    public List<License> getLicenseForOrg(@PathVariable("organizationId") String organizationId){
        logger.info("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrg(organizationId);
    }
}
