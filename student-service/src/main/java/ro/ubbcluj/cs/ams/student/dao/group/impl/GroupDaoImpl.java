package ro.ubbcluj.cs.ams.student.dao.group.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.student.dao.group.GroupDao;
import ro.ubbcluj.cs.ams.student.model.Tables;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;
import ro.ubbcluj.cs.ams.student.model.tables.records.GroupUniversityRecord;
import ro.ubbcluj.cs.ams.student.service.exception.StudentExceptionType;
import ro.ubbcluj.cs.ams.student.service.exception.StudentServiceException;

import java.util.List;


@Component
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(GroupDaoImpl.class);

    @Override
    public Integer addGroup(GroupUniversity group) {

        logger.info("========= LOGGING Before addGroup: "+group+" in dao ==========");

        Record1<Integer> idGroup = dsl.insertInto(Tables.GROUP_UNIVERSITY,Tables.GROUP_UNIVERSITY.NAME,Tables.GROUP_UNIVERSITY.SPEC_ID,Tables.GROUP_UNIVERSITY.YEAR)
                .values(group.getName(),group.getSpecId(),group.getYear())
                .returningResult(Tables.GROUP_UNIVERSITY.GROUP_ID)
                .fetchOne();
        if(idGroup == null)
            return null;

        logger.info("========= SUCCESSFUL addGroup with id: "+idGroup.value1()+" ==========");

        return idGroup.value1();
    }

    @Override
    public GroupUniversityRecord findOneByStudent(String student) {

        logger.info(">>>>>>>>>> LOGGING findOneByStudent {} <<<<<<<<<<<<<<<<",student);

        Integer groupId = dsl.selectFrom(Tables.STUDENT)
                .where(Tables.STUDENT.USER_ID.eq(student))
                .fetchOne(Tables.STUDENT.GROUP_ID);
        GroupUniversityRecord groupUniversityRecord = dsl.selectFrom(Tables.GROUP_UNIVERSITY)
                .where(Tables.GROUP_UNIVERSITY.GROUP_ID.eq(groupId))
                .fetchOne();

        if(groupUniversityRecord==null)
            throw new StudentServiceException("There is no group for student "+student, StudentExceptionType.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info(">>>>>>>>> SUCCESSFUL LOGGING findOneByStudent {} <<<<<<<<<<<<<<<<", groupUniversityRecord.getName());
        return groupUniversityRecord;
    }

    @Override
    public List<GroupUniversityRecord> findAll() {

        logger.info(">>>>>>>>>> LOGGING findAll <<<<<<<<<<<<<<<<");

        List<GroupUniversityRecord> groupUniversityRecords = dsl.selectFrom(Tables.GROUP_UNIVERSITY)
                .fetch();

        if(groupUniversityRecords == null)
            throw new StudentServiceException("There is no groups", StudentExceptionType.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info(">>>>>>>>> SUCCESSFUL LOGGING findOneByStudent {} <<<<<<<<<<<<<<<<", groupUniversityRecords.size());
        return groupUniversityRecords;
    }
}
