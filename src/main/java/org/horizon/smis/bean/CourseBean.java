package org.horizon.smis.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.horizon.smis.entity.Course;
import org.horizon.smis.service.CourseService;

import java.io.Serializable;
import java.util.List;

@Named("courseBean")
@ViewScoped
public class CourseBean implements Serializable {

    @Inject
    private CourseService courseService;

    private Course form = new Course();
    private Long editId;

    public List<Course> getCourses() {
        return courseService.findAll();
    }

    public Course getForm() {
        return form;
    }

    public void setForm(Course form) {
        this.form = form;
    }

    public boolean isEditMode() {
        return editId != null;
    }

    public void save() {
        try {
            if (!isEditMode()) {
                courseService.create(form);
                msg(FacesMessage.SEVERITY_INFO, "Course created successfully.");
            } else {
                form.setId(editId);
                courseService.update(form);
                msg(FacesMessage.SEVERITY_INFO, "Course updated successfully.");
            }
            reset();
        } catch (Exception e) {
            msg(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage());
        }
    }

    public void edit(Long id) {
        Course c = courseService.findById(id);
        if (c == null) {
            msg(FacesMessage.SEVERITY_ERROR, "Course not found.");
            return;
        }

        this.editId = id;

        Course copy = new Course();
        copy.setId(c.getId());
        copy.setName(c.getName());
        copy.setInstructor(c.getInstructor());
        copy.setCredits(c.getCredits());
        copy.setCapacity(c.getCapacity());
        this.form = copy;
    }

    public void delete(Long id) {
        try {
            courseService.delete(id);
            msg(FacesMessage.SEVERITY_INFO, "Course deleted.");

            if (editId != null && editId.equals(id)) {
                reset();
            }
        } catch (Exception e) {
            msg(FacesMessage.SEVERITY_ERROR, "Delete failed: " + e.getMessage());
        }
    }

    public void cancelEdit() {
        reset();
        msg(FacesMessage.SEVERITY_INFO, "Edit cancelled.");
    }

    private void reset() {
        this.form = new Course();
        this.editId = null;
    }

    private void msg(FacesMessage.Severity sev, String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, text, null));
    }
}
