package ro.ubbcluj.cs.ams.subject.dao.activityTypeDao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.subject.dao.activityTypeDao.ActivityTypeDao;
//import ro.ubbcluj.cs.ams.subject.model.Tables;
//import ro.ubbcluj.cs.ams.subject.model.tables.records.ActivityTypeRecord;

@Component
public class ActivityTypeDaoImpl implements ActivityTypeDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(ActivityTypeDao.class);

//    @Override
//    public ActivityTypeRecord findById(Integer id) {
//
//        logger.info("======== LOGGING findById =========");
//
//        ActivityTypeRecord activityTypeRecord = dsl.selectFrom(Tables.ACTIVITY_TYPE)
//                                                .where(Tables.ACTIVITY_TYPE.TYPE_ID.eq(id))
//                                                .fetchOne();
//        logger.info("======== SUCCESSFUL LOGGING findById =========");
//        return activityTypeRecord;
//    }
}
