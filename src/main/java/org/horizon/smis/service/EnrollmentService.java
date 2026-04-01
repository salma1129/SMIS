package org.horizon.smis.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.horizon.smis.entity.Course;
import org.horizon.smis.entity.Enrollment;
import org.horizon.smis.entity.Student;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class EnrollmentService {

    @PersistenceContext(unitName = "smisPU")
    private EntityManager em;

    /**
     * Enroll a student in a course with capacity checks.
     */
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        // Lock course row to avoid race conditions (two students enrolling at the same time)
        Course course = em.find(Course.class, courseId, LockModeType.PESSIMISTIC_WRITE);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        Student student = em.find(Student.class, studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        // Check if already enrolled
        boolean alreadyEnrolled = em.createQuery(
                        "select count(e) from Enrollment e where e.student.id = :sid and e.course.id = :cid",
                        Long.class
                ).setParameter("sid", studentId)
                .setParameter("cid", courseId)
                .getSingleResult() > 0;

        if (alreadyEnrolled) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        // Capacity check
        Integer capacity = course.getCapacity(); // can be null
        if (capacity != null) {
            long enrolledCount = em.createQuery(
                            "select count(e) from Enrollment e where e.course.id = :cid",
                            Long.class
                    ).setParameter("cid", courseId)
                    .getSingleResult();

            if (enrolledCount >= capacity) {
                throw new IllegalStateException("Course is full");
            }
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(null);

        em.persist(enrollment);
        return enrollment;
    }

    /**
     * Set/update a grade for an enrollment.
     */
    @Transactional
    public void updateGrade(Long enrollmentId, Double grade) {
        Enrollment e = em.find(Enrollment.class, enrollmentId);
        if (e == null) {
            throw new IllegalArgumentException("Enrollment not found");
        }
        e.setGrade(grade);
        em.merge(e);
    }

    /**
     * Remove an enrollment (drop course).
     */
    @Transactional
    public void unenroll(Long enrollmentId) {
        Enrollment e = em.find(Enrollment.class, enrollmentId);
        if (e != null) em.remove(e);
    }
    public List<Enrollment> findByStudent(Long studentId) {
        return em.createQuery(
                        "select e from Enrollment e " +
                                "join fetch e.course " +
                                "where e.student.id = :sid " +
                                "order by e.enrollmentDate desc",
                        Enrollment.class
                ).setParameter("sid", studentId)
                .getResultList();
    }
    public java.util.List<org.horizon.smis.entity.Enrollment> findAllWithDetails() {
        return em.createQuery(
                "select e from Enrollment e " +
                        "join fetch e.student " +
                        "join fetch e.course " +
                        "order by e.enrollmentDate desc",
                org.horizon.smis.entity.Enrollment.class
        ).getResultList();
    }


}
