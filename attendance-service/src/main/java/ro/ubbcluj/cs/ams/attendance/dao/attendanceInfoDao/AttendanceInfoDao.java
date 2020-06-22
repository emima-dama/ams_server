package ro.ubbcluj.cs.ams.attendance.dao.attendanceInfoDao;

import ro.ubbcluj.cs.ams.attendance.model.tables.pojos.AttendanceInfo;
import ro.ubbcluj.cs.ams.attendance.model.tables.records.AttendanceInfoRecord;
import ro.ubbcluj.cs.ams.attendance.model.tables.records.AttendanceRecord;

public interface AttendanceInfoDao {

    Integer addAttendanceInfo(AttendanceInfo attendanceInfo);

    AttendanceInfoRecord findByCode(String code);

    AttendanceInfoRecord findById(Integer id);
}
