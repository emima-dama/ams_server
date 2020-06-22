package ro.ubbcluj.cs.ams.auth.dao.roleDao;

import ro.ubbcluj.cs.ams.auth.model.tables.records.RoleRecord;

public interface RoleDao {

    RoleRecord findById(Integer roleId);
}
