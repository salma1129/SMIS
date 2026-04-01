package org.horizon.smis.rest.dto;

import java.time.LocalDate;

public class StudentResponseDTO {
    public Long id;
    public String name;
    public String email;
    public LocalDate enrollmentDate;
    public String major;
    public Double gpa;
}
