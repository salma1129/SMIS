package org.horizon.smis.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.horizon.smis.entity.Course;

import java.util.List;

@ApplicationScoped
public class CourseService {

    @PersistenceContext(unitName = "smisPU")
    private EntityManager em;

    public List<Course> findAll() {
        return em.createQuery("select c from Course c order by c.id desc", Course.class)
                .getResultList();
    }

    public Course findById(Long id) {
        return em.find(Course.class, id);
    }

    @Transactional
    public void create(Course c) {
        em.persist(c);
    }

    @Transactional
    public Course update(Course c) {
        return em.merge(c);
    }

    @Transactional
    public void delete(Long id) {
        Course c = em.find(Course.class, id);
        if (c != null) em.remove(c);
    }

    // Optional helper: current number of enrolled students
    public long getEnrolledCount(Long courseId) {
        return em.createQuery(
                "select count(e) from Enrollment e where e.course.id = :cid",
                Long.class
        ).setParameter("cid", courseId).getSingleResult();
    }
}
