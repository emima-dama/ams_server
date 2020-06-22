package ro.ubbcluj.cs.ams.attendance.dao.attendanceDao;

import ro.ubbcluj.cs.ams.attendance.model.tables.pojos.Attendance;
import ro.ubbcluj.cs.ams.attendance.model.tables.records.AttendanceRecord;

import java.util.List;

public interface AttendanceDao {

    AttendanceRecord addAttendance(Attendance attendance);

    List<AttendanceRecord> findAllByStudent(String student);
}
