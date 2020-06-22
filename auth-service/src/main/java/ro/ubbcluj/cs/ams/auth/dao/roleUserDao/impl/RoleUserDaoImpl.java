package ro.ubbcluj.cs.ams.auth.dao.roleUserDao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.auth.dao.roleUserDao.RoleUserDao;
import ro.ubbcluj.cs.ams.auth.model.Tables;
import ro.ubbcluj.cs.ams.auth.model.tables.records.RoleUserRecord;

@Component
public class RoleUserDaoImpl implements RoleUserDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(RoleUserDaoImpl.class);


    @Override
    public Result<RoleUserRecord> findByUser(Integer userId) {

        Result<RoleUserRecord> roles = dsl.selectFrom(Tables.ROLE_USER)
                                .fetch();
        logger.info("size {}",roles.size());
        Result<RoleUserRecord> roleUserRecord = dsl.selectFrom(Tables.ROLE_USER)
                                        .where(Tables.ROLE_USER.USER_ID.eq(userId))
                                        .fetch();
        return roleUserRecord;
    }
}
