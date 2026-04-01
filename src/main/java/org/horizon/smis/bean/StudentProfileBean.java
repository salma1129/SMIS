package org.horizon.smis.bean;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.horizon.smis.entity.Enrollment;
import org.horizon.smis.entity.Student;
import org.horizon.smis.service.EnrollmentService;
import org.horizon.smis.service.StudentService;

import java.io.Serializable;
import java.util.List;

@Named("studentProfileBean")
@ViewScoped
public class StudentProfileBean implements Serializable {

    private Long studentId;

    private Student student;
    private List<Enrollment> enrollments;

    @Inject
    private StudentService studentService;

    @Inject
    private EnrollmentService enrollmentService;

    public void load() {
        if (studentId == null) return;

        student = studentService.findById(studentId);
        enrollments = enrollmentService.findByStudent(studentId);
    }

    // getters/setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Student getStudent() { return student; }
    public List<Enrollment> getEnrollments() { return enrollments; }
}
