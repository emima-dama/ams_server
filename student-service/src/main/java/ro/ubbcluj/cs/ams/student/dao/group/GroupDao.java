package ro.ubbcluj.cs.ams.student.dao.group;

import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;
import ro.ubbcluj.cs.ams.student.model.tables.records.GroupUniversityRecord;

import java.util.List;

public interface GroupDao {

    Integer addGroup(GroupUniversity group);

    GroupUniversityRecord findOneByStudent(String student);

    List<GroupUniversityRecord> findAll();

}
