package ro.ubbcluj.cs.ams.subject.dao.subjectDao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.subject.dao.subjectDao.SubjectDao;
import ro.ubbcluj.cs.ams.subject.model.Tables;
import ro.ubbcluj.cs.ams.subject.model.tables.pojos.Subject;
import ro.ubbcluj.cs.ams.subject.model.tables.records.SubjectRecord;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectExceptionType;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectServiceException;

@Component
public class SubjectDaoImpl implements SubjectDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(SubjectDaoImpl.class);


    @Override
    public SubjectRecord addSubject(Subject subject) {

        logger.info(">>>>>>>>>>>> Before add subject <<<<<<<<<<<<<<<");

        SubjectRecord subjectRecord = dsl.insertInto(Tables.SUBJECT,Tables.SUBJECT.ID,Tables.SUBJECT.NAME,Tables.SUBJECT.CREDITS,Tables.SUBJECT.SPEC_ID,Tables.SUBJECT.YEAR)
                .values(subject.getId(),subject.getName(),subject.getCredits(),subject.getSpecId(),subject.getYear())
                .returning()
                .fetchOne();

        logger.info(">>>>>>>>>>>> After add subject <<<<<<<<<<<<<<<");
        return subjectRecord;
    }

    @Override
    public SubjectRecord findById(String id) {

        logger.info(">>>>>>>>>>>> LOGGING Before findById <<<<<<<<<<<<<<<");

        SubjectRecord subjectRecord = dsl.selectFrom(Tables.SUBJECT)
                .where(Tables.SUBJECT.ID.eq(id))
                .fetchOne();

        if(subjectRecord==null)
            throw new SubjectServiceException("There is no subject with id "+id, SubjectExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info(">>>>>>>>>>>> LOGGING after findById {} {} {} {} {}<<<<<<<<<<<<<<<",subjectRecord.getId(),subjectRecord.getName(),subjectRecord.getCredits(),subjectRecord.getSpecId(),subjectRecord.getYear());
        return subjectRecord;
    }

    @Override
    public String findNameById(String id) {

        logger.info(" >>>>>>>>>>> LOGGING findNameById  {} <<<<<<<<<<",id);

        String name = dsl.selectFrom(Tables.SUBJECT)
                .where(Tables.SUBJECT.ID.eq(id))
                .fetchOne(Tables.SUBJECT.NAME);


        logger.info(" >>>>>>>>>>> SUCCESSFUL LOGGING findNameById  {} <<<<<<<<<<",name);

        return name;
    }
}
