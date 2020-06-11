package ro.ubbcluj.cs.ams.student.dao.enrollment;

import ro.ubbcluj.cs.ams.student.model.tables.pojos.Enrollment;
import ro.ubbcluj.cs.ams.student.model.tables.records.EnrollmentRecord;

import java.util.List;

public interface EnrollmentDao {

    EnrollmentRecord addEnrollment(Enrollment enrollment);

    List<String> getStundentsBySubject(String subjectId);

    List<String> getSubjectsByStudent(String studentId);
}
