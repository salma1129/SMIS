package org.horizon.smis.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.horizon.smis.entity.Student;
import org.horizon.smis.service.StudentService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named("studentBean")
@ViewScoped
public class StudentBean implements Serializable {

    @Inject
    private StudentService studentService;

    private Student form = new Student();
    private Long editId; // null => create mode, not null => edit mode

    public List<Student> getStudents() {
        return studentService.findAll();
    }

    public boolean isEditMode() {
        return editId != null;
    }

    public Student getForm() {
        return form;
    }

    public void setForm(Student form) {
        this.form = form;
    }

    public void save() {
        try {
            // basic default
            if (form.getEnrollmentDate() == null) {
                form.setEnrollmentDate(LocalDate.now());
            }

            if (!isEditMode()) {
                studentService.create(form);
                addMsg(FacesMessage.SEVERITY_INFO, "Student created successfully.");
            } else {
                form.setId(editId);
                studentService.update(form);
                addMsg(FacesMessage.SEVERITY_INFO, "Student updated successfully.");
            }

            resetForm();

        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage());
        }
    }

    public void edit(Long id) {
        Student s = studentService.findById(id);
        if (s == null) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Student not found.");
            return;
        }

        this.editId = id;

        // copy values into form (avoid weird binding side-effects)
        Student copy = new Student();
        copy.setId(s.getId());
        copy.setName(s.getName());
        copy.setEmail(s.getEmail());
        copy.setEnrollmentDate(s.getEnrollmentDate());
        copy.setMajor(s.getMajor());
        copy.setGpa(s.getGpa());
        this.form = copy;
    }

    public void delete(Long id) {
        try {
            studentService.delete(id);
            addMsg(FacesMessage.SEVERITY_INFO, "Student deleted.");

            // if you delete the one currently being edited
            if (editId != null && editId.equals(id)) {
                resetForm();
            }

        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Delete failed: " + e.getMessage());
        }
    }

    public void cancelEdit() {
        resetForm();
        addMsg(FacesMessage.SEVERITY_INFO, "Edit cancelled.");
    }

    private void resetForm() {
        this.form = new Student();
        this.editId = null;
    }

    private void addMsg(FacesMessage.Severity severity, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, null));
    }
}
