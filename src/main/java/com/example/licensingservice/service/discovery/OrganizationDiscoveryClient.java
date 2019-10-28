package com.example.licensingservice.service.discovery;

import java.util.List;

import com.example.licensingservice.common.ApiCaller;
import com.example.licensingservice.model.Organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class OrganizationDiscoveryClient {

    @Autowired
    public OrganizationDiscoveryClient(ApiCaller apiCaller,
                                       DiscoveryClient discoveryClient){
        this.apiCaller=apiCaller;
        this.discoveryClient=discoveryClient;
    }
    private ApiCaller apiCaller;
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId){
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

        if(instances.isEmpty()){
            return null;
        }

        String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);

        ParameterizedTypeReference<Organization> responseType = new ParameterizedTypeReference<Organization>() {};

        Organization organization =
                apiCaller.syncGet(responseType, serviceUri);
        return organization;

    }
}
