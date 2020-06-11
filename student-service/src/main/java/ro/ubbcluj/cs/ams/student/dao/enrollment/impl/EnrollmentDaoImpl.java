package ro.ubbcluj.cs.ams.student.dao.enrollment.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.student.dao.enrollment.EnrollmentDao;
import ro.ubbcluj.cs.ams.student.model.Tables;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.Enrollment;
import ro.ubbcluj.cs.ams.student.model.tables.records.EnrollmentRecord;

import java.util.List;

@Component
public class EnrollmentDaoImpl implements EnrollmentDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(EnrollmentDaoImpl.class);

    @Override
    public EnrollmentRecord addEnrollment(Enrollment enrollment) {

        logger.info("========= LOGGING Before addGroup: "+enrollment+" in dao ==========");

        EnrollmentRecord enrollmentRecord = dsl.insertInto(Tables.ENROLLMENT, Tables.ENROLLMENT.STUDENT_ID, Tables.ENROLLMENT.SUBJECT_ID)
                                            .values(enrollment.getStudentId(),enrollment.getSubjectId())
                                            .returning()
                                            .fetchOne();


        logger.info("========= LOGGING After addGroup: "+enrollmentRecord+" ==========");
        return enrollmentRecord;
    }

    @Override
    public List<String> getStundentsBySubject(String subjectId) {

        logger.info("=========== LOGGING Before getStudentsBySubject  {} ========= ",subjectId);

        List<String> students = dsl.selectFrom(Tables.ENROLLMENT)
                            .where(Tables.ENROLLMENT.SUBJECT_ID.eq(subjectId))
                            .fetch(Tables.ENROLLMENT.STUDENT_ID);

        logger.info("=========== LOGGING After getStudentBySubject =========");
        return students;
    }

    @Override
    public List<String> getSubjectsByStudent(String studentId) {

        logger.info("=========== LOGGING Before getSubjectsByStudent  {} ========= ",studentId);

        List<String> subjects = dsl.selectFrom(Tables.ENROLLMENT)
                .where(Tables.ENROLLMENT.STUDENT_ID.eq(studentId))
                .fetch(Tables.ENROLLMENT.SUBJECT_ID);

        logger.info("=========== LOGGING After getSubjectsByStudent =========");
        return subjects;
    }
}
