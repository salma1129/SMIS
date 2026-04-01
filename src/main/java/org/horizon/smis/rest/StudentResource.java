package org.horizon.smis.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.horizon.smis.entity.Student;
import org.horizon.smis.rest.dto.StudentCreateDTO;
import org.horizon.smis.rest.dto.StudentResponseDTO;
import org.horizon.smis.service.StudentService;

import java.net.URI;

@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {

    @Inject
    private StudentService studentService;

    @GET
    @Path("/{id}")
    public Response getStudent(@PathParam("id") Long id) {
        Student s = studentService.findById(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorDTO("Student not found"))
                    .build();
        }

        StudentResponseDTO dto = toDto(s);
        return Response.ok(dto).build();
    }

    @POST
    public Response createStudent(StudentCreateDTO dto) {
        if (dto == null || dto.name == null || dto.name.isBlank()
                || dto.email == null || dto.email.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO("name and email are required"))
                    .build();
        }

        Student s = new Student();
        s.setName(dto.name);
        s.setEmail(dto.email);
        s.setEnrollmentDate(dto.enrollmentDate);
        s.setMajor(dto.major);
        s.setGpa(dto.gpa);

        studentService.create(s);

        // return created object
        StudentResponseDTO out = toDto(s);
        return Response.created(URI.create("/api/students/" + s.getId()))
                .entity(out)
                .build();
    }

    private StudentResponseDTO toDto(Student s) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.id = s.getId();
        dto.name = s.getName();
        dto.email = s.getEmail();
        dto.enrollmentDate = s.getEnrollmentDate();
        dto.major = s.getMajor();
        dto.gpa = s.getGpa();
        return dto;
    }

    // simple error JSON
    public static class ErrorDTO {
        public String error;
        public ErrorDTO() {}
        public ErrorDTO(String error) { this.error = error; }
    }
}
