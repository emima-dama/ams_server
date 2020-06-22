package ro.ubbcluj.cs.ams.auth.dao.roleDao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.ams.auth.dao.roleDao.RoleDao;
import ro.ubbcluj.cs.ams.auth.model.Tables;
import ro.ubbcluj.cs.ams.auth.model.tables.records.RoleRecord;

@Component
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private DSLContext dsl;

    private final Logger logger = LogManager.getLogger(RoleDaoImpl.class);


    @Override
    public RoleRecord findById(Integer roleId) {

        logger.info("role id {}",roleId);

        RoleRecord roleUserRecord = dsl.selectFrom(Tables.ROLE)
                .where(Tables.ROLE.ID.eq(roleId))
                .fetchOne();
        return roleUserRecord;
    }
}
