package com.example.licensingservice.service.discovery;

import com.example.licensingservice.common.ApiCaller;
import com.example.licensingservice.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    @Autowired
    private RestTemplate restTemplate;
//    private ApiCaller apiCaller;

    public Organization getOrganization(String organizationId){

        ResponseEntity< Organization > restExchange =
                restTemplate.exchange(
                        "http://organizationservice/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        /*Organization organization = new Organization();
        ParameterizedTypeReference<Organization> responseType = new ParameterizedTypeReference<Organization>() {};
        try {
            organization =
                    apiCaller.syncGet(responseType, String.format("http://organizationservice/v1/organizations/%s", organizationId));
        }
        catch(Exception e){
            logger.error("Error getting organization : ", e);
        }*/
        return restExchange.getBody();
    }
}
