package ro.ubbcluj.cs.ams.attendance.service.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import ro.ubbcluj.cs.ams.attendance.controller.MicroserviceCall;
import ro.ubbcluj.cs.ams.attendance.dao.attendanceDao.AttendanceDao;
import ro.ubbcluj.cs.ams.attendance.dao.attendanceInfoDao.AttendanceInfoDao;
import ro.ubbcluj.cs.ams.attendance.dto.*;


import ro.ubbcluj.cs.ams.attendance.model.tables.pojos.Attendance;
import ro.ubbcluj.cs.ams.attendance.model.tables.pojos.AttendanceInfo;
import ro.ubbcluj.cs.ams.attendance.model.tables.records.AttendanceInfoRecord;
import ro.ubbcluj.cs.ams.attendance.model.tables.records.AttendanceRecord;
import ro.ubbcluj.cs.ams.attendance.service.Service;
import ro.ubbcluj.cs.ams.attendance.service.exception.AttendanceExceptionType;
import ro.ubbcluj.cs.ams.attendance.service.exception.AttendanceServiceException;

import javax.inject.Provider;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@org.springframework.stereotype.Service
public class AttendanceService implements Service {

    @Autowired
    private AttendanceInfoDao attendanceInfoDao;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private Provider<Instant> instantProvider;

    @Value("${size.code}")
    private Integer sizeCode;

    @Autowired
    MicroserviceCall microserviceCall;


    private Logger logger = LogManager.getLogger(AttendanceService.class);

    public AttendanceInfoResponse addAttendanceInfo(AttendanceInfoReq attendanceInfoReq, String username) {

        logger.info("+++++++++ addAttendanceInfo in service +++++++");

        LocalDateTime createdAt = LocalDateTime.ofInstant(instantProvider.get(), ZoneOffset.UTC);
        String barcode = this.generateBarcode();
        AttendanceInfo attendanceInfo = new AttendanceInfo(Integer.parseInt("0"),attendanceInfoReq.getCourseId(),attendanceInfoReq.getActivityId(), username, createdAt,attendanceInfoReq.getRemainingTime(),barcode);
        Integer attendanceInfo_id = attendanceInfoDao.addAttendanceInfo(attendanceInfo);

        return AttendanceInfoResponse.builder()
                .barcode(barcode)
                .createdAt(createdAt)
                .build();
//        return null;
    }

    @Override
    public AttendanceResponse addAttendance(AttendanceRequest attendanceRequest, String username) {

        logger.info("+++++++++ addAttendance in service +++++++");

        AttendanceInfoRecord attendanceInfoRecord = attendanceInfoDao.findByCode(attendanceRequest.getAttendanceInfoCode());
        if(attendanceInfoRecord == null)
            throw new AttendanceServiceException("Attendance info not found", AttendanceExceptionType.ATTENDANCE_INFO_NOT_FOUND, HttpStatus.BAD_REQUEST);

        LocalDateTime currentTime = LocalDateTime.ofInstant(instantProvider.get(), ZoneOffset.UTC);
        LocalDateTime createdAt = attendanceInfoRecord.getCreatedAt();

        if(createdAt.plusMinutes(attendanceInfoRecord.getRemainingTime()).isBefore(currentTime))
            throw new AttendanceServiceException("Time for this attendance has expired! ", AttendanceExceptionType.INVALID_TIME, HttpStatus.BAD_REQUEST);

        Attendance attendance = new Attendance(Integer.valueOf("0"),username,createdAt,attendanceInfoRecord.getId());
        AttendanceRecord attendanceAdded = attendanceDao.addAttendance(attendance);

        if(attendanceAdded.equals(null))
            throw new AttendanceServiceException("Attendance with id "+attendanceInfoRecord.getId()+" for student "+username+" cannot be added!",AttendanceExceptionType.INVALID_ATTENDANCE,HttpStatus.BAD_REQUEST);

        SubjectResponseDto subjectResponseDto = microserviceCall.getSubjectActivityNames(attendanceInfoRecord.getCourseId(),attendanceInfoRecord.getActivityId());

        return AttendanceResponse.builder()
                .course(subjectResponseDto.getSubjectName())
                .activity(subjectResponseDto.getActivityName())
                .build();
    }

    private String generateBarcode() {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(sizeCode);
        for (int i = 0; i < sizeCode; i++) {

            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
