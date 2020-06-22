package ro.ubbcluj.cs.ams.attendance.service;

import ro.ubbcluj.cs.ams.attendance.dto.*;

public interface Service {

    AttendanceInfoResponse addAttendanceInfo(AttendanceInfoReq attendanceInfoReq, String username);

    AttendanceResponse addAttendance(AttendanceRequest attendanceRequest,String username);

    AttendancesByTeacherResponseDto findAttendancesForStudentByTeacher(String studentId, String teacher);

    AttendancesByTeacherResponseDto findAttendancesByStudent(String student);
}
