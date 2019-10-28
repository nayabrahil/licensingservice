package com.example.licensingservice.service;


import com.example.licensingservice.common.GenericBuilder;
import com.example.licensingservice.config.ServiceConfig;
import com.example.licensingservice.context.UserContextHolder;
import com.example.licensingservice.model.License;
import com.example.licensingservice.model.Organization;
import com.example.licensingservice.repository.LicenseRepository;
import com.example.licensingservice.service.discovery.OrganizationDiscoveryClient;
import com.example.licensingservice.service.discovery.OrganizationFeignClient;
import com.example.licensingservice.service.discovery.OrganizationRestTemplateClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LicenseService {

    public static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    ServiceConfig config;
    @Autowired
    OrganizationFeignClient organizationFeignClient;
    @Autowired
    private OrganizationDiscoveryClient organizationDiscoveryClient;
    @Autowired
    private OrganizationRestTemplateClient organizationRestClient;

    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(
                organizationId, licenseId);
        license.setComment(config.getExampleProperty());
        return license;
    }

    public License getLicense(String organizationId, String licenseId, String clientType) {
        licenseRepository.findByOrganizationIdAndLicenseId(
                organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId, clientType);

        return GenericBuilder.of(License::new)
                .with(License::setOrganizationName, org.getName())
                .with(License::setContactEmail, org.getContactEmail())
                .with(License::setContactName, org.getContactName())
                .with(License::setContactPhone, org.getContactPhone())
                .build();

    }

    private void randomlyRunLong() {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value="10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value="7000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value="7000")
    })
    public List<License> getLicensesByOrg(String organizationId) {
        //randomlyRunLong();
        logger.info("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        return Arrays.asList(
                GenericBuilder.of(License::new)
                        .with(License::setLicenseId, "0000000-00-00000")
                        .with(License::setOrganizationId, organizationId)
                        .with(License::setProductName, "Sorry no licensing information found")
                        .build());
    }

    public void saveLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "3000")})
    private Organization retrieveOrgInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                //organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }
}
