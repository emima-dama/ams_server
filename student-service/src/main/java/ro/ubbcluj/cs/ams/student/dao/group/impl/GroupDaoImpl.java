package ro.ubbcluj.cs.ams.student.dao.group.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.student.dao.group.GroupDao;
import ro.ubbcluj.cs.ams.student.model.Tables;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;


@Component
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(GroupDaoImpl.class);

    @Override
    public Integer addGroup(GroupUniversity group) {

        logger.info("========= LOGGING Before addGroup: "+group+" in dao ==========");

        Record1<Integer> idGroup = dsl.insertInto(Tables.GROUP_UNIVERSITY, Tables.GROUP_UNIVERSITY.NAME, Tables.GROUP_UNIVERSITY.SPEC_ID, Tables.GROUP_UNIVERSITY.YEAR)
                                    .values(group.getName(),group.getSpecId(),group.getYear())
                                    .returningResult(Tables.GROUP_UNIVERSITY.GROUP_ID)
                                    .fetchOne();
        if(idGroup == null)
            return null;

        logger.info("========= SUCCESSFUL addGroup with id: "+idGroup.value1()+" ==========");

        return idGroup.value1();
    }
}
