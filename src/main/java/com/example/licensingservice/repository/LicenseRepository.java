package com.example.licensingservice.repository;

import com.example.licensingservice.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);
    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
