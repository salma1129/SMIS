package org.horizon.smis.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.horizon.smis.entity.Enrollment;
import org.horizon.smis.service.EnrollmentService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("gradesBean")
@ViewScoped
public class GradesBean implements Serializable {

    @Inject
    private EnrollmentService enrollmentService;

    private List<Enrollment> enrollments;

    // holds edited grades by enrollmentId
    private final Map<Long, Double> gradeEdits = new HashMap<>();

    public void load() {
        enrollments = enrollmentService.findAllWithDetails();
        gradeEdits.clear();
        for (Enrollment e : enrollments) {
            gradeEdits.put(e.getId(), e.getGrade());
        }
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public Map<Long, Double> getGradeEdits() {
        return gradeEdits;
    }

    public void saveGrade(Long enrollmentId) {
        try {
            Double g = gradeEdits.get(enrollmentId);

            // Optional: basic validation (adjust scale if needed)
            if (g != null && (g < 0 || g > 20)) {
                msg(FacesMessage.SEVERITY_ERROR, "Grade must be between 0 and 20.");
                return;
            }

            enrollmentService.updateGrade(enrollmentId, g);
            msg(FacesMessage.SEVERITY_INFO, "Grade saved.");

            // refresh data
            load();

        } catch (Exception e) {
            msg(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage());
        }
    }

    private void msg(FacesMessage.Severity sev, String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, text, null));
    }
}
