package com.example.licensingservice.service.discovery;

import com.example.licensingservice.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("organizationservice")
public interface OrganizationFeignClient {

    @GetMapping(value = "/v1/organizations/{organizationId}")
    public Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
