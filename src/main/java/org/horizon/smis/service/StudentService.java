package org.horizon.smis.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.horizon.smis.entity.Student;

import java.util.List;

@ApplicationScoped
public class StudentService {

    @PersistenceContext(unitName = "smisPU")
    private EntityManager em;

    public List<Student> findAll() {
        return em.createQuery("select s from Student s order by s.id desc", Student.class)
                .getResultList();
    }

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }

    @Transactional
    public void create(Student s) {
        em.persist(s);
    }

    @Transactional
    public Student update(Student s) {
        return em.merge(s);
    }

    @Transactional
    public void delete(Long id) {
        Student s = em.find(Student.class, id);
        if (s != null) em.remove(s);
    }
}
