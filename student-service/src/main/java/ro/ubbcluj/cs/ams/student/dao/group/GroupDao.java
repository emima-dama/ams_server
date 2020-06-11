package ro.ubbcluj.cs.ams.student.dao.group;

import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;

public interface GroupDao {

    /**
     * @return : id of GroupUniversity
     * */
    Integer addGroup(GroupUniversity group);

//    Group findOneGroupById(int id);
//
//    List<Group> findAllGroupsBySpecializationId(Integer specializationId);

}
