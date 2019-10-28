package com.example.licensingservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Organization {
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
