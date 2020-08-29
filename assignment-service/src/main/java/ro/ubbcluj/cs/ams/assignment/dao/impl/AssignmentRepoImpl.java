package ro.ubbcluj.cs.ams.assignment.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.cs.ams.assignment.model.Tables;
import ro.ubbcluj.cs.ams.assignment.model.tables.pojos.Grade;
import ro.ubbcluj.cs.ams.assignment.model.tables.records.GradeRecord;
import ro.ubbcluj.cs.ams.assignment.dao.AssignmentRepo;

import java.util.List;

@Repository
public class AssignmentRepoImpl implements AssignmentRepo {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(AssignmentRepoImpl.class);

    @Override
    public GradeRecord addGrade(Grade grade) {

        logger.info(">>>>>>>>>>>>>>>> Before addGrade <<<<<<<<<<<<<");
        logger.info("Grade {}", grade);

        GradeRecord gradeRecord = dsl.insertInto(Tables.GRADE, Tables.GRADE.TYPE_ID, Tables.GRADE.TEACHER, Tables.GRADE.STUDENT, Tables.GRADE.VALUE, Tables.GRADE.SUBJECT_ID, Tables.GRADE.DATE)
                .values(grade.getTypeId(), grade.getTeacher(), grade.getStudent(), grade.getValue(), grade.getSubjectId(), grade.getDate())
                .returning()
                .fetchOne();
        logger.info(">>>>>>>>>>>>>>>> addGrade successful <<<<<<<<<<<<<");
        return gradeRecord;
    }

    @Override
    public List<GradeRecord> findAllByStudent(String student) {

        logger.info(">>>>>>>>>>>>>>>> Before findAllByStudent <<<<<<<<<<<<<");

        List<GradeRecord> gradeRecord = dsl.selectFrom(Tables.GRADE)
                .where(Tables.GRADE.STUDENT.eq(student))
                .fetch();
        logger.info(">>>>>>>>>>>>>>>> findAllByStudent successful <<<<<<<<<<<<<");
        return gradeRecord;
    }

    @Override
    public List<GradeRecord> findAllByStudentAndSubject(String name, String subject) {

        logger.info(">>>>>>>>>>>>>>>> Before findAllByStudent <<<<<<<<<<<<<");

        List<GradeRecord> gradeRecord = dsl.selectFrom(Tables.GRADE)
                .where(Tables.GRADE.STUDENT.eq(name),Tables.GRADE.SUBJECT_ID.eq(subject))
                .fetch();
        logger.info(">>>>>>>>>>>>>>>> findAllByStudent successful <<<<<<<<<<<<<");
        return gradeRecord;
    }
}
