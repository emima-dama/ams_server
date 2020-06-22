package ro.ubbcluj.cs.ams.student.dao.student.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.student.dao.student.StudentDao;
import ro.ubbcluj.cs.ams.student.model.Tables;
import ro.ubbcluj.cs.ams.student.service.exception.StudentExceptionType;
import ro.ubbcluj.cs.ams.student.service.exception.StudentServiceException;

import java.util.List;

@Component
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(StudentDaoImpl.class);

    @Override
    public List<String> findStudentsByGroup(Integer group) {

        logger.info(">>>>>>>>>> LOGGING findStudentsByGroup {} <<<<<<<<<<<<<<<<",group);

        List<String> students = dsl.selectFrom(Tables.STUDENT)
                .where(Tables.STUDENT.GROUP_ID.eq(group))
                .fetch(Tables.STUDENT.USER_ID);


        if(students == null)
            throw new StudentServiceException("There is no users of students for group "+group, StudentExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info(">>>>>>>>> SUCCESSFUL LOGGING findStudentsByGroup {} <<<<<<<<<<<<<<<<", students.size());
        return students;
    }
}
