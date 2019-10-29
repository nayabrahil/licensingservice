package com.example.licensingservice;

import com.example.licensingservice.context.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class LicensingserviceApplication {

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if(interceptors==null){
			restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		}
		else{
			interceptors.add(new UserContextInterceptor());
			restTemplate.setInterceptors(interceptors);
		}
		return restTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(LicensingserviceApplication.class, args);
	}

}
