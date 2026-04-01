package org.horizon.smis.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.horizon.smis.entity.Course;
import org.horizon.smis.service.CourseService;

import java.util.List;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    @Inject
    private CourseService courseService;

    @GET
    public List<Course> listCourses() {
        return courseService.findAll();
    }
}
