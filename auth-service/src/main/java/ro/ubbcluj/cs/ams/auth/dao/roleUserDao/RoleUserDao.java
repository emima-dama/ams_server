package ro.ubbcluj.cs.ams.auth.dao.roleUserDao;

import org.jooq.Result;
import ro.ubbcluj.cs.ams.auth.model.tables.records.RoleUserRecord;

public interface RoleUserDao {

    Result<RoleUserRecord> findByUser(Integer userId);
}
