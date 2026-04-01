package org.horizon.smis.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.horizon.smis.entity.Enrollment;
import org.horizon.smis.rest.dto.EnrollmentCreateDTO;
import org.horizon.smis.service.EnrollmentService;

@Path("/enrollments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnrollmentResource {

    @Inject
    private EnrollmentService enrollmentService;

    @POST
    public Response enroll(EnrollmentCreateDTO dto) {
        if (dto == null || dto.studentId == null || dto.courseId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO("studentId and courseId are required"))
                    .build();
        }

        try {
            Enrollment e = enrollmentService.enrollStudent(dto.studentId, dto.courseId);
            return Response.status(Response.Status.CREATED).entity(e).build();

        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorDTO(ex.getMessage()))
                    .build();

        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorDTO(ex.getMessage()))
                    .build();
        }
    }

    public static class ErrorDTO {
        public String error;
        public ErrorDTO() {}
        public ErrorDTO(String error) { this.error = error; }
    }
}
