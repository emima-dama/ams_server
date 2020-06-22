package ro.ubbcluj.cs.ams.student.dao.student;

import java.util.List;

public interface StudentDao {

    List<String> findStudentsByGroup(Integer group);
}
