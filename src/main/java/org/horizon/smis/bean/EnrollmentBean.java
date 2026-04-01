package org.horizon.smis.bean;


import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.horizon.smis.entity.Course;
import org.horizon.smis.entity.Student;
import org.horizon.smis.service.CourseService;
import org.horizon.smis.service.EnrollmentService;
import org.horizon.smis.service.StudentService;

import java.io.Serializable;
import java.util.List;

@Named("enrollmentBean")
@ViewScoped
public class EnrollmentBean implements Serializable {

    private Long studentId;
    private Long courseId;

    @Inject
    private StudentService studentService;

    @Inject
    private CourseService courseService;

    @Inject
    private EnrollmentService enrollmentService;

    public List<Student> getStudents() {
        return studentService.findAll();
    }

    public List<Course> getCourses() {
        return courseService.findAll();
    }

    public void enroll() {
        if (studentId == null || courseId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a student and a course.", null));
            return;
        }

        try {
            enrollmentService.enrollStudent(studentId, courseId);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Enrollment successful!", null));

            // reset selection
            studentId = null;
            courseId = null;

        } catch (IllegalStateException | IllegalArgumentException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error أثناء التسجيل.", null));
        }
    }

    // getters/setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}
